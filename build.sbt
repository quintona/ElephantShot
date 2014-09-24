import io.gatling.sbt.GatlingPlugin

name := "ElephantShot"

version := "0.0.1"

val Spark        = "1.0.1"

val HadoopClient = "2.4.0"

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.0.0-RC5" % "test",
  "io.gatling" % "test-framework" % "1.0-RC5" % "test",
  "org.apache.spark"  %% "spark-core"      % Spark,
  "org.apache.spark"  %% "spark-sql"       % Spark,
  "org.apache.spark"  %% "spark-hive"      % Spark,
  // Hack: explicitly add this dependency to workaround an Avro related bug
  // SPARK-1121? Appears to work! Otherwise, a java.lang.IncompatibleClassChangeError
  // is thrown in the call to saveAsParquetFile in SparkSQL9.scala.
  "org.apache.hadoop"  % "hadoop-client"   % HadoopClient
)

lazy val root = (project in file(".")).
  enablePlugins(GatlingPlugin)


