package org.opencompare.api.scala.metadata

import org.opencompare.api.scala.{Feature, PCM}

trait Positions extends PCM {

  var featurePositions : Map[Feature, Int] = Map.empty[Feature, Int]
  var productPositions : Map[Product, Int] = Map.empty[Product, Int]

}
