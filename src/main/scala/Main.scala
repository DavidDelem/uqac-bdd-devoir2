import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import net.liftweb.json.DefaultFormats
import net.liftweb.json._

import scala.collection.mutable.ListBuffer

object Main extends App {
  //Pour la lectur du json
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
  //Lecture du json monster par monster (Voir class Monster) qu'on met dans une liste
  for (acct <- allMonster) {
    val monster = acct.extract[Monster]
    allMonsterList+= monster
  }

  //Création du rdd à partir de notre liste
  val rddAllMonster = sc.parallelize(allMonsterList)
  println(rddAllMonster)

  //création de la batch view (sort(monstre1, monstre2, monstre3...))
  val batchViewSpellMonsters = rddAllMonster.flatMap(monster=>
    monster.spells.map(spells => (spells, monster.name))
  ).groupByKey()

  //Création d'un fichier html avec les résultats
  val saveFile = "batchViewSpellMonsters.html"

  val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile)))

  writer.write("<style>table, th, td {\n    border: 1px solid black;\n    border-collapse: collapse;\n}</style>\n")
  writer.write("<table>\n  <tr>\n    <th>Spell</th>\n    <th>Monsters</th> \n </tr>")

  for (spellMonsters <- batchViewSpellMonsters) {
    writer.write("<tr>\n")
    writer.write("<td>"+spellMonsters._1 + "</td>\n")
    writer.write("<td>")
    for(monster <- spellMonsters._2) {
      writer.write(monster + " ")
    }
    writer.write("</td>\n")
    writer.write("</tr>\n")
  }

  writer.write("<table> \n")
  writer.close()

}