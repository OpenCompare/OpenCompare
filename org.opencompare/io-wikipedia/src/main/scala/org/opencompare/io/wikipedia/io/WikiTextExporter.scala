package org.opencompare.io.wikipedia.io

import java.io.{InputStream, FileInputStream, BufferedInputStream}

import org.opencompare.api.java.{Cell, PCMMetadata, PCMContainer, PCM}
import org.opencompare.api.java.io.{ExportMatrixExporter, PCMExporter}

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 26/11/14.
 */
class WikiTextExporter(exportRawContent : Boolean = false)  extends PCMExporter {

  private var exportMatrixExporter = new ExportMatrixExporter

  // Constructor for Java compatibility with default parameters
  def this() {
    this(false)
  }

  override def export(container: PCMContainer): String = {
    val builder = new StringBuilder
    val pcm = container.getPcm

    builder ++= "{| class=\"wikitable\"\n" // new table
    val title = pcm.getName
    builder ++= "|+ " + title + "\n" // caption

    val exportMatrix = exportMatrixExporter.export(container)

    for (row <- 0 to exportMatrix.getNumberOfRows) {

      builder ++= "|-\n"

      for(column <- 0 to exportMatrix.getNumberOfColumns) {

        val exportCell = exportMatrix.getCell(row, column)
        if (Option(exportCell).isDefined) {

          if (exportCell.isFeature || exportCell.isInProductsKeyColumn) {
            builder ++= "! " // new cell (we can also use !! to separate cells horizontally)
          } else {
            builder ++= "| " // new cell (we can also use || to separate cells horizontally)
          }

          if (exportCell.getRowspan > 1) {
            builder ++= "rowspan=\"" + exportCell.getRowspan + "\""
          }

          if (exportCell.getColspan > 1) {
            builder ++= "colspan=\"" + exportCell.getColspan + "\""
          }

          if (exportCell.getRowspan > 1 || exportCell.getColspan > 1) {
            builder ++= " |"
          }

          if (exportRawContent) {
            builder ++= exportCell.getRawContent
          } else {
            builder ++= exportCell.getContent
          }

          builder ++= "\n"
        }

      }
    }

    builder ++= "|}" //  end table

    builder.toString()
  }

}
