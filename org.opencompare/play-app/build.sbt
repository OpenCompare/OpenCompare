import play.Project._

name := """PCM app"""

version := "0.2-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.2.2", 
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.webjars" % "angularjs" % "1.3.0",
  "org.webjars" % "font-awesome" % "4.3.0-1",
  "org.webjars" % "handsontable" % "0.12.5")

playJavaSettings
