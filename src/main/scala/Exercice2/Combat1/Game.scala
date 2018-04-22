package Exercice2.Combat1

import Exercice2.{Link, LivingEntity, LivingEntityPrototype}
import Exercice2.Utils.{Constants, GraphConsole}
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeContext, Graph, TripletFields}

class Game extends Serializable {

  //--------------------
  // EXECUTE GAME
  //--------------------

  def execute(g: Graph[LivingEntity, Link], sc: SparkContext, maxIterations: Int): Graph[LivingEntity, Link] = {

    var myGraph = g
    var roundCounter = 0
    val fields = new TripletFields(true, true, true) //join strategy

    def gameLoop(): Unit = {

      while (true) {

        roundCounter+=1
        println("================ Battle round : " + roundCounter + " ================")


        //--------------------
        // MOVING + REGENERATE UPDATE
        //--------------------

        val newVerticesMove = myGraph.vertices.map(vertex => {
          vertex._2.regenerate()
          vertex._2.move()
          vertex
        })
        myGraph = Graph(newVerticesMove, myGraph.edges)

        //--------------------
        // TARGET UPDATE
        //--------------------

        val targetMessages = myGraph.aggregateMessages[LivingEntity](
          sendTargetMsg,
          mergeTargetMsg,
          fields
        )
        targetMessages.collect()

        myGraph = myGraph.joinVertices(targetMessages) {

          (_, fighter, target) => {

            val newFighter = LivingEntityPrototype.create(fighter)
            newFighter.target = target
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

        damageMessages.collect()

        myGraph = myGraph.joinVertices(damageMessages) {

          (_, damageReceiver, damages) => {

            val newDamageReceiver = LivingEntityPrototype.create(damageReceiver)
            newDamageReceiver.takeDamage(damages)
            newDamageReceiver
          }
        }

        // Affichage graphique
        GraphConsole.printLivingEntityGraphVertices(myGraph)

        // Récupération du nombre d'alliés et ennemis toujours en vie
        val nbBadGuysAlive = myGraph.vertices.filter{ vertex => {vertex._2.team == "BadGuys" && vertex._2.hp > 0}}.count
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

        Thread.sleep(Constants.sleepMilli)
      }

    }

    gameLoop() //execute loop
    myGraph //return the result graph
  }


  //------------------------------------------------------------------------------
  //-------------- FONCTION UTILISEES DANS LES AGGREGATEMESSAGES  ----------------
  //------------------------------------------------------------------------------

  // Première fonction de l'aggregateMessages gérant la MAJ de la target
  // On regarde si ils sont enemis et si oui on complete le triplet
  def sendTargetMsg(triplet: EdgeContext[LivingEntity, Link, LivingEntity]) {

    if(triplet.attr.relation == "enemy") {
      triplet.sendToSrc(triplet.dstAttr)
      triplet.sendToDst(triplet.srcAttr)
    }
  }
  // Deuxième fonction de l'aggregateMessages gérant la MAJ de la target
  // Reçoit les éléments 2 à 2 et renvoie le monstre le plus interessant
  def mergeTargetMsg(monster1: LivingEntity, monster2: LivingEntity): LivingEntity = {

    if(monster1.hp <= monster2.hp && monster1.hp > 0) monster1
    else monster2
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
