package org.opencompare.api.scala

class PCM {

  var name : String = ""
  var features : Set[AbstractFeature] = Set.empty
  var productsKey : Option[Feature] = None
  private var _products : Set[Product] = Set.empty[Product]


  def products : Set[Product] = _products
  def products_= (value: Set[Product]) = {
    _products = value
    for (product <- _products if product.pcm != this) {
      product.pcm = this
    }
  }

  def concreteFeatures : Set[Feature] = {
    features.flatMap {
      case f : Feature => Set(f)
      case fg : FeatureGroup => fg.concreteFeatures
    }
  }

  def depthOfFeatureHierarchy() : Int = {
    features.map {
      case f : Feature => 1
      case fg : FeatureGroup => 1 + fg.depth
    }.max
  }


  def canEqual(other: Any): Boolean = other.isInstanceOf[PCM]

  override def equals(other: Any): Boolean = other match {
    case that: PCM =>
      (that canEqual this) &&
        name == that.name &&
        features == that.features &&
        productsKey == that.productsKey &&
        _products == that._products
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, features, productsKey, _products)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
