package Exercice2.Combat1

import Exercice2.Bestiary._
import Exercice2.Utils.Position
import Exercice2.{Link, LivingEntity}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Combat1 extends App  {

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
      (1L, new Solar(new Position(0, 0), 1)),
      (3L, new OrcWorgRider(new Position(0, 50), 3)),
      (4L, new OrcWorgRider(new Position(5, 50), 4)),
      (5L, new OrcWorgRider(new Position(10, 50), 5)),
      (6L, new OrcWorgRider(new Position(15, 50), 6)),
      (7L, new OrcWorgRider(new Position(20, 50), 7)),
      (8L, new OrcWorgRider(new Position(25, 50), 8)),
      (9L, new OrcWorgRider(new Position(30, 50), 9)),
      (10L, new OrcWorgRider(new Position(-5, 50), 10)),
      (11L, new OrcWorgRider(new Position(-10, 50), 11)),
      (12L, new BrutalWarlord(new Position(-15, 50), 12)),
      (13L, new DoubleAxeFury(new Position(-20, 50), 13)),
      (14L, new DoubleAxeFury(new Position(0, 50), 14)),
      (15L, new DoubleAxeFury(new Position(-5, 50), 15)),
      (16L, new DoubleAxeFury(new Position(5, 50), 16))

    ))

  val relationships: RDD[Edge[Link]] =
    sc.parallelize(Array(
      Edge(1L, 3L, new Link("enemy")),
      Edge(1L, 4L, new Link("enemy")),
      Edge(1L, 5L, new Link("enemy")),
      Edge(1L, 6L, new Link("enemy")),
      Edge(1L, 7L, new Link("enemy")),
      Edge(1L, 8L, new Link("enemy")),
      Edge(1L, 9L, new Link("enemy")),
      Edge(1L, 10L, new Link("enemy")),
      Edge(1L, 11L, new Link("enemy")),
      Edge(1L, 12L, new Link("enemy")),
      Edge(1L, 13L, new Link("enemy")),
      Edge(1L, 14L, new Link("enemy")),
      Edge(1L, 15L, new Link("enemy")),
      Edge(1L, 16L, new Link("enemy"))
    ))

  // Build the initial Graph
  val graph = Graph(protagonist, relationships)

  val game = new Game()
  val resultsFight = game.execute(graph, sc, 100)

}