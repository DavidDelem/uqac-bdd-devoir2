import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.DataFrame

object Main extends App {
  val appname = "Devoir 2"
  val master = "local"
  val file = "data.txt"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  //val data = sc.textFile(file)
  //read.json(data)
  val rdd = sc.parallelize(data)
  //val lineLengths = data.flatMap(Functions.func1)
  //val totalLength = lineLengths.reduce((a, b) => a + b)
}

object Functions {
  def func1(s: String): String = {
    s
  }
}

