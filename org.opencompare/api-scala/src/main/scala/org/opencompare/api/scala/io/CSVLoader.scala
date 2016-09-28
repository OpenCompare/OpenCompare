package org.opencompare.api.scala.io
import java.io.File

import org.opencompare.api.scala.PCM
import org.opencompare.api.scala.interpreter.CellContentInterpreter
import org.opencompare.api.scala.metadata.{PCMOrientation, Unknown}
import com.github.tototoshi.csv._

import scala.io.Source

class CSVLoader(cellContentInterpreter: CellContentInterpreter, separator : Char = ',', quote : Char = '"', orientation : PCMOrientation = Unknown()) extends PCMLoader {

  private val importMatrixLoader = new ImportMatrixLoader(cellContentInterpreter, orientation)

  /**
    * Return a list of PCMs from a string representation
    *
    * @param pcms : string representation of a PCM
    * @return the PCM represented by pcm
    */
  override def load(pcms: String): List[PCM] = {
    val reader = CSVReader.open(Source.fromString(pcms))
    readCSV(reader)
  }

  /**
    * Return a list of PCMs from a file
    *
    * @param file file to load
    * @return loaded PCM
    */
  override def load(file: File): List[PCM] = {
    val reader = CSVReader.open(file)
    readCSV(reader)
  }

  def readCSV(reader : CSVReader) : List[PCM] = {
    val importMatrix = new ImportMatrix

    for ((cells, row) <- reader.toStream.zipWithIndex) {
      for ((cell, column) <- cells.zipWithIndex) {
        importMatrix.setCell(new ImportCell(cell, cell), row, column)
      }

    }

    reader.close()
    List(importMatrixLoader.load(importMatrix))
  }
}
