package Exercice2.Combat1

import Exercice2.Bestiary.{DoubleAxeFury, OrcWorgRider, Solar, _}
import Exercice2.Utils.GraphConsole
import Exercice2.{Link, LivingEntity, Monster}
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

    if(monster1.asInstanceOf[Monster].hp <= monster2.asInstanceOf[Monster].hp && monster1.asInstanceOf[Monster].hp > 0) monster1
    else monster2
  }

  def sendDamageMsg(triplet: EdgeContext[LivingEntity, Link, Int]) {

    if(triplet.srcAttr.asInstanceOf[Monster].target.id == triplet.dstAttr.id) {
      triplet.sendToDst(triplet.srcAttr.asInstanceOf[Monster].attackTarget())
    }
    if(triplet.dstAttr.asInstanceOf[Monster].target.id == triplet.srcAttr.id) {
      triplet.sendToSrc(triplet.dstAttr.asInstanceOf[Monster].attackTarget())
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

          (vid, fighter, target) => {

            fighter match {
              case brutalWarlord: BrutalWarlord =>{
                var newBrutalWarlord = new BrutalWarlord(brutalWarlord.position, brutalWarlord.id)
                newBrutalWarlord.target = target
                newBrutalWarlord.hp = fighter.hp
                newBrutalWarlord
              }
              case doubleAxeFury: DoubleAxeFury =>{
                var newDoubleAxeFury = new DoubleAxeFury(doubleAxeFury.position, doubleAxeFury.id)
                newDoubleAxeFury.target = target
                newDoubleAxeFury.hp = fighter.hp
                newDoubleAxeFury
              }
              case orcWorgRider: OrcWorgRider =>{
                var newOrcWorgRider = new OrcWorgRider(orcWorgRider.position, orcWorgRider.id)
                newOrcWorgRider.target = target
                newOrcWorgRider.hp = fighter.hp
                newOrcWorgRider
              }
              case solar: Solar =>{
                var newSolar = new Solar(solar.position, solar.id)
                newSolar.target = target
                newSolar.hp = fighter.hp
                newSolar
              }
              case _ => {
                fighter
              }
            }

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

          (vid, damageReceiver, damages) => {

            damageReceiver match {
              case brutalWarlord: BrutalWarlord =>{
                var newBrutalWarlord = new BrutalWarlord(brutalWarlord.position, brutalWarlord.id)
                newBrutalWarlord.target = brutalWarlord.target
                newBrutalWarlord.hp = brutalWarlord.hp
                newBrutalWarlord.takeDamage(damages)
                newBrutalWarlord
              }
              case doubleAxeFury: DoubleAxeFury =>{
                var newDoubleAxeFury = new DoubleAxeFury(doubleAxeFury.position, doubleAxeFury.id)
                newDoubleAxeFury.target = doubleAxeFury.target
                newDoubleAxeFury.hp = doubleAxeFury.hp
                newDoubleAxeFury.takeDamage(damages)
                newDoubleAxeFury
              }
              case orcWorgRider: OrcWorgRider =>{
                var newOrcWorgRider = new OrcWorgRider(orcWorgRider.position, orcWorgRider.id)
                newOrcWorgRider.target = orcWorgRider.target
                newOrcWorgRider.hp = orcWorgRider.hp
                newOrcWorgRider.takeDamage(damages)
                newOrcWorgRider
              }
              case solar: Solar =>{
                var newSolar = new Solar(solar.position, solar.id)
                newSolar.target = solar.target
                newSolar.hp = solar.hp
                newSolar.takeDamage(damages)
                newSolar
              }
              case _ => {
                damageReceiver
              }
            }

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
