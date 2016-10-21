package org.opencompare.api.scala

import org.scalatest.{FlatSpec, Matchers}

trait OpenCompareTest extends FlatSpec with Matchers {

  // Utils functions
  def createFeature(pcm : PCM, name : String) : Feature = {
    val feature = new Feature
    feature.name = name
    pcm.features += feature
    feature
  }

  def createFeature(fg : FeatureGroup, name : String) : Feature = {
    val feature = new Feature
    feature.name = name
    fg.subFeatures += feature
    feature
  }

  def createFeatureGroup(pcm: PCM, name: String) : FeatureGroup = {
    val featureGroup = new FeatureGroup
    featureGroup.name = name
    pcm.features += featureGroup
    featureGroup
  }

  def createProduct(pcm : PCM) : Product = {
    val product = new Product
    pcm.products += product
    product
  }

  def createCell(product : Product, feature : Feature, content : String, interpretation : Option[Value]) : Cell = {
    val cell = new Cell
    cell feature = feature
    cell.content = content
    cell.interpretation = interpretation
    product.cells += cell
    cell
  }

}
