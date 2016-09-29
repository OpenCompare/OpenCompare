package org.opencompare.api.scala.io

import org.opencompare.api.scala.interpreter.{CellContentInterpreter, DefaultCellContentInterpreter}
import org.opencompare.api.scala.metadata.{ProductsAsColumns, ProductsAsRows}
import org.scalatest.{FlatSpec, Matchers}

class ImportMatrixLoaderTest extends FlatSpec with Matchers {

  val cellContentInterpreter: CellContentInterpreter = new DefaultCellContentInterpreter

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

    val loader = new ImportMatrixLoader(cellContentInterpreter, ProductsAsRows())
    val pcm = loader.load(input)

    withClue("concrete features")(pcm.concreteFeatures.size should be (3))
    withClue("products")(pcm.products.size should be (2))

    withClue("cells") {
      pcm.products.foreach(_.cells.size should be (3))
    }

    withClue("feature names") {
      val featureNames = pcm.concreteFeatures.map(_.name)
      featureNames.forall(name => name.startsWith("F") || name == "Products") shouldBe true
    }

    withClue("product names") {
      val productNames = pcm.products.flatMap(_.keyContent)
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

    val loader = new ImportMatrixLoader(cellContentInterpreter, ProductsAsColumns())
    val pcm = loader.load(input)

    withClue("concrete features")(pcm.concreteFeatures.size should be (3))
    withClue("products")(pcm.products.size should be (2))

    withClue("cells") {
      pcm.products.foreach(_.cells.size should be (3))
    }

    withClue("products key")(pcm.productsKey.map(_.name) should be (Some("Products")))

    withClue("feature names") {
      val featureNames = pcm.concreteFeatures.map(_.name)
      featureNames.forall(name => name.startsWith("F") || name == "Products") shouldBe true
    }

    withClue("product names") {
      val productNames = pcm.products.flatMap(_.keyContent)
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

    val loader = new ImportMatrixLoader(cellContentInterpreter, ProductsAsRows())
    val pcm = loader.load(input)


    withClue("features") (pcm.features.size should be (2))
    withClue("concrete features")(pcm.concreteFeatures.size should be (3))

    for (feature <- pcm.features) {
      withClue("top feature name") (Set("Products", "FG") should contain (feature.name))
    }

    for (feature <- pcm.concreteFeatures) {
      withClue("feature name") (Set("Products", "F1", "F2") should contain (feature.name))
    }

    withClue("products")(pcm.products.size should be (2))

  }

}
