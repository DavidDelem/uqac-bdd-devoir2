package Exercice2

import Exercice2.Bestiary.{DoubleAxeFury, OrcWorgRider, Solar, _}
import Exercice2.Utils.Position
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeContext, Graph, TripletFields, VertexId}

class Game extends Serializable {

  def sendMsg(triplet: EdgeContext[LivingEntity, Link, LivingEntity]): Unit = {
    //System.out.println("------------- sendMsg -------------")
//    System.out.println(ctx.srcAttr)
//    System.out.println(ctx.dstAttr)
//    System.out.println(ctx.attr.getRelation)

    //faire une liste de toutes les cibles possibles
    //seulement le solar attaque pour le moment
    if(triplet.attr.relation == "enemy") {
      //System.out.println("enemy found")

      triplet.sendToSrc(triplet.dstAttr)
      triplet.sendToDst(triplet.srcAttr)

    }



  }

  def mergeMsg(monster1: LivingEntity, monster2: LivingEntity): LivingEntity = {
    //choisir parmis toutes les cibles une à attaquer

    //System.out.println("------------- mergeMsg ------------- monster 1 = "+ monster1.asInstanceOf[Monster].name + " ; monster 2 = " + monster2.asInstanceOf[Monster].name)
    if(monster1.asInstanceOf[Monster].hp <= monster2.asInstanceOf[Monster].hp) monster1
    else monster2

//    monster1.asInstanceOf[Monster].updateTarget(monster2)
//    monster2.asInstanceOf[Monster].updateTarget(monster1)

//    monster1
  }


  def execute(g: Graph[LivingEntity, Link], sc: SparkContext, maxIterations: Int): Graph[LivingEntity, Link] = {
    var myGraph = g
    var roundCounter = 0
    val fields = new TripletFields(true, true, true) //join strategy

    def gameLoop(): Unit = {
      while (true) {
        roundCounter+=1
        println("================ Battle round : " + roundCounter + " ================")

//        val messages = myGraph.aggregateMessages[LivingEntity](
//          sendMsg,
//          mergeMsg,
//          fields
//        )
//
//        if (messages.isEmpty()) {
//          System.out.println("Did nothing")
//        }
//
//        myGraph = myGraph.joinVertices(messages) {
//          System.out.println("------------- join vertices -------------")
//          //faire la perte des hp
////          System.out.println(messages.collect().foreach(elem =>{
////            println(elem._1 + "  --  " +elem._2)
////          }))
//          (vid, fighter, target) => {
//            println("TEST   ----  Fighter = "+fighter.asInstanceOf[Monster].name + "("+fighter.asInstanceOf[Monster].hp + ")   ---> target = "+target.asInstanceOf[Monster].name)
//            fighter.asInstanceOf[Monster].updateTarget(target)
//            fighter
//          }
//        }

        //--------------------
        // TARGET UPDATE
        //--------------------

        val targetMessages = myGraph.aggregateMessages[LivingEntity](
          triplet => {
            if(triplet.attr.relation == "enemy") {
              triplet.sendToSrc(triplet.dstAttr)
              triplet.sendToDst(triplet.srcAttr)
            }
          },
          (monster1, monster2) =>{
            if(monster1.asInstanceOf[Monster].hp <= monster2.asInstanceOf[Monster].hp && monster1.asInstanceOf[Monster].hp > 0) monster1
            else monster2
          }
        )

        targetMessages.collect()

        myGraph = myGraph.joinVertices(targetMessages) {

          (vid, fighter, target) => {
            //fighter.asInstanceOf[Monster].updateTarget(target)
            //println("TEST 1  --  "+ fighter.name + " HP = " + fighter.hp)
            //fighter.hp -= 1
            //println(fighter.name + "  -->  " + fighter.asInstanceOf[Monster].target.name)


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
          triplet => {
//            println("SRC  --  "+triplet.srcAttr.name + "(" + triplet.srcAttr.hp + ")  --> " + triplet.srcAttr.asInstanceOf[Monster].target.name)
//            println("DST  --  "+triplet.dstAttr.name + "(" + triplet.dstAttr.hp + ")  --> " + triplet.dstAttr.asInstanceOf[Monster].target.name)
            if(triplet.srcAttr.asInstanceOf[Monster].target.id == triplet.dstAttr.id) {
              triplet.sendToDst(triplet.srcAttr.asInstanceOf[Monster].attackTarget())
            }
            if(triplet.dstAttr.asInstanceOf[Monster].target.id == triplet.srcAttr.id) {
              triplet.sendToSrc(triplet.dstAttr.asInstanceOf[Monster].attackTarget())
            }
          },
          (damage1, damage2) => damage1 + damage2
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

//        var printedGraph = myGraph.triplets.collect()
//          printedGraph.foreach(
//            triplet => {
//              println("SRC  --  "+triplet.srcAttr.name + "(" + triplet.srcAttr.hp + ")  --> " + triplet.srcAttr.asInstanceOf[Monster].target.name)
//              println("DST  --  "+triplet.dstAttr.name + "(" + triplet.dstAttr.hp + ")  --> " + triplet.dstAttr.asInstanceOf[Monster].target.name)
//        })

        val nbBadGuysAlive = myGraph.vertices.filter{ vertex => {
          if(vertex._2.team == "BadGuys" && vertex._2.hp > 0) println("vertex._2.name = " + vertex._2.name)
          vertex._2.team == "BadGuys" && vertex._2.hp > 0
        }}.count
        val nbGoodGuysAlive = myGraph.vertices.filter{ vertex =>  vertex._2.team == "GoodGuys" && vertex._2.hp > 0}.count

        println("nbBadGuysAlive = " + nbBadGuysAlive)
        println("nbGoodGuysAlive = " + nbGoodGuysAlive)

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
