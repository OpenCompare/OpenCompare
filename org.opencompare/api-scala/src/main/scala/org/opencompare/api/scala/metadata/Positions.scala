package org.opencompare.api.scala.metadata

import org.opencompare.api.scala.{Feature, Product, PCM}

trait Positions extends PCM {

  var featurePositions : Map[Feature, Int] = Map.empty[Feature, Int]
  var productPositions : Map[Product, Int] = Map.empty[Product, Int]

  def sortedFeatures() : List[Feature] = concreteFeatures.toList.sortBy(f => featurePositions.getOrElse(f, -1))
  def sortedProducts() : List[Product] = products.toList.sortBy(p => productPositions.getOrElse(p, -1))

}
