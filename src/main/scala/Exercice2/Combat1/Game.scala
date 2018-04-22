package Exercice2.Combat1

import Exercice2.Bestiary.{DoubleAxeFury, OrcWorgRider, Solar, _}
import Exercice2.{Link, LivingEntity, LivingEntityPrototype}
import Exercice2.Utils.GraphConsole
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeContext, Graph, TripletFields}

class Game extends Serializable {

  def sendTargetMsg(triplet: EdgeContext[LivingEntity, Link, LivingEntity]) {

    if(triplet.attr.relation == "enemy") {
      triplet.sendToSrc(triplet.dstAttr)
      triplet.sendToDst(triplet.srcAttr)
    }
  }

  def mergeTargetMsg(monster1: LivingEntity, monster2: LivingEntity): LivingEntity = {

    if(monster1.hp <= monster2.hp && monster1.hp > 0) monster1
    else monster2
  }

  def sendDamageMsg(triplet: EdgeContext[LivingEntity, Link, Int]) {

    if(triplet.srcAttr.target.id == triplet.dstAttr.id) {
      triplet.sendToDst(triplet.srcAttr.attackTarget())
    }
    if(triplet.dstAttr.target.id == triplet.srcAttr.id) {
      triplet.sendToSrc(triplet.dstAttr.attackTarget())
    }
  }

  def mergeDamageMsg(damage1: Int, damage2: Int): Int = {
    damage1 + damage2
}

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

        GraphConsole.printGraph(myGraph)

        val nbBadGuysAlive = myGraph.vertices.filter{ vertex => {vertex._2.team == "BadGuys" && vertex._2.hp > 0}}.count
        val nbGoodGuysAlive = myGraph.vertices.filter{ vertex =>  vertex._2.team == "GoodGuys" && vertex._2.hp > 0}.count

        // Break loop condition
        if(nbBadGuysAlive == 0){
          println("END OF LOOP : Solar successfully saved Pito")
          return
        }
        else if(nbGoodGuysAlive == 0){
          println("END OF LOOP : Unfortunatly, Solar and Pito died! Bad guys won")
          return
        }
        else if (roundCounter == maxIterations) return


      }

    }

    gameLoop() //execute loop
    myGraph //return the result graph
  }

}
