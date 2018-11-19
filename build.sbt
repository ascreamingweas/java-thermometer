name := """play-java-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  guice,
  "org.mockito"           % "mockito-all"                         % "1.10.19" % "test"
)
