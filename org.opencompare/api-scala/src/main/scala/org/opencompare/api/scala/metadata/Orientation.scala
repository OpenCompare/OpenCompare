package org.opencompare.api.scala.metadata

import org.opencompare.api.scala.PCM

trait Orientation extends PCM {
  var orientation : PCMOrientation = ProductsAsRows()
}

trait PCMOrientation
case class ProductsAsRows() extends PCMOrientation
case class ProductsAsColumns() extends PCMOrientation
case class Unknown() extends PCMOrientation
