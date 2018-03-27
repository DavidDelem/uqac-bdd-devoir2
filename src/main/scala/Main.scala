import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import net.liftweb.json.DefaultFormats
import net.liftweb.json._

import scala.collection.mutable.ListBuffer

object Main extends App {
  implicit val formats: DefaultFormats.type = DefaultFormats

  val appname = "Devoir 2"
  val master = "local"
  val file = "crawlers/allMonsters.json"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)


  val allMonsterString = scala.io.Source.fromFile(file).mkString
  val json = parse(allMonsterString)


  val allMonster = json.children
  var allMonsterList = new ListBuffer[Monster]()
  for (acct <- allMonster) {
    val monster = acct.extract[Monster]
    allMonsterList+= monster
  }

  val rddAllMonster = sc.parallelize(allMonsterList)
  println(rddAllMonster)

  val batchViewSpellMonsters = rddAllMonster.flatMap(monster=>
    monster.spells.map(spells => (spells, monster.name))
  ).groupByKey()

  batchViewSpellMonsters.foreach(println)
}