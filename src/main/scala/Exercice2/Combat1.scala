package Exercice2

import Exercice2.Bestiary._
import Exercice2.Utils.Position
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
  //https://gist.github.com/mitchi/c78a74685edf6a74813b808acf0906b5
  // Create an RDD for the vertices
  val protagonist: RDD[(VertexId, (LivingEntity))] =
    sc.parallelize(Array(
      (1L, new Solar(new Position(0, 0))),
      (2L, new Pito(new Position(-10, 0))),
      (3L, new OrcWorgRider(new Position(0, 200))),
      (4L, new OrcWorgRider(new Position(5, 200))),
      (5L, new OrcWorgRider(new Position(10, 200))),
      (6L, new OrcWorgRider(new Position(15, 200))),
      (7L, new OrcWorgRider(new Position(20, 200))),
      (8L, new OrcWorgRider(new Position(25, 200))),
      (9L, new OrcWorgRider(new Position(30, 200))),
      (10L, new OrcWorgRider(new Position(-5, 200))),
      (11L, new OrcWorgRider(new Position(-10, 200))),
      (12L, new BrutalWarlord(new Position(-15, 200))),
      (13L, new DoubleAxeFury(new Position(-20, 200))),
      (14L, new DoubleAxeFury(new Position(0, 205))),
      (15L, new DoubleAxeFury(new Position(-5, 205))),
      (16L, new DoubleAxeFury(new Position(5, 205)))

    ))
//
  val relationships: RDD[Edge[Link]] =
    sc.parallelize(Array(
      Edge(1L, 2L, new Link("friend")),
      Edge(1L, 12L, new Link("enemy")),
      Edge(1L, 4L, new Link("enemy")),
      Edge(1L, 16L, new Link("enemy"))
////      Edge(1L, 6L, new Link("enemy", 200)),
////      Edge(1L, 7L, new Link("enemy", 200)),
////      Edge(1L, 8L, new Link("enemy", 200)),
////      Edge(1L, 9L, new Link("enemy", 200)),
////      Edge(1L, 10L, new Link("enemy", 200)),
////      Edge(1L, 11L, new Link("enemy", 200)),
////      Edge(1L, 12L, new Link("enemy", 200)),
////      Edge(1L, 13L, new Link("enemy", 200)),
////      Edge(1L, 14L, new Link("enemy", 200)),
////      Edge(1L, 15L, new Link("enemy", 200)),
////      Edge(1L, 16L, new Link("enemy", 200))
////      Edge(3L, 1L, new Link("enemy", 200)),
////      Edge(4L, 1L, new Link("enemy", 200)),
////      Edge(5L, 1L, new Link("enemy", 200)),
////      Edge(6L, 1L, new Link("enemy", 200)),
////      Edge(7L, 1L, new Link("enemy", 200)),
////      Edge(8L, 1L, new Link("enemy", 200)),
////      Edge(9L, 1L, new Link("enemy", 200)),
////      Edge(10L, 1L, new Link("enemy", 200)),
////      Edge(11L, 1L, new Link("enemy", 200)),
////      Edge(12L, 1L, new Link("enemy", 200)),
////      Edge(13L, 1L, new Link("enemy", 200)),
////      Edge(14L, 1L, new Link("enemy", 200)),
////      Edge(15L, 1L, new Link("enemy", 200)),
////      Edge(16L, 1L, new Link("enemy", 200))
    ))

  // Build the initial Graph
  val graph = Graph(protagonist, relationships)

//  val facts: RDD[String] =
//    graph.triplets.map(triplet =>
//      triplet.srcAttr.getName + " is the " + triplet.attr.getRelation + " of " + triplet.dstAttr.getName)
//  facts.collect.foreach(println(_))

  val algoFight = new Game()
  val resultsFight = algoFight.execute(graph, sc, 1)

}