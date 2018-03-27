import org.apache.spark.{SparkConf, SparkContext}

object Example extends App {
  val appname = "Devoir 2"
  val master = "local"
  val file = "crawlers/allMonsters.json"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)

  val data = sc.parallelize(
    List(
      ("premier", List("Sort1", "Sort2")),
      ("second", List("Sort1")),
      ("troisieme", List())
    )
  )

  println(data)
  data.foreach(println)

  val a = data.flatMap(pair=>
    pair._2.map(spells => (spells, pair._1))
  ).groupByKey()

  a.foreach(println)
}
