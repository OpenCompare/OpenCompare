package org.opencompare.api.java.io

import org.opencompare.api.java.PCMFactory
import org.opencompare.api.java.interpreter.CellContentInterpreter
import org.scalatest.{Matchers, FlatSpec}

import collection.JavaConversions._

/**
 * Created by gbecan on 10/2/15.
 */
abstract class ImportMatrixLoaderTest extends FlatSpec with Matchers {

  val factory : PCMFactory
  val cellContentInterpreter: CellContentInterpreter

  it should "load a PCM with products as lines" in {
    val input = new ImportMatrix()
    input
      .setCell(new ImportCell("Products"), 0, 0)
      .setCell(new ImportCell("F1"), 0, 1)
      .setCell(new ImportCell("F2"), 0, 2)
      .setCell(new ImportCell("P1"), 1, 0)
      .setCell(new ImportCell("C"), 1, 1)
      .setCell(new ImportCell("C"), 1, 2)
      .setCell(new ImportCell("P2"), 2, 0)
      .setCell(new ImportCell("C"), 2, 1)
      .setCell(new ImportCell("C"), 2, 2)

    val loader = new ImportMatrixLoader(factory, cellContentInterpreter, PCMDirection.PRODUCTS_AS_LINES)
    val output = loader.load(input)

    withClue("concrete features")(output.getPcm.getConcreteFeatures.size() should be (3))
    withClue("products")(output.getPcm.getProducts.size() should be (2))

    withClue("cells") {
      output.getPcm.getProducts.foreach(_.getCells.size() should be(3))
    }

    withClue("feature names") {
      val featureNames = output.getPcm.getConcreteFeatures.map(_.getName)
      featureNames.forall(name => name.startsWith("F") || name == "Products") shouldBe true
    }

    withClue("product names") {
      val productNames = output.getPcm.getProducts.map(_.getKeyContent)
      productNames.forall(_.startsWith("P")) shouldBe true
    }
  }


  it should "load a PCM with products as columns" in {

    val input = new ImportMatrix()
    input
      .setCell(new ImportCell("Products"), 0, 0)
      .setCell(new ImportCell("P1"), 0, 1)
      .setCell(new ImportCell("P2"), 0, 2)
      .setCell(new ImportCell("F1"), 1, 0)
      .setCell(new ImportCell("C"), 1, 1)
      .setCell(new ImportCell("C"), 1, 2)
      .setCell(new ImportCell("F2"), 2, 0)
      .setCell(new ImportCell("C"), 2, 1)
      .setCell(new ImportCell("C"), 2, 2)

    val loader = new ImportMatrixLoader(factory, cellContentInterpreter, PCMDirection.PRODUCTS_AS_COLUMNS)
    val output = loader.load(input)

    withClue("concrete features")(output.getPcm.getConcreteFeatures.size() should be (3))
    withClue("products")(output.getPcm.getProducts.size() should be (2))

    withClue("cells") {
      output.getPcm.getProducts.foreach(_.getCells.size() should be (3))
    }

    withClue("products key")(output.getPcm.getProductsKey.getName should be ("Products"))

    withClue("feature names") {
      val featureNames = output.getPcm.getConcreteFeatures.map(_.getName)
      featureNames.forall(name => name.startsWith("F") || name == "Products") shouldBe true
    }

    withClue("product names") {
      val productNames = output.getPcm.getProducts.map(_.getKeyContent)
      productNames.forall(_.startsWith("P")) shouldBe true
    }

  }

  it should "load a PCM with feature groups" in {

    //    "Products","FG","FG"
    //    "Products","F1","F2"
    //    "P1","C11",C12"
    //    "P2","C21",C22"


    val input = new ImportMatrix()
    input
      .setCell(new ImportCell("Products"), 0, 0)
      .setCell(new ImportCell("FG"), 0, 1)
      .setCell(new ImportCell("FG"), 0, 2)
      .setCell(new ImportCell("Products"), 1, 0)
      .setCell(new ImportCell("F1"), 1, 1)
      .setCell(new ImportCell("F2"), 1, 2)
      .setCell(new ImportCell("P1"), 2, 0)
      .setCell(new ImportCell("C11"), 2, 1)
      .setCell(new ImportCell("C12"), 2, 2)
      .setCell(new ImportCell("P2"), 3, 0)
      .setCell(new ImportCell("C21"), 3, 1)
      .setCell(new ImportCell("C22"), 3, 2)

    val loader = new ImportMatrixLoader(factory, cellContentInterpreter, PCMDirection.PRODUCTS_AS_LINES)
    val pcm = loader.load(input).getPcm


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
