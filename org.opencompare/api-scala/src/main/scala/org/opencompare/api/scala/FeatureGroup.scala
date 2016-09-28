package org.opencompare.api.scala

class FeatureGroup extends AbstractFeature {

  var subFeatures : Set[AbstractFeature] = Set.empty[AbstractFeature]

  def concreteFeatures : Set[Feature] = {
    subFeatures.flatMap {
      case f : Feature => Set(f)
      case fg : FeatureGroup => fg.concreteFeatures
    }
  }

  def depth : Int = subFeatures.map {
    case f : Feature => 1
    case fg : FeatureGroup => 1 + fg.depth
  }.max

}
