package org.opencompare.api.scala

class PCM {

  var name : String = ""

  var products : List[Product] = Nil

  var productsKey : Feature = _

  var features : List[AbstractFeature] = Nil

  def concreteFeatures : List[Feature] = {
    features.flatMap {
      case f : Feature => List(f)
      case fg : FeatureGroup => fg.concreteFeatures
    }
  }


}
