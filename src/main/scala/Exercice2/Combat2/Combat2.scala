package Exercice2.Combat2

import org.apache.spark.{SparkConf, SparkContext}

object Combat2 extends App {
  val appname = "Devoir 2 - Exercice 2 - Combat 1. Solar vs Éclaireurs Orcs"
  val master = "local"
  val conf = new SparkConf().setAppName(appname).setMaster(master)
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")
}
