package org.opencompare.api.scala

class PCM {

  private var _products : Set[Product] = Set.empty[Product]

  var name : String = ""

  def products : Set[Product] = _products
  def products_= (value: Set[Product]) = {
    _products = value
    for (product <- _products if product.pcm != this) {
      product.pcm = this
    }
  }

  var productsKey : Feature = _

  var features : Set[AbstractFeature] = Set.empty

  def concreteFeatures : Set[Feature] = {
    features.flatMap {
      case f : Feature => Set(f)
      case fg : FeatureGroup => fg.concreteFeatures
    }
  }

  def depthOfFeatureHierarchy() : Int = {
    features.map {
      case f : Feature => 1
      case fg : FeatureGroup => fg.depth
    }.max
  }


}
