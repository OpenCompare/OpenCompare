package org.opencompare.api.scala

class Feature extends AbstractFeature {

  private var _cells : Set[Cell] = Set.empty[Cell]

  def cells : Set[Cell] = _cells
  def cells_= (value : Set[Cell]) = {
    _cells = value
    for (cell <- _cells if cell.feature != this) {
      // TODO : update previous instance if it exists
      cell.feature = this
    }
  }

}
