package org.opencompare.api.scala

class Product {

  private var _cells : Set[Cell] = Set.empty[Cell]

  var pcm : PCM = _
  def key : Feature = pcm.productsKey
  def keyCell : Option[Cell] = findCell(key)
  def keyContent : Option[String] = keyCell.map(_.content)

  def cells : Set[Cell] = _cells
  def cells_= (value : Set[Cell]) : Unit = {
    _cells = value // Change value
    for (cell <- _cells if cell.product != this) { // Update other side of the association
      if (Option(cell.product).isDefined) {
        cell.product.cells = cell.product.cells - cell
      }
      cell.product = this
    }
  }

  def findCell(feature : Feature) : Option[Cell] = cells.find(_.feature == feature)




}
