name := "flink-sbt"

version := "0.1"

scalaVersion := "2.11.12"

compileOrder := CompileOrder.JavaThenScala

libraryDependencies ++= {
  lazy val flinkVersion = "1.10.0"

  Seq(
    "org.slf4j"         % "slf4j-api"                    % "1.7.30",
    "ch.qos.logback"    % "logback-classic"              % "1.2.3",
    "com.amazonaws"     % "aws-kinesisanalytics-runtime" % "1.1.0",
    "org.apache.flink"  % "flink-java"                   % flinkVersion,
    "org.apache.flink" %% "flink-streaming-java"         % flinkVersion,
    "org.apache.flink" %% "flink-clients"                % flinkVersion,
    "org.apache.flink" %% "flink-connector-kinesis"      % flinkVersion,
    "org.json" % "json" % "20190722"
//    "com.google.code.gson" % "gson" % "2.8.6"
  )
}

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case "log4j.propreties" => MergeStrategy.first
  // ----
  // required for spark-sql to read different data types (e.g. parquet/orc/csv...)
  // ----
  case PathList("META-INF", "services", xs @ _*) => MergeStrategy.first
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}

mainClass in assembly := Some("flinkTest.BasicStreamingJob")

test in assembly := {}
