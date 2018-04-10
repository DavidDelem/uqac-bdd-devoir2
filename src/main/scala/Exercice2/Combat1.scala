package Exercice2

import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Combat1 extends App {

  val appname = "Devoir 2 - Exercice 2 - Combat 1. Solar vs Éclaireurs Orcs"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")

  val sol = new Solar()
  print(sol)


  // Create an RDD for the vertices
  val users: RDD[(VertexId, (Monster))] =
    sc.parallelize(Array(
      (1L, new Solar())
    ))
//
//  // Create an RDD for edges
//  val relationships: RDD[Edge[String]] =
//    sc.parallelize(Array(
//      Edge(3L, 7L, "collab"),
//      Edge(5L, 3L, "advisor"),
//      Edge(2L, 5L, "colleague"),
//      Edge(5L, 7L, "pi")
//    ))
//  // Define a default user in case there are relationship with missing user
//  val defaultUser = ("John Doe", "Missing")
//
//  // Build the initial Graph
//  val graph = Graph(users, relationships, defaultUser)
//
//  val facts: RDD[String] =
//    graph.triplets.map(triplet =>
//      triplet.srcAttr._1 + " is the " + triplet.attr + " of " + triplet.dstAttr._1)
//  facts.collect.foreach(println(_))

}