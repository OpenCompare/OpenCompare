package org.opencompare.api.scala

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

trait Position {
  var position : Int
}

class PCMTest extends FlatSpec with Matchers with BeforeAndAfterAll {


  it should "create a cell" in {
    val cell = new Cell
    cell.content = "content"
    cell.rawContent = "raw content"

    cell.content should be ("content")
    cell.rawContent should be ("raw content")
  }

  it should "create a product" in {
    val product = new Product

  }

  it should "create a feature" in {
    val feature = new Feature
    feature.name = "name"
    feature.name should be ("name")
  }

  it should "create a feature group" in {
    val featureGroup = new FeatureGroup
    featureGroup.name = "name"
    featureGroup.name should be ("name")
  }

  it should "create a PCM" in {
    val pcm = new PCM

    pcm.name = "pcm"
    pcm.name should be ("pcm")

    // Create features
    val features = (for (i <- 0 until 10) yield {
      val feature = new Feature
      feature.name = "Feature " + i
      i -> feature
    }).toMap

    pcm.features = features.values.toList

      // Create products
    pcm.products = (for (i <- 0 until 10) yield {
      val product = new Product

      product.cells = (for (j <- 0 until 10) yield {
        val cell = new Cell with Position {
          override var position: Int = j
        }
        cell.rawContent = "c" + i + j
        cell.content = cell.rawContent
        cell.feature = features(j)
        cell
      }).toSet

      product
    }).toList


    pcm.productsKey = features(0)

    for ((product, productIndex) <- pcm.products.zipWithIndex) {

      product.key.name should be ("Feature 0")

      for (cell <- product.cells) {
        val position = cell.asInstanceOf[Cell with Position].position
        cell.rawContent should be ("c" + productIndex + position)
        cell.content should be ("c" + productIndex + position)
        cell.feature.name should be ("Feature " + position)
        cell.product should be (product)
      }
    }


  }

}
