package Exercice2.Combat1

import java.lang.Math.PI

import Exercice2.Bestiary._
import Exercice2.Utils.{Constants, Position}
import Exercice2.{Link, LivingEntity}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object Combat1 extends App  {

  val appname = "Devoir 2 - Exercice 2 - Combat 1. Solar vs Éclaireurs Orcs"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")

  // https://spark.apache.org/docs/2.1.1/graphx-programming-guide.html
  // https://gist.github.com/mitchi/c78a74685edf6a74813b808acf0906b5

  var i:Long = 1
  val protagonistBuffer: ArrayBuffer[(VertexId, LivingEntity)] = ArrayBuffer()

  //We put the good guy in circle around the village
  //Then we put the bad guy in circle around them

  val goodGuyNumber = 1.0
  val badGuyNumber = 14.0
  var goodGuyInterval:Double = 360.0/goodGuyNumber
  var badGuyInterval:Double = 360.0/badGuyNumber

  var goodGuyAngle:Double = 0.0
  var badGuyAngle:Double = 0.0

  var goodGuyRad:Double = goodGuyAngle * (PI / 180.0)
  var badGuyRad:Double = badGuyAngle* (PI / 180.0)

  //Solar
  for (i <- 1 to 1) {
    protagonistBuffer += ((
      i,
      new Solar(
        new Position(
          Constants.goodGuyCircleRadius * Math.cos(goodGuyRad),
          Constants.goodGuyCircleRadius * Math.sin(goodGuyRad)
        ),
        i.toInt
      )
    ))

    goodGuyAngle += goodGuyInterval
    goodGuyRad = goodGuyAngle * (PI / 180.0)

  }

  //9x worgs rider
  for (i <- 2 to 10) {
    protagonistBuffer += ((
      i,
      new OrcWorgRider(
        new Position(
          Constants.badGuyCircleRadius * Math.cos(badGuyRad),
          Constants.badGuyCircleRadius * Math.sin(badGuyRad)
        ),
        i.toInt
      )
    ))

    badGuyAngle += badGuyInterval
    badGuyRad = badGuyAngle * (PI / 180.0)
  }

  //1x warlord
  for (i <- 11 to 11) {
    protagonistBuffer += ((
      i,
      new BrutalWarlord(
        new Position(
          Constants.badGuyCircleRadius * Math.cos(badGuyRad),
          Constants.badGuyCircleRadius * Math.sin(badGuyRad)
        ),
        i.toInt
      )
    ))

    badGuyAngle += badGuyInterval
    badGuyRad = badGuyAngle * (PI / 180.0)
  }

  //4x barbars orc
  for (i <- 12 to 15) {
    protagonistBuffer += ((
      i,
      new DoubleAxeFury(
        new Position(
          Constants.badGuyCircleRadius * Math.cos(badGuyRad),
          Constants.badGuyCircleRadius * Math.sin(badGuyRad)
        ),
        i.toInt
      )
    ))

    badGuyAngle += badGuyInterval
    badGuyRad = badGuyAngle * (PI / 180.0)
  }



  val relationshipsBuffer: ArrayBuffer[Edge[Link]] = ArrayBuffer()

  for (protagonist1 <- protagonistBuffer) {
    for (protagonist2 <- protagonistBuffer) {
      if(protagonist1 == protagonist2){}
      else {
        if (protagonist1._2.team == protagonist2._2.team) {
        } else {
          relationshipsBuffer += Edge(protagonist1._2.id.toLong, protagonist2._2.id.toLong, new Link("enemy"))
        }
      }
    }
  }

  // Create an RDD for the vertices
  val protagonist: RDD[(VertexId, (LivingEntity))] = sc.parallelize(protagonistBuffer)

  val relationships: RDD[Edge[Link]] = sc.parallelize(relationshipsBuffer)


//  // Création du RDD pour les vertices
//  val protagonist: RDD[(VertexId, (LivingEntity))] =
//    sc.parallelize(Array(
//      (1L, new Solar(new Position(0, 0), 1)),
//      (3L, new OrcWorgRider(new Position(150, 0), 3)),
//      (4L, new OrcWorgRider(new Position(140, 10), 4)),
//      (5L, new OrcWorgRider(new Position(130, 20), 5)),
//      (6L, new OrcWorgRider(new Position(120, 30), 6)),
//      (7L, new OrcWorgRider(new Position(110, 40), 7)),
//      (8L, new OrcWorgRider(new Position(100, 50), 8)),
//      (9L, new OrcWorgRider(new Position(90, 60), 9)),
//      (10L, new OrcWorgRider(new Position(80, 70), 10)),
//      (11L, new OrcWorgRider(new Position(70, 80), 11)),
//      (12L, new BrutalWarlord(new Position(60, 90), 12)),
//      (13L, new DoubleAxeFury(new Position(50, 100), 13)),
//      (14L, new DoubleAxeFury(new Position(40, 110), 14)),
//      (15L, new DoubleAxeFury(new Position(30, 120), 15)),
//      (16L, new DoubleAxeFury(new Position(20, 130), 16))
//
//    ))
//
//  // Création du RDD pour les edges
//  val relationships: RDD[Edge[Link]] =
//    sc.parallelize(Array(
//      Edge(1L, 3L, new Link("enemy")),
//      Edge(1L, 4L, new Link("enemy")),
//      Edge(1L, 5L, new Link("enemy")),
//      Edge(1L, 6L, new Link("enemy")),
//      Edge(1L, 7L, new Link("enemy")),
//      Edge(1L, 8L, new Link("enemy")),
//      Edge(1L, 9L, new Link("enemy")),
//      Edge(1L, 10L, new Link("enemy")),
//      Edge(1L, 11L, new Link("enemy")),
//      Edge(1L, 12L, new Link("enemy")),
//      Edge(1L, 13L, new Link("enemy")),
//      Edge(1L, 14L, new Link("enemy")),
//      Edge(1L, 15L, new Link("enemy")),
//      Edge(1L, 16L, new Link("enemy"))
//    ))

  // Construction du graphe initial
  val graph = Graph(protagonist, relationships)

  // Lancement de la bataille
  val game = new Game()
  val resultsFight = game.execute(graph, sc, 100)

}