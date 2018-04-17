package Exercice2

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Graph, TripletFields}

class Game {
  //def execute(g: Graph[Link, String], sc: SparkContext): Graph[Link, String] = {
  def execute(g: Graph[LivingEntity, Link], sc: SparkContext, maxIterations: Int): Graph[LivingEntity, Link] = {
    var myGraph = g
    var roundCounter = 0
    val fields = new TripletFields(true, true, true)

    def gameLoop(): Unit = {
      while (true) {
        roundCounter+=1
        println("Battle round : " + roundCounter)

        if (roundCounter == maxIterations) return




      }

    }

    gameLoop //execute loop
    myGraph //return the result graph
  }

  }
