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
    // TODO
    ProductsAsRows()
  }

}
