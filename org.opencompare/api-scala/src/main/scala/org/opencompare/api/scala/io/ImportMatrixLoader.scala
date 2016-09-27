package org.opencompare.api.scala.io

import org.opencompare.api.scala.PCM
import org.opencompare.api.scala.interpreter.CellContentInterpreter
import org.opencompare.api.scala.metadata._

class ImportMatrixLoader(val cellContentInterpreter: CellContentInterpreter, orientation : PCMOrientation) {


  def load(matrix: ImportMatrix) : PCM = {
    // Detect types and information for each cell
    detectTypes(matrix)

    // Expand rowpsan and colspan
    matrix.flattenCells()

    // Remove holes in matrix
    removeHoles(matrix)

    // Detect orientation of the matrix
    val detectedOrientation = orientation match {
      case Unknown() => detectOrientation(matrix)
      case _ => orientation
    }

    // Remove empty and duplicated rows
    matrix.removeEmptyRows()
    matrix.removeDuplicatedRows()

    // Remove empty and duplicated columns
    matrix.transpose()
    matrix.removeEmptyRows()
    matrix.removeDuplicatedRows()

    // Transpose matrix if necessary
    detectedOrientation match {
      case ProductsAsRows() => matrix.transpose()
      case _ =>
    }

    // Create PCM
    val pcm = new PCM with Orientation with Positions
    pcm.name = matrix.name

    // Create features

    // Set feature positions in metadata

    // Create products

    // Set products key


    pcm
  }

  /**
    * Detect types of each cell of the matrix
    * @param matrix matrix
    */
  protected def detectTypes(matrix: ImportMatrix): Unit = {
    for (row <- 0 until matrix.numberOfRows;
         column <- 0 until matrix.numberOfColumns) {
      matrix.getCell(row, column).foreach { cell =>
        if (cell.interpretation.isEmpty) {
          cell.interpretation = cellContentInterpreter.interpretString(cell.content)
        }
      }
    }
  }

  protected def removeHoles(matrix: ImportMatrix): Unit = {
    for (row <- 0 until matrix.numberOfRows;
         column <- 0 until matrix.numberOfColumns) {
      val cell = matrix.getCell(row, column)
      if (cell.isEmpty) {
        matrix.setCell(new ImportCell(), row, column)
      }
    }
  }

  protected def detectOrientation(matrix: ImportMatrix): PCMOrientation = {

    // Compute homogeneity of rows
    val homogeneityOfRows = for (row <- 0 until matrix.numberOfRows) yield {
      // Get types
      val types = (for (column <- 0 until matrix.numberOfColumns) yield {
        matrix.getCell(row, column).flatMap(_.interpretation.map(_.getClass.getName))
      }).flatten

      // Count the number of cells with the main types
      val mainType = types.groupBy(identity[String]).map(_._2.size).max

      // Compute homogeneity of the row
      val homogeneity = mainType.toDouble / matrix.numberOfRows().toDouble
      homogeneity
    }

    val globalHomogeneityOfRows = homogeneityOfRows.sum / matrix.numberOfRows

    // Compute homogeneity of columns
    val homogeneityOfColumns = for (column <- 0 until matrix.numberOfColumns) yield {
      // Get types
      val types = (for (row <- 0 until matrix.numberOfRows) yield {
        matrix.getCell(row, column).flatMap(_.interpretation.map(_.getClass.getName))
      }).flatten

      // Count the number of cells with the main types
      val mainType = types.groupBy(identity[String]).map(_._2.size).max

      // Compute homogeneity of the row
      val homogeneity = mainType.toDouble / matrix.numberOfColumns().toDouble
      homogeneity
    }

    val globalHomogeneityOfColumns = homogeneityOfColumns.sum / matrix.numberOfColumns

    if (globalHomogeneityOfRows > globalHomogeneityOfColumns) {
      ProductsAsColumns()
    } else {
      ProductsAsRows()
    }


  }

}
