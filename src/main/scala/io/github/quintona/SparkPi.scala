package io.github.quintona

import scala.math.random

import org.apache.spark._

/** Computes an approximation to pi */
object SparkPi {

  def setSystemProperties(master:Option[String]){
    val resolvedMaster = master match {
      case Some(m) => m
      case _ => "spark://localhost:7077"
    }
    System.setProperty("spark.master", resolvedMaster)

  }

  def main(args: Array[String]) {
    val master = if (args.length > 0) Some(args(0)) else None
    setSystemProperties(master)
    val conf = new SparkConf().setAppName("Spark Pi")
    val spark = new SparkContext(conf)
    val slices = 2
    val n = 100000 * slices
    val count = spark.parallelize(1 to n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x*x + y*y < 1) 1 else 0
    }.reduce(_ + _)
    println("Pi is roughly " + 4.0 * count / n)
    spark.stop()
  }
}