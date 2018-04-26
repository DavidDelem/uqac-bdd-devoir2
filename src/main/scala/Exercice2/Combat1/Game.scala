package Exercice2.Combat1

import Exercice2.{Link, LivingEntity, LivingEntityPrototype}
import Exercice2.Utils.{GraphConsole, Position}
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
      .open("ws://localhost:8089/fight")
      .listener(new TextListener {
        override def onOpen(){ println("WSClient connected") }
        override def onClose(){ println("WSClient disconnected") }
        override def onMessage(message: String): Unit = {println("Message = " + message)}
        override def onError(t: Throwable): Unit = {println("Error = " + t)}
      })

    webSocketClient.send("FightBeginning");

    def gameLoop(): Unit = {

      while (true) {

        roundCounter+=1
        println("================ Battle round : " + roundCounter + " ================")

        //--------------------------------------
        // CHECKPOINT + CLEAR ALL DEAD ENTITIES
        //--------------------------------------

        if(roundCounter%10==0){
          myGraph = myGraph.subgraph(vpred = (id, attr) =>  attr.hp > 0)
          myGraph.checkpoint()
        }

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

        val targetMessages = myGraph.aggregateMessages[(LivingEntity, Position)](
          sendTargetMsg,
          mergeTargetMsg,
          fields
        )
        //targetMessages.collect()

        myGraph = myGraph.joinVertices(targetMessages) {

          (_, fighter, tupleTarget) => {

            val newFighter = LivingEntityPrototype.create(fighter)
            newFighter.target = tupleTarget._1
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

        //damageMessages.collect()

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
          vertex._2.target,
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

        // Conditions d'arrêt: victoire des ennemis ou des alliés
        if(nbBadGuysAlive == 0){
          println("END OF LOOP : Solar successfully saved Pito :D")
          return
        }
        else if(nbGoodGuysAlive == 0){
          println("END OF LOOP : Unfortunatly, Solar and Pito died! Bad guys won :(")
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
  def sendTargetMsg(triplet: EdgeContext[LivingEntity, Link, (LivingEntity, Position)]) {

    if(triplet.attr.relation == "enemy") {
      triplet.sendToSrc((triplet.dstAttr, triplet.srcAttr.position))
      triplet.sendToDst((triplet.srcAttr, triplet.dstAttr.position))
    }
  }
  // Deuxième fonction de l'aggregateMessages gérant la MAJ de la target
  // Reçoit les éléments 2 à 2 et renvoie le monstre le plus interessant
  def mergeTargetMsg(tupleMonster1: (LivingEntity, Position), tupleMonster2: (LivingEntity, Position)): (LivingEntity, Position) = {

    val distanceToMonster1 = Position.distanceBetween(tupleMonster1._1.position, tupleMonster1._2)
    val distanceToMonster2 = Position.distanceBetween(tupleMonster2._1.position, tupleMonster2._2)

    //When the 2 monsters are dead
    if(tupleMonster1._1.hp == 0 && tupleMonster2._1.hp == 0) return tupleMonster1
    //When monster2 is dead
    if(tupleMonster1._1.hp > 0 && tupleMonster2._1.hp == 0) tupleMonster1
    //When monster1 is dead
    else if(tupleMonster2._1.hp > 0 && tupleMonster1._1.hp == 0) tupleMonster2
    //When monster1 is closer than monster2
    else if(distanceToMonster1 <= distanceToMonster2) tupleMonster1
    //When monster2 is closer than monster1
    else tupleMonster2
  }

  // Première fonction de l'aggregateMessages gérant les damages
  def sendDamageMsg(triplet: EdgeContext[LivingEntity, Link, Int]) {

    if(triplet.srcAttr.target.id == triplet.dstAttr.id) {
      triplet.sendToDst(triplet.srcAttr.attackTarget())
    }
    if(triplet.dstAttr.target.id == triplet.srcAttr.id) {
      triplet.sendToSrc(triplet.dstAttr.attackTarget())
    }
  }

  // Deuxième fonction de l'aggregateMessages gérant les damages */
  // Reçoit les damages 2 par 2 et les ajoutes */
  def mergeDamageMsg(damage1: Int, damage2: Int): Int = {
    damage1 + damage2
  }

}
