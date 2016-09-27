package org.opencompare.api.scala.io

import org.opencompare.api.scala.PCM
import org.opencompare.api.scala.interpreter.CellContentInterpreter
import org.opencompare.api.scala.metadata.{Orientation, PCMOrientation, Positions}

class ImportMatrixLoader(val cellContentInterpreter: CellContentInterpreter, orientation : PCMOrientation) {


  def load(matrix: ImportMatrix) : PCM = {



    // Detect types and information for each cell
    detectTypes(matrix)

    // Expand rowpsan and colspan

    // Remove holes in matrix

    // Detect direction of the matrix

    // Remove empty and duplicated lines

    // Remove empty and duplicated columns

    // Transpose matrix if necessary

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

}
