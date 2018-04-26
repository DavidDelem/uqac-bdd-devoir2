package Exercice2.Combat2


import Exercice2.{Link, LivingEntity, LivingEntityPrototype}
import Exercice2.Utils.Position
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeContext, Graph, TripletFields}
import net.liftweb.json._
import org.jfarcand.wcs.{TextListener, WebSocket}

class Game extends Serializable {

  //--------------------
  // EXECUTE GAME
  //--------------------

  def execute(g: Graph[LivingEntity, Link], sc: SparkContext, maxIterations: Int): Graph[LivingEntity, Link] = {

    var myGraph = g
    var roundCounter = 0
    val fields = new TripletFields(true, true, true) //join strategy
    implicit val formats: DefaultFormats.type = DefaultFormats

    //WebSocket Client to send real-time data to the GUI
    val webSocketClient = WebSocket()
      .open("ws://127.0.0.1:8089/fight")
      .listener(new TextListener {
        override def onOpen(){ println("WSClient connected") }
        override def onClose(){ println("WSClient disconnected") }
        override def onMessage(message: String): Unit = {println("Message = " + message)}
        override def onError(t: Throwable): Unit = {println("Error = " + t)}
      })

    webSocketClient.send("FightBeginning")

    def gameLoop(): Unit = {

      while (true) {
        if(roundCounter%10 == 0) {
          myGraph.checkpoint();
        }
        roundCounter+=1
        println("================ Battle round : " + roundCounter + " ================")

        //--------------------------------------
        // CHECKPOINT + CLEAR ALL DEAD ENTITIES
        //--------------------------------------

        if(roundCounter%10==0) myGraph.checkpoint()

        myGraph = myGraph.subgraph(vpred = (_, attr) =>  attr.hp > 0)


        //--------------------
        // MOVING + REGENERATE UPDATE
        //--------------------
        if(roundCounter > 1){

          val newVerticesMove = myGraph.vertices.map(vertex => {
            if(vertex._2.hp > 0){
              vertex._2.regenerate()
              vertex._2.move()
              vertex._2.hurtDuringRound = false
            }
            vertex
          })

          myGraph = Graph(newVerticesMove, myGraph.edges)
        }
        //--------------------
        // TARGET UPDATE
        //--------------------

        val targetMessages = myGraph.aggregateMessages[List[LivingEntity]](
          sendTargetMsg,
          mergeTargetMsg,
          fields
        )

        myGraph = myGraph.joinVertices(targetMessages) {

          (_, fighter, allTargets) => {

            val newFighter = LivingEntityPrototype.create(fighter)
            newFighter.setTargets(allTargets)
            newFighter
          }
        }

        //--------------------
        // DAMAGE UPDATE
        //--------------------

        val damageMessages = myGraph.aggregateMessages[Int](
          sendDamageMsg,
          mergeDamageMsg,
          fields
        )

        myGraph = myGraph.joinVertices(damageMessages) {

          (_, damageReceiver, damages) => {

            if(damages>0){
              val newDamageReceiver = LivingEntityPrototype.create(damageReceiver)
              newDamageReceiver.takeDamage(damages)
              newDamageReceiver
            }else
              damageReceiver

          }
        }

        //----------------------------------------
        // SAVE RDD ROUND FOR GUI (SERIALIZATION)
        //----------------------------------------
        //TODO: how to serialize without creating a new LivingEntity ?
        val roundVerticesRDD = myGraph.vertices.map(vertex => (
          vertex._1, new LivingEntity(
          vertex._2.id,
          vertex._2.name,
          vertex._2.hpmax,
          vertex._2.hp,
          vertex._2.armor,
          vertex._2.position,
          vertex._2.team,
          vertex._2.regeneration,
          vertex._2.speeds,
          vertex._2.melee,
          vertex._2.ranged,
          vertex._2.special,
          vertex._2.maxTargets,
          vertex._2.targets,
          vertex._2.hurtDuringRound)
        )).collect()

        webSocketClient.send(net.liftweb.json.Serialization.write(roundVerticesRDD))

        // Print graph
        //GraphConsole.printLivingEntityGraphVertices(myGraph)

        //------------------------
        // END LOOP CONDITIONS
        //------------------------

        // Récupération du nombre d'alliés et ennemis toujours en vie
        val nbBadGuysAlive = myGraph.vertices.filter{ vertex => vertex._2.team == "BadGuys" && vertex._2.hp > 0}.count
        val nbGoodGuysAlive = myGraph.vertices.filter{ vertex =>  vertex._2.team == "GoodGuys" && vertex._2.hp > 0}.count
        println("nbBadGuysAlive : " + nbBadGuysAlive)
        println("nbGoodGuysAlive : " + nbGoodGuysAlive)

        // Conditions d'arrêt: victoire des ennemis ou des alliés
        if(nbBadGuysAlive == 0){
          println("END OF LOOP : Solar and his friends saved Pito's village :D")
          return
        }
        else if(nbGoodGuysAlive == 0){
          println("END OF LOOP : Unfortunatly, all Angels and Pito died! Bad guys won :(")
          return
        }
        else if (roundCounter == maxIterations) return

      }

    }

    //execute loop
    gameLoop()
    //close & shutdown WS client
    webSocketClient.close
    webSocketClient.shutDown
    //return the result graph
    myGraph
  }


  //------------------------------------------------------------------------------
  //-------------- FONCTION UTILISEES DANS LES AGGREGATEMESSAGES  ----------------
  //------------------------------------------------------------------------------

  // Première fonction de l'aggregateMessages gérant la MAJ de la target
  // On regarde si ils sont enemis et si oui on complete le triplet
  def sendTargetMsg(triplet: EdgeContext[LivingEntity, Link, List[LivingEntity]]) {

    if(triplet.attr.relation == "enemy") {
      triplet.sendToSrc(List(triplet.dstAttr))
      triplet.sendToDst(List(triplet.srcAttr))
    }
  }
  // Deuxième fonction de l'aggregateMessages gérant la MAJ de la target
  // Reçoit les éléments 2 à 2 et renvoie le monstre le plus interessant
  def mergeTargetMsg(monsterAccum: List[LivingEntity], newMonster: List[LivingEntity]): List[LivingEntity] = {
    monsterAccum:::newMonster
  }

  // Première fonction de l'aggregateMessages gérant les damages
  def sendDamageMsg(triplet: EdgeContext[LivingEntity, Link, Int]) {

    //Vérifie que cette connexion est dans ma liste de targets (Pour ne pas attaquer tous les monstres)
    if(triplet.srcAttr.targets.map(target => target.id).contains(triplet.dstAttr.id)) {
      triplet.sendToDst(triplet.srcAttr.attackTarget(triplet.dstAttr))
    }

    if(triplet.dstAttr.targets.map(target => target.id).contains(triplet.srcAttr.id)) {
      triplet.sendToSrc(triplet.dstAttr.attackTarget(triplet.srcAttr))
    }

//    if(triplet.srcAttr.target.id == triplet.dstAttr.id) {
//      triplet.sendToDst(triplet.srcAttr.attackTarget())
//    }
//    if(triplet.dstAttr.target.id == triplet.srcAttr.id) {
//      triplet.sendToSrc(triplet.dstAttr.attackTarget())
//    }
  }

  // Deuxième fonction de l'aggregateMessages gérant les damages */
  // Reçoit les damages 2 par 2 et les ajoutes */
  def mergeDamageMsg(damage1: Int, damage2: Int): Int = {
    damage1 + damage2
  }

}