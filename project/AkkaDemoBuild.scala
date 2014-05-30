import sbt._
import Keys._

object AkkaDemoBuild extends Build {
  name := "Akka Demo"
  version := "1.0-SNAPSHOT"
  organization := "myOrg"
  scalaVersion := "2.11.1"

  val akkaVersion = "2.3.3"

  lazy val root =
    project.aggregate(computeNode)

  lazy val akkaCommon = project

  lazy val computeNode =
    project
      .dependsOn(akkaCommon)
      .settings(
        libraryDependencies ++= List(
          "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
          "com.typesafe.akka" %% "akka-actor" % akkaVersion,
          "com.typesafe.akka" %% "akka-remote" % akkaVersion,
          "com.typesafe.akka" %% "akka-contrib" % akkaVersion
        )
      )

  lazy val client =
    project
      .dependsOn(akkaCommon)
      .settings(
        libraryDependencies ++= List(
          "com.typesafe.akka" %% "akka-actor" % akkaVersion,
          "com.typesafe.akka" %% "akka-remote" % akkaVersion,
          "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
          "com.github.scopt" %% "scopt" % "3.2.0"
        )
      )
}
