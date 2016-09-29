package org.opencompare.api.scala

class FeatureGroup extends AbstractFeature {

  private var _subFeatures : Set[AbstractFeature] = Set.empty[AbstractFeature]

  def subFeatures : Set[AbstractFeature] = _subFeatures
  def subFeatures_= (value : Set[AbstractFeature]) = {
    _subFeatures = value
    _subFeatures.foreach(feature => feature.parent = Some(this))
  }


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


  def canEqual(other: Any): Boolean = other.isInstanceOf[FeatureGroup]

  override def equals(other: Any): Boolean = other match {
    case that: FeatureGroup =>
      (that canEqual this) &&
        name == that.name &&
        parent == that.parent
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, parent)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"FeatureGroup($name, $subFeatures)"
}
