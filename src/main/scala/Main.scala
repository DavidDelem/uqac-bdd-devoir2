import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Main extends App {
  val appname = "Devoir 2"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  val lines = sc.textFile("data.txt")
  val lineLengths = lines.flatMap(Functions.func1).collect
  //val totalLength = lineLengths.reduce((a, b) => a + b)
}

object Functions {
  def func1(s: String): String = {
    s
  }
}

