package org.opencompare.api.scala

class Feature extends AbstractFeature {

  private var _cells : List[Cell] = Nil

  def cells : List[Cell] = _cells
  def cells_= (value : List[Cell]) = {
    _cells = value
    for (cell <- _cells if cell.feature != this) {
      cell.feature = this
    }
  }

}
