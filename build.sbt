ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "atl_scala",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.4.0",
      "org.apache.spark" %% "spark-sql" % "3.4.0",
      "org.apache.commons" % "commons-lang3" % "3.12.0"

    )
  )
