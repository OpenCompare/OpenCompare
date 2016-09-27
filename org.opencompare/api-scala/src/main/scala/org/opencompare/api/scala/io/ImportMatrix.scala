package org.opencompare.api.scala.io

class ImportMatrix extends IOMatrix[ImportCell] {

  def removeDuplicatedRows(): Unit = {
    // Remove duplicated rows
    val distinctRows = rows().distinct

    // Update matrix
    updateMatrix(distinctRows)

    // Update maximum number of rows
    maxRow = distinctRows.size - 1
  }

  def removeEmptyRows(): Unit = {
    // Remove empty rows
    val nonEmptyRows = rows().filter { cells =>
        cells.forall { cell =>
          cell.isEmpty || cell.get.content.matches("\\s*")
        }
    }

    // Update matrix
    updateMatrix(nonEmptyRows)

    maxRow = nonEmptyRows.size - 1
  }

  private def updateMatrix(rows : List[List[Option[ImportCell]]]): Unit = {
    cells = rows.zipWithIndex.flatMap { case (row, rowIndex) =>
      for ((cell, columnIndex) <- row.zipWithIndex if cell.isDefined) yield {
        (rowIndex, columnIndex) -> cell.get
      }
    }.toMap
  }
}
