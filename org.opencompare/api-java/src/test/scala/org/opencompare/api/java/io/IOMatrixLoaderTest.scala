package org.opencompare.api.java.io

import org.opencompare.api.java.PCMFactory
import org.scalatest.{Matchers, FlatSpec}

import collection.JavaConversions._

/**
 * Created by gbecan on 10/2/15.
 */
abstract class IOMatrixLoaderTest extends FlatSpec with Matchers {

  val factory : PCMFactory

  it should "load a PCM with products as columns" in {

    val input = new IOMatrix()
      .setCell(new IOCell(""), 0, 0)
      .setCell(new IOCell("P1"), 0, 1)
      .setCell(new IOCell("P2"), 0, 2)
      .setCell(new IOCell("F1"), 1, 0)
      .setCell(new IOCell("C"), 1, 1)
      .setCell(new IOCell("C"), 1, 2)
      .setCell(new IOCell("F2"), 2, 0)
      .setCell(new IOCell("C"), 2, 1)
      .setCell(new IOCell("C"), 2, 2)

    val loader = new IOMatrixLoader(factory, false)
    val output = loader.load(input).head

    val featureNames = output.getPcm.getConcreteFeatures.map(_.getName)
    featureNames.forall(_.startsWith("F")) shouldBe true

    val productNames = output.getPcm.getProducts.map(_.getName)
    productNames.forall(_.startsWith("P")) shouldBe true

  }

}
