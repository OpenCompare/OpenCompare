package org.opencompare.api.scala.io

import org.opencompare.api.scala.PCM
import org.opencompare.api.scala.metadata.{Orientation, PCMOrientation, Positions}

class ImportMatrixLoader(val orientation : PCMOrientation) {


  def load(matrix: ImportMatrix) : PCM = {



    // Detect types and information for each cell

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

  }

}
