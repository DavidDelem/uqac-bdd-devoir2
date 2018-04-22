package Exercice2.Utils

import Exercice2.{Link, LivingEntity}
import org.apache.spark.graphx.Graph

object  GraphConsole{

  def printLivingEntityGraphVertices(graph : Graph[LivingEntity, Link]): Unit ={

    val printedGraph = graph.vertices.collect()

    printedGraph.foreach(
      vertex => {
        println("VERTEX:   " +
          ""+vertex._2.name + "(id = " + vertex._2.id + ", hp = "+vertex._2.hp+", x = "+BigDecimal(vertex._2.position.x).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble +", y = "+BigDecimal(vertex._2.position.y).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble+ ")  " +
          "--target(distance = "+BigDecimal(Position.distanceBetween(vertex._2.position, vertex._2.target.position)).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble+")-->  " +
          vertex._2.target.name + "(id = " + vertex._2.target.id + ", hp = "+vertex._2.target.hp+", x= "+BigDecimal(vertex._2.target.position.x).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble+", y = "+BigDecimal(vertex._2.target.position.y).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble+")")
      })
  }
}
