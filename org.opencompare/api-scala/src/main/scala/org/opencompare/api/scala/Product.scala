package org.opencompare.api.scala

class Product {

  private var _cells : Set[Cell] = Set.empty[Cell]

  var pcm : PCM = _
  def key : Option[Feature] = pcm.productsKey
  def keyCell : Option[Cell] = key.flatMap(findCell(_))
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


  def canEqual(other: Any): Boolean = other.isInstanceOf[Product]

  override def equals(other: Any): Boolean = other match {
    case that: Product =>
      (that canEqual this) &&
        _cells == that._cells
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(_cells)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }


  override def toString = s"Product($cells)"
}
