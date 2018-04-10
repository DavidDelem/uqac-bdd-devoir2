name := "uqac-bdd-devoir2"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.1.0",
  "org.apache.spark" %% "spark-sql" % "2.1.0",
  "net.liftweb" %% "lift-json" % "3.3.0-M1",
  "org.apache.spark" %% "spark-graphx" % "2.1.0"


)