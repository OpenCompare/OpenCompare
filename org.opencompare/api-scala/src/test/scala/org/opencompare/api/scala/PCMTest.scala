package org.opencompare.api.scala

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

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

    // Create products
    pcm.products = (for (i <- 0 until 10) yield {
      new Product
    }).toList

    // Create features
    pcm.features = (for (i <-0 until 10) yield {
      val feature = new Feature
      feature.name = "Feature " + i
      feature
    }).toList



  }

}
