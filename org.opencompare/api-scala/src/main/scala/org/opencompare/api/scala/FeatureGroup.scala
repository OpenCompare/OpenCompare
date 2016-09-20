package org.opencompare.api.scala

class FeatureGroup extends AbstractFeature {

  var subFeatures : List[AbstractFeature] = Nil

  def concreteFeatures : List[Feature] = {
    subFeatures.flatMap {
      case f : Feature => List(f)
      case fg : FeatureGroup => fg.concreteFeatures
    }
  }

  def depth : Int = subFeatures.map {
    case f : Feature => 1
    case fg : FeatureGroup => 1 + fg.depth
  }.max

}
