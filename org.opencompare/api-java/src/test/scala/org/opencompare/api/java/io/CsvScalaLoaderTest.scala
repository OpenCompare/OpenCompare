package org.opencompare.api.java.io

import org.opencompare.api.java.PCMFactory
import org.opencompare.api.java.interpreter.CellContentInterpreter
import org.scalatest.{Matchers, FlatSpec}

import scala.io.Source

import collection.JavaConversions._

/**
  * Created by gbecan on 20/11/15.
  */
abstract class CsvScalaLoaderTest(val factory : PCMFactory, val cellContentInterpreter: CellContentInterpreter) extends FlatSpec with Matchers {

  it should "load CSV with feature groups" in {
    val csv = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("csv/Feature-group.csv")).mkString
    val csvLoader = new CSVLoader(factory, cellContentInterpreter)
    val pcmContainers = csvLoader.load(csv)

    pcmContainers shouldNot be ('empty)

    val pcm = pcmContainers.head.getPcm
    withClue("features") (pcm.getFeatures.size() should be (2))
    withClue("concrete features")(pcm.getConcreteFeatures.size() should be (3))

    for (feature <- pcm.getFeatures) {
      withClue("top feature name") (Set("Products", "FG") should contain (feature.getName))
    }

    for (feature <- pcm.getConcreteFeatures) {
      withClue("feature name") (Set("Products", "F1", "F2") should contain (feature.getName))
    }

    withClue("products")(pcm.getProducts.size() should be (2))
  }
}
