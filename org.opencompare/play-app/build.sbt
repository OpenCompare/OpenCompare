import play.sbt.PlayImport._
import play.sbt.routes.RoutesKeys._

import scala.xml.XML

name := """OpenCompare"""

version := XML.loadFile("pom.xml").\("version").text

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := XML.loadFile("../pom.xml").\\("scala.version").text

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.webjars" % "angularjs" % "1.3.0",
  "org.webjars" % "angular-ui-bootstrap" % "0.13.0",
  "org.webjars" % "angular-translate" % "2.7.2",
  "org.webjars" % "font-awesome" % "4.4.0",
  "org.webjars" % "ui-grid" % "3.0.1",
  "org.webjars" % "bootstrap-material-design" % "0.3.0",
  "org.webjars" % "jquery-ui" % "1.11.4",
  "org.webjars" % "jquery-ui-themes" % "1.11.4",
  "org.webjars" % "angular-chart.js" % "0.7.1",
  "org.webjars.bower" % "angular-utf8-base64" % "0.0.5" exclude("org.webjars.bower", "angular"),
  "org.webjars.bower" % "angular-base64-upload" % "0.1.8" exclude("org.webjars.bower", "angular"),
  "org.webjars.bower" % "angular-ui-slider" % "0.1.1" exclude("org.webjars.bower", "angular") exclude("org.webjars.bower", "jquery"),
  "org.webjars.bower" % "bootstrap-autohidingnavbar" % "1.0.2" exclude("org.webjars.bower", "bootstrap") exclude("org.webjars.bower", "jquery"),
  "org.webjars.bower" % "angular-clipboard" % "1.1.0" exclude("org.webjars.bower", "angular"),
  cache,
  filters
)

routesGenerator := InjectedRoutesGenerator