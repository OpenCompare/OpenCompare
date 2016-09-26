package org.opencompare.api.scala

class PCM {

  private var _products : List[Product] = Nil

  var name : String = ""

  def products : List[Product] = _products
  def products_= (value: List[Product]) = {
    _products = value
    for (product <- _products if product.pcm != this) {
      product.pcm = this
    }
  }

  var productsKey : Feature = _

  var features : List[AbstractFeature] = Nil

  def concreteFeatures : List[Feature] = {
    features.flatMap {
      case f : Feature => List(f)
      case fg : FeatureGroup => fg.concreteFeatures
    }
  }


}
