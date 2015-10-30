package org.opencompare.api.java.io

import org.opencompare.api.java.PCMFactory
import org.scalatest.{Matchers, FlatSpec}

import collection.JavaConversions._

/**
 * Created by gbecan on 10/2/15.
 */
abstract class IOMatrixLoaderTest extends FlatSpec with Matchers {

  val factory : PCMFactory

  it should "load a PCM with products as lines" in {
    val input = new IOMatrix()
      .setCell(new IOCell("Products"), 0, 0)
      .setCell(new IOCell("F1"), 0, 1)
      .setCell(new IOCell("F2"), 0, 2)
      .setCell(new IOCell("P1"), 1, 0)
      .setCell(new IOCell("C"), 1, 1)
      .setCell(new IOCell("C"), 1, 2)
      .setCell(new IOCell("P2"), 2, 0)
      .setCell(new IOCell("C"), 2, 1)
      .setCell(new IOCell("C"), 2, 2)

    val loader = new IOMatrixLoader(factory, PCMDirection.PRODUCTS_AS_LINES)
    val output = loader.load(input)

    output.getPcm.getConcreteFeatures.size() should be (3)
    output.getPcm.getProducts.size() should be (2)

    output.getPcm.getProducts.foreach(_.getCells.size() should be (3))

    val featureNames = output.getPcm.getConcreteFeatures.map(_.getName)
    featureNames.forall(name => name.startsWith("F") || name == "Products") shouldBe true

    val productNames = output.getPcm.getProducts.map(_.getKeyContent)
    productNames.forall(_.startsWith("P")) shouldBe true
  }


  it should "load a PCM with products as columns" in {

    val input = new IOMatrix()
      .setCell(new IOCell("Products"), 0, 0)
      .setCell(new IOCell("P1"), 0, 1)
      .setCell(new IOCell("P2"), 0, 2)
      .setCell(new IOCell("F1"), 1, 0)
      .setCell(new IOCell("C"), 1, 1)
      .setCell(new IOCell("C"), 1, 2)
      .setCell(new IOCell("F2"), 2, 0)
      .setCell(new IOCell("C"), 2, 1)
      .setCell(new IOCell("C"), 2, 2)

    val loader = new IOMatrixLoader(factory, PCMDirection.PRODUCTS_AS_COLUMNS)
    val output = loader.load(input)

    output.getPcm.getConcreteFeatures.size() should be (3)
    output.getPcm.getProducts.size() should be (2)

    output.getPcm.getProducts.foreach(_.getCells.size() should be (3))

    val featureNames = output.getPcm.getConcreteFeatures.map(_.getName)
    featureNames.forall(name => name.startsWith("F") || name == "Products") shouldBe true

    val productNames = output.getPcm.getProducts.map(_.getKeyContent)
    productNames.forall(_.startsWith("P")) shouldBe true

  }

}
