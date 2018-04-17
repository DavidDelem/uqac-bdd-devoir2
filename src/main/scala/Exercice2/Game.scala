package Exercice2

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeContext, Graph, TripletFields, VertexId}

class Game extends Serializable {

  def sendMsg(ctx: EdgeContext[LivingEntity, Link, LivingEntity]): Unit = {
    System.out.println("------------- sendMsg -------------")
//    System.out.println(ctx.srcAttr)
//    System.out.println(ctx.dstAttr)
//    System.out.println(ctx.attr.getRelation)

    //faire une liste de toutes les cibles possibles
    //seulement le solar attaque pour le moment
    if(ctx.attr.relation == "enemy") {
      System.out.println("enemy found")

      ctx.sendToDst(ctx.srcAttr)
      ctx.sendToDst(ctx.dstAttr)

    }



  }

  def mergeMsg(monster1: LivingEntity, monster2: LivingEntity): LivingEntity = {
    //choisir parmis toutes les cibles une à attaquer

    System.out.println("------------- mergeMsg -------------")

    (monster1.asInstanceOf[Monster]).updateTarget(monster2)
    (monster2.asInstanceOf[Monster]).updateTarget(monster1)

    monster1
  }


  def execute(g: Graph[LivingEntity, Link], sc: SparkContext, maxIterations: Int): Graph[LivingEntity, Link] = {
    var myGraph = g
    var roundCounter = 0
    val fields = new TripletFields(true, true, true)

    def gameLoop(): Unit = {
      while (true) {
        roundCounter+=1
        println("Battle round : " + roundCounter)

        val messages = myGraph.aggregateMessages[LivingEntity](
          sendMsg,
          mergeMsg,
          fields
        )

        if (messages.isEmpty()) {
          System.out.println("Did nothing")
        }

        myGraph = myGraph.joinVertices(messages) {
          System.out.println("------------- join vertices -------------")
          //faire la perte des hp
          System.out.println(messages.collect().foreach(elem => println(elem._1)))
          (vid, sommet, bestId) => sommet
        }



        //Ignorez : Code de debug
//        var printedGraph = myGraph.vertices.collect()
//        printedGraph = printedGraph.sortBy(_._1)
//        printedGraph.foreach(
//          elem => println(elem._2)
//        )

        if (roundCounter == maxIterations) return


      }

    }

    gameLoop() //execute loop
    myGraph //return the result graph
  }

  }
