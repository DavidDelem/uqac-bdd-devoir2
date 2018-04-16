package Exercice2

import Exercice2.Bestiary._
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Combat1 extends App {

  val appname = "Devoir 2 - Exercice 2 - Combat 1. Solar vs Éclaireurs Orcs"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")


  //https://spark.apache.org/docs/2.1.1/graphx-programming-guide.html
  // Create an RDD for the vertices
  val protagonist: RDD[(VertexId, (LivingEntity))] =
    sc.parallelize(Array(
      (1L, new Solar()),
      (2L, new Pito()),
      (3L, new OrcWorgRider()),
      (4L, new OrcWorgRider()),
      (5L, new OrcWorgRider()),
      (6L, new OrcWorgRider()),
      (7L, new OrcWorgRider()),
      (8L, new OrcWorgRider()),
      (9L, new OrcWorgRider()),
      (10L, new OrcWorgRider()),
      (11L, new OrcWorgRider()),
      (12L, new BrutalWarlord()),
      (13L, new DoubleAxeFury()),
      (14L, new DoubleAxeFury()),
      (15L, new DoubleAxeFury()),
      (16L, new DoubleAxeFury())

    ))

  val relationships: RDD[Edge[String]] =
    sc.parallelize(Array(
      Edge(1L, 3L, "enemy"),
      Edge(1L, 4L, "enemy"),
      Edge(1L, 5L, "enemy"),
      Edge(1L, 6L, "enemy"),
      Edge(1L, 7L, "enemy"),
      Edge(1L, 8L, "enemy"),
      Edge(1L, 9L, "enemy"),
      Edge(1L, 10L, "enemy"),
      Edge(1L, 11L, "enemy"),
      Edge(1L, 12L, "enemy"),
      Edge(1L, 13L, "enemy"),
      Edge(1L, 14L, "enemy"),
      Edge(1L, 15L, "enemy"),
      Edge(1L, 16L, "enemy")
    ))

  // Build the initial Graph
  val graph = Graph(protagonist, relationships)

  val facts: RDD[String] =
    graph.triplets.map(triplet =>
      triplet.srcAttr.getName() + " is the " + triplet.attr + " of " + triplet.dstAttr.getName())
  facts.collect.foreach(println(_))

}