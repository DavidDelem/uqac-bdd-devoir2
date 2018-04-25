package Exercice2.Combat2

import java.io.File
import java.lang.Math._

import Exercice2.Bestiary._
import Exercice2.Utils.Position
import Exercice2.{Link, LivingEntity}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer
import Exercice2.Utils.Constants


object Combat2 extends App {

  val appname = "Devoir 2 - Exercice 2 - Combat 1. Solar vs Éclaireurs Orcs"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")

  var i:Long = 1
  val protagonistBuffer: ArrayBuffer[(VertexId, LivingEntity)] = ArrayBuffer()

  //We put the good guy in circle around the village
  //Then we put the bad guy in circle around them

  val goodGuyNumber = 10.0
  val badGuyNumber = 211.0
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

  //2x Planetar
  for (i <- 2 to 3) {
    protagonistBuffer += ((
      i,
      new Planetar(
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

  //2x Movanic Deva
  for (i <- 4 to 5) {
    protagonistBuffer += ((
      i,
      new MovanicDeva(
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

  //5x Astral Deva
  for (i <- 6 to 10) {
    protagonistBuffer += ((
      i,
      new AstralDeva(
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

  //Green Great Wyrm Dragon
  for (i <- 11 to 11) {
    protagonistBuffer += ((
      i,
      new GreenGreatWyrmDragon(
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

  //200x Orc Barbarians
  for (i <- 12 to 211) {
    protagonistBuffer += ((
      i,
      new GreataxeOrc(
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

  //10x Angel Slayer
  for (i <- 212 to 221) {
    protagonistBuffer += ((
      i,
      new AngelSlayer(
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
          if(protagonist1._2.team == "GoodGuys" || protagonist2._2.team == "GoodGuys") {
            relationshipsBuffer += Edge(protagonist1._2.id.toLong, protagonist2._2.id.toLong, new Link("friend"))
          } else if (protagonist1._2.name == "Green Great Wyrm Dragon" || protagonist2._2.name == "Green Great Wyrm Dragon"){
            relationshipsBuffer += Edge(protagonist1._2.id.toLong, protagonist2._2.id.toLong, new Link("friend"))
          }
        } else {
          relationshipsBuffer += Edge(protagonist1._2.id.toLong, protagonist2._2.id.toLong, new Link("enemy"))
        }
      }
    }
  }

  // Create an RDD for the vertices
  val protagonist: RDD[(VertexId, (LivingEntity))] = sc.parallelize(protagonistBuffer)

  val relationships: RDD[Edge[Link]] = sc.parallelize(relationshipsBuffer)

  // Build the initial Graph
  val graph = Graph(protagonist, relationships)

  val game = new Game()
  val resultsFight = game.execute(graph, sc, 100)
}
