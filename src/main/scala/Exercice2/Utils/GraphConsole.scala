package Exercice2.Utils

import Exercice2.{Link, LivingEntity, Monster}
import org.apache.spark.graphx.Graph

object  GraphConsole{

  def printGraph(graph : Graph[LivingEntity, Link]): Unit ={

    val printedGraph = graph.triplets.collect()

    printedGraph.foreach(
      triplet => {
        println("TRIPLET:    [SRC] "+triplet.srcAttr.name + "(id = " + triplet.srcAttr.id + ", hp = "+triplet.srcAttr.hp+")  --target-->  " + triplet.srcAttr.asInstanceOf[Monster].target.name + "(id = " + triplet.srcAttr.asInstanceOf[Monster].target.id + ", hp = "+triplet.srcAttr.asInstanceOf[Monster].target.hp+")" +
          "    [DST] "+triplet.dstAttr.name + "(id = " + triplet.dstAttr.id + ", hp = "+triplet.dstAttr.hp+")  --target-->  " + triplet.dstAttr.asInstanceOf[Monster].target.name + "(id = " + triplet.dstAttr.asInstanceOf[Monster].target.id + ", hp = "+triplet.dstAttr.asInstanceOf[Monster].target.hp+")"
        )
      })
  }
}
