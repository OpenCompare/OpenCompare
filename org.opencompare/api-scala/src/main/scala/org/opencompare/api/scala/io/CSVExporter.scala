package org.opencompare.api.scala.io
import java.io.StringWriter

import com.github.tototoshi.csv.CSVWriter
import org.opencompare.api.scala.PCM
import org.opencompare.api.scala.metadata.{Orientation, Positions}

class CSVExporter extends PCMExporter {

  private val exportMatrixExporter = new ExportMatrixExporter

  /**
    * Export a PCM into a specific format
    *
    * @param pcm PCM to export
    * @return string representing the PCM
    */
  override def export(pcm: PCM with Positions with Orientation): String = {
    export(pcm, ',', '"')
  }

  def export(pcm: PCM with Positions with Orientation, separator : Char, quote : Char) : String = {
    val stringWriter = new StringWriter()
    val writer = CSVWriter.open(stringWriter)

    val exportMatrix = exportMatrixExporter.export(pcm)
    exportMatrix.flattenCells()

    for (row <- 0 until exportMatrix.numberOfRows) {
      val line = for (column <- 0 until exportMatrix.numberOfColumns) yield {
        val cell = exportMatrix.getCell(row, column)
        if (cell.isDefined) {
          cell.get.content
        } else {
          ""
        }
      }

      writer.writeRow(line)
    }

    writer.close()
    stringWriter.toString
  }
}
