import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object Main extends App {
  val appname = "Devoir 2"
  val master = "local"
  val file = "crawlers/allMonsters.json"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  val sqlContext = SparkSession.builder().master(master).getOrCreate()

  val df = sqlContext.read.json(sc.wholeTextFiles(file).values)
  df.createOrReplaceTempView("bestiary")
  val sqlDF = sqlContext.sql("SELECT count(*) FROM bestiary")
  sqlDF.show()
}

