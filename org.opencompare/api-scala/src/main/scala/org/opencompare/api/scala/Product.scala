package org.opencompare.api.scala

class Product {

  private var _cells : Set[Cell] = Set.empty[Cell]

  var pcm : PCM = _
  def key : Feature = pcm.productsKey
  def keyCell : Option[Cell] = findCell(key)
  def keyContent : Option[String] = keyCell.map(_.content)

  def cells : Set[Cell] = _cells
  def cells_= (value : Set[Cell]) = {
    _cells = value
    for (cell <- _cells if cell.product != this) {
      cell.product = this
    }
  }

  def findCell(feature : Feature) : Option[Cell] = cells.find(_.feature == feature)




}
