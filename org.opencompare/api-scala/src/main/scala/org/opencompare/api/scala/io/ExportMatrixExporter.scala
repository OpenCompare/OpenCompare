package org.opencompare.api.scala.io

import org.opencompare.api.scala.{AbstractFeature, PCM}
import org.opencompare.api.scala.metadata.{Orientation, Positions, ProductsAsColumns}

class ExportMatrixExporter {

  def export(pcm: PCM with Positions with Orientation) : ExportMatrix = {

    val matrix = new ExportMatrix

    matrix.name = pcm.name

    val productsStartRow = exportFeatures(pcm, matrix)

    exportProducts(pcm, matrix, productsStartRow)

    // Transpose matrix if necessary
    pcm.orientation match {
      case ProductsAsColumns() => matrix.transpose()
      case _ =>
    }
    matrix
  }

  def exportFeatures(pcm : PCM with Positions, matrix : ExportMatrix) : Int = {

    var currentFeatureLevel = List.empty[(AbstractFeature, Int, Int)]

    for (feature <- pcm.sortedFeatures()) {
      currentFeatureLevel = currentFeatureLevel :+ (feature, 1, 1)
    }

    var exportCellRows = List.empty[List[ExportCell]]
    var noParents = false

    while (currentFeatureLevel.nonEmpty && !noParents) {
      var nextFeatureLevel = List.empty[(AbstractFeature, Int, Int)]
      var row = List.empty[(AbstractFeature, Int, Int)]

      // Detect if current level of features has at least one parent
      noParents = currentFeatureLevel.forall(l => l._1.parent.isEmpty)

      // Analyze hierarchy of features
      var i = 0
      while (i < currentFeatureLevel.size) {
        val (feature, rowspan, colspan) = currentFeatureLevel(i)

        // Compute colspan
        var newColspan = 1
        while ((i + 1) < currentFeatureLevel.size && (feature == currentFeatureLevel(i + 1)._1)) {
          i += 1
          newColspan += 1
        }

        // Compute rowspan and prepare for next iteration
        val parentGroup = feature.parent

        if (parentGroup.isEmpty) {
          val newRowspan = rowspan + 1
          nextFeatureLevel = nextFeatureLevel :+ (feature, newRowspan, newColspan)
          if (noParents) {
            row = row :+ (feature, rowspan, newColspan)
          }
        } else {
          row = row :+ (feature, rowspan, newColspan)
          nextFeatureLevel = nextFeatureLevel :+ (parentGroup.get, 1, 1)
        }

        i += 1
      }

      // Create cells
      val exportCellRow = for ((feature, rowspan, colspan) <- row) yield {
        val exportCell = new ExportCell(feature.name, feature.name, rowspan, colspan)
        exportCell.feature = true
        exportCell.inProductsKeyColumn = false
        exportCell
      }

      exportCellRows = exportCellRows :+ exportCellRow

      currentFeatureLevel = nextFeatureLevel
    }

    // Add rows to table
    for ((exportCellRow, row) <- exportCellRows.reverse.zipWithIndex) {
      var column = 0
      while (matrix.isPositionOccupied(row, column)) {
        column += 1
      }

      for (exportCell <- exportCellRow) {
        matrix.setCell(exportCell, row, column)
        column += exportCell.colspan
      }
    }

    exportCellRows.size
  }

  def exportProducts(pcm : PCM with Positions, matrix : ExportMatrix, productsStartRow : Int): Unit = {
    var row = productsStartRow
    for (product <- pcm.sortedProducts()) {
      var column = 0

      for (feature <- pcm.sortedFeatures()) {
        val cellOpt = product.findCell(feature)

        cellOpt.foreach { cell =>
          val exportCell = new ExportCell(cell.content, cell.rawContent)
          exportCell.feature = false
          exportCell.inProductsKeyColumn = pcm.productsKey == feature
          matrix.setCell(exportCell, row, column)
        }

        column += 1
      }

      row += 1
    }
  }

}
