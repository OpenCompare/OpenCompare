package org.opencompare.api.scala.io

class IOMatrix[T <: IOCell] {

  protected var maxRow : Int = 0
  protected var maxColumn : Int = 0
  protected var cells : Map[(Int, Int), T] = Map.empty[(Int, Int), T]
  private var _name : String = ""

  def name : String = _name
  def name_= (value : String) : IOMatrix[T] = {
    _name = value
    this
  }

  def getCell(row : Int, column : Int) : Option[T] = {
    cells.get((row, column))
  }

  def setCell(cell: T, row: Int, column: Int): IOMatrix[T] = {
    cells = cells + ((row, column) -> cell)

    maxRow = if (maxRow < (row + cell.rowspan - 1)) {
      row + cell.rowspan - 1
    }
    else {
      maxRow
    }

    maxColumn = if (maxColumn < (column + cell.colspan - 1)) {
      column + cell.colspan - 1
    } else {
      maxColumn
    }

    this
  }

  def isPositionOccupied(row: Int, column: Int) : Boolean = {

    // Check cell is defined
    var result = getCell(row, column).isDefined

    // Check previous cells with rowspan
    result = result || (0 to row).reverse
      .map(i => (i, getCell(i, column)))
      .exists { e => e._2.isDefined && (e._1 + e._2.get.rowspan > row) }

    // Check previous cells with colspan
    result = result || (0 to column).reverse
      .map(j => (j, getCell(row, j)))
      .exists { e => e._2.isDefined && (e._1 + e._2.get.colspan > column) }

    result
  }

}
