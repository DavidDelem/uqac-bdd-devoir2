package Exercice2.Combat2

import Exercice2.Bestiary._
import Exercice2.Utils.Position
import Exercice2.{Game, Link, LivingEntity}
import org.apache.spark.graphx.{Edge, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer
import Exercice2.Utils.Constants


object Combat2 extends App {

  val appname = "Devoir 2 - Exercice 2 - Combat 2. Solar vs Dragon"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")
  sc.setCheckpointDir(System.getProperty("java.io.tmpdir"))

  var i:Long = 1
  val protagonistBuffer: ArrayBuffer[(VertexId, LivingEntity)] = ArrayBuffer()
  val rnd = new scala.util.Random

  //Solar
  for (i <- 1 to 1) {
    protagonistBuffer += ((
      i,
      new Solar(
        new Position(
          Constants.goodGuyMinX + rnd.nextInt( (Constants.goodGuyMaxX  - Constants.goodGuyMinX ) + 1 ),
          Constants.goodGuyMinY + rnd.nextInt( (Constants.goodGuyMaxY  - Constants.goodGuyMinY ) + 1 )
        ),
        i.toInt
      )
    ))
  }

  //2x Planetar
  for (i <- 2 to 3) {
    protagonistBuffer += ((
      i,
      new Planetar(
        new Position(
          Constants.goodGuyMinX + rnd.nextInt( (Constants.goodGuyMaxX  - Constants.goodGuyMinX ) + 1 ),
          Constants.goodGuyMinY + rnd.nextInt( (Constants.goodGuyMaxY  - Constants.goodGuyMinY ) + 1 )
        ),
        i.toInt
      )
    ))
  }
//
  //2x Movanic Deva
  for (i <- 4 to 5) {
    protagonistBuffer += ((
      i,
      new MovanicDeva(
        new Position(
          Constants.goodGuyMinX + rnd.nextInt( (Constants.goodGuyMaxX  - Constants.goodGuyMinX ) + 1 ),
          Constants.goodGuyMinY + rnd.nextInt( (Constants.goodGuyMaxY  - Constants.goodGuyMinY ) + 1 )
        ),
        i.toInt
      )
    ))
  }
//
  //5x Astral Deva
  for (i <- 6 to 10) {
    protagonistBuffer += ((
      i,
      new AstralDeva(
        new Position(
          Constants.goodGuyMinX + rnd.nextInt( (Constants.goodGuyMaxX  - Constants.goodGuyMinX ) + 1 ),
          Constants.goodGuyMinY + rnd.nextInt( (Constants.goodGuyMaxY  - Constants.goodGuyMinY ) + 1 )
        ),
        i.toInt
      )
    ))
  }

  //Green Great Wyrm Dragon
  for (i <- 11 to 11) {
    protagonistBuffer += ((
      i,
      new GreenGreatWyrmDragon(
        new Position(
          Constants.badGuyMinX + rnd.nextInt( (Constants.badGuyMaxX  - Constants.badGuyMinX ) + 1 ),
          Constants.badGuyMinY + rnd.nextInt( (Constants.badGuyMaxY  - Constants.badGuyMinY ) + 1 )
        ),
        i.toInt
      )
    ))
  }

  //100x Orc Barbarians
  for (i <- 12 to 111) {
  protagonistBuffer += ((
      i,
      new GreataxeOrc(
        new Position(
          Constants.badGuyMinX + rnd.nextInt( (Constants.badGuyMaxX  - Constants.badGuyMinX ) + 1 ),
          Constants.badGuyMinY + rnd.nextInt( (Constants.badGuyMaxY  - Constants.badGuyMinY ) + 1 )
        ),
        i.toInt
      )
    ))
  }

  //10x Angel Slayer
  for (i <- 112 to 121) {
    protagonistBuffer += ((
      i,
      new AngelSlayer(
        new Position(
          Constants.badGuyMinX + rnd.nextInt( (Constants.badGuyMaxX  - Constants.badGuyMinX ) + 1 ),
          Constants.badGuyMinY + rnd.nextInt( (Constants.badGuyMaxY  - Constants.badGuyMinY ) + 1 )
        ),
        i.toInt
      )
    ))
  }



  val relationshipsBuffer: ArrayBuffer[Edge[Link]] = ArrayBuffer()

  //Generate relationships
  for (j <- 0 to protagonistBuffer.length-2) {
    for {k <- j+1 to protagonistBuffer.length-1} {

      if(protagonistBuffer(j) == protagonistBuffer(k)){}
      else {
        if (protagonistBuffer(j)._2.team == protagonistBuffer(k)._2.team) {
          if(protagonistBuffer(j)._2.team == "GoodGuys" || protagonistBuffer(k)._2.team == "GoodGuys") {
            relationshipsBuffer += Edge(protagonistBuffer(j)._2.id.toLong, protagonistBuffer(k)._2.id.toLong, new Link("friend"))
          } else if (protagonistBuffer(j)._2.name == "Green Great Wyrm Dragon" || protagonistBuffer(k)._2.name == "Green Great Wyrm Dragon"){
            relationshipsBuffer += Edge(protagonistBuffer(j)._2.id.toLong, protagonistBuffer(k)._2.id.toLong, new Link("friend"))
          }
        } else {
          relationshipsBuffer += Edge(protagonistBuffer(j)._2.id.toLong, protagonistBuffer(k)._2.id.toLong, new Link("enemy"))
        }
      }
    }
  }

  // Create an RDD for the vertices
  val protagonist: RDD[(VertexId, (LivingEntity))] = sc.parallelize(protagonistBuffer)
  val relationships: RDD[Edge[Link]] = sc.parallelize(relationshipsBuffer)

  val game = new Game()
  val resultsFight = game.execute(protagonist, relationships, sc, 1000)
}
