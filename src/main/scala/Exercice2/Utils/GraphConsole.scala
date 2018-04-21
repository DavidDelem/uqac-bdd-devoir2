package Exercice2.Utils

import Exercice2.{Link, LivingEntity}
import org.apache.spark.graphx.Graph

object  GraphConsole{

  def printGraph(graph : Graph[LivingEntity, Link]): Unit ={

    val printedGraph = graph.vertices.collect()

    printedGraph.foreach(
      vertex => {
        println("VERTEX:   "+vertex._2.name + "(id = " + vertex._2.id + ", hp = "+vertex._2.hp+")  --target-->  " + vertex._2.target.name + "(id = " + vertex._2.target.id + ", hp = "+vertex._2.target.hp+")")
      })
  }
}
