package org.opencompare.api.scala

class Feature extends AbstractFeature {

  private var _cells : Set[Cell] = Set.empty[Cell]

  def cells : Set[Cell] = _cells
  def cells_= (value : Set[Cell]) : Unit = {
    _cells = value
    for (cell <- _cells if cell.feature != this) { // Update other side of the association
      if (Option(cell.feature).isDefined) {
        cell.feature.cells = cell.feature.cells - cell
      }
      cell.feature = this
    }
  }

}
