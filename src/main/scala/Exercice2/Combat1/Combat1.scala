package Exercice2.Combat1

import java.lang.Math.PI

import Exercice2.Bestiary._
import Exercice2.Utils.{Constants, Position}
import Exercice2.{Game, Link, LivingEntity}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object Combat1 extends App  {

  val appname = "Devoir 2 - Exercice 2 - Combat 1. Solar vs Orcs"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")
  sc.setCheckpointDir(System.getProperty("java.io.tmpdir"))

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
      1L,
      new Solar(
        new Position(
          Constants.goodGuyCircleRadius * Math.cos(goodGuyRad),
          Constants.goodGuyCircleRadius * Math.sin(goodGuyRad)
        ),
        1
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

  for (j <- 0 to protagonistBuffer.length-2) {
    for {k <- j+1 to protagonistBuffer.length-1} {
      if(protagonistBuffer(j) == protagonistBuffer(k)){}
      else {
        if (protagonistBuffer(j)._2.team == protagonistBuffer(k)._2.team) {
        } else {
          relationshipsBuffer += Edge(protagonistBuffer(j)._2.id.toLong, protagonistBuffer(k)._2.id.toLong, new Link("enemy"))
        }
      }
    }
  }

  // Create an RDD for the vertices
  val protagonist: RDD[(VertexId, (LivingEntity))] = sc.parallelize(protagonistBuffer)
  val relationships: RDD[Edge[Link]] = sc.parallelize(relationshipsBuffer)


  // Lancement de la bataille
  val game = new Game()
  val resultsFight = game.execute(protagonist, relationships, sc, 1000)

}