package org.opencompare.io.wikipedia.export

import java.util
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java._
import org.opencompare.io.wikipedia.io.WikiTextLoader
import org.opencompare.io.wikipedia.pcm.{Cell, Matrix, Page}
import scala.collection.JavaConversions._

/**
 * Created by gbecan on 19/11/14.
 */
class PCMModelExporter {

  private val factory = new PCMFactoryImpl

  def export(page : Page) : util.List[PCMContainer] = {
    seqAsJavaList(toPCM(page).toSeq)
  }

  private def toPCM(page : Page) : List[PCMContainer] = {

    for (matrix <- page.getMatrices) yield {
      val pcm = factory.createPCM()
      val container = new PCMContainer()
      val metadata = new PCMMetadata(pcm)
      container.setPcm(pcm)
      container.setMetadata(metadata)

      pcm.setName(matrix.name)

      if (!nFirstLine(matrix, 1).isEmpty) {
        // Get number of rows (resp. columns) for features (resp. products)
        val nbFeatureRows = nFirstLine(matrix, 1).map(_.rowspan).max
        val nbProductColumns = nFirstColumns(matrix, 1).map(_.colspan).max


        // Detect holes in the matrix and add a cell if necessary
        fillMissingCells(matrix)

        // Extract features
        val features = extractFeatures(matrix, container, nbFeatureRows)

        // Normalize matrix (remove row/colspan + add empty cell in matrix' hole)
        val normalizedMatrix = normalize(matrix)

        // Extract products and cells
        extractProducts(normalizedMatrix, container, nbFeatureRows, nbProductColumns, features)
      }

      container
    }

  }

  def nFirstLine(matrix : Matrix, n : Int) : List[Cell] = {
    matrix.cells.filter(_._1._1 < n).map(_._2).toList
  }

  def nFirstColumns(matrix : Matrix, n : Int) : List[Cell] = {
    matrix.cells.filter(_._1._2 < n).map(_._2).toList
  }

  /**
   * Extract features from a normalized matrix
   * @param matrix
   * @param container
   * @param nbFeatureRows
   */
  def extractFeatures(matrix : Matrix, container : PCMContainer, nbFeatureRows : Int): Map[(Int, Int), AbstractFeature] = {

    var cellToAFeatures = Map.empty[(Int, Int), AbstractFeature]

    for (column <- 1 until matrix.getNumberOfColumns()) {

      // Extract features
      var previous : AbstractFeature = {

        // Get cell
        val row = nbFeatureRows - 1
        val cell = matrix.getCell(row, column).get

        // Create new feature
        val feature = factory.createFeature()
        feature.setName(cell.content)
        container.getPcm.addFeature(feature)
        container.getMetadata.setFeaturePosition(feature, column)

        // Map cells to feature
        for (i <- cell.row until cell.row + cell.rowspan;
          j <- cell.column until cell.column + cell.colspan
        ) {
          cellToAFeatures += ((i, j) -> feature)
        }

        feature
      }


      // Extract feature groups
      for (row <- (0 until nbFeatureRows - 1).reverse) {

        val cell = matrix.getCell(row, column).get


        if (cell.content != previous.getName) {

          val aFeatureInCell = cellToAFeatures.get(row, column)

          previous = if (aFeatureInCell.isDefined) {
            val featureGroup = aFeatureInCell.get.asInstanceOf[FeatureGroup]
            featureGroup.addFeature(previous)
            featureGroup
          } else {

            // Create new feature group
            val featureGroup = factory.createFeatureGroup()
            featureGroup.setName(cell.content)
            container.getPcm.addFeature(featureGroup)
            featureGroup.addFeature(previous)

            // Map cells to feature
            for (i <- cell.row until cell.row + cell.rowspan;
                 j <- cell.column until cell.column + cell.colspan
            ) {
              cellToAFeatures += ((i, j) -> featureGroup)
            }

            featureGroup
          }

        }


//        if (groupName != previous.getName) {
//          previous = if (cellToAFeatures.contains(groupName)) {
//            val featureGroup = cellToAFeatures(groupName).asInstanceOf[FeatureGroup]
//            featureGroup.addFeature(previous)
//            featureGroup
//          } else {
//            val featureGroup = factory.createFeatureGroup()
//            featureGroup.setName(groupName)
//            pcm.addFeature(featureGroup)
//            featureGroup.addFeature(previous)
//            cellToAFeatures += groupName -> featureGroup
//            featureGroup
//          }
//        }

      }

    }

    cellToAFeatures
  }

  /**
   * Extract products and cells from a normalized matrix
   * @param matrix
   * @param container
   * @param nbFeatureRows
   * @param nbProductColumns
   */
  def extractProducts(matrix : Matrix, container : PCMContainer, nbFeatureRows : Int, nbProductColumns : Int, features : Map[(Int, Int), AbstractFeature]): Unit = {
    for (r <- nbFeatureRows until matrix.getNumberOfRows()) {
      // Get product name
      val productName = (for (c <- 0 until nbProductColumns) yield {
        matrix.getCell(r, c).get.content
      }).mkString(".")

      // Create product
      val product = factory.createProduct()
      container.getPcm.addProduct(product)
      product.setName(productName)

      // Create cells
      for (c <- nbProductColumns until matrix.getNumberOfColumns())  {
        val extractedCell = matrix.getCell(r, c).get
        val content = extractedCell.content
        val rawContent = extractedCell.rawContent

        val cell = factory.createCell()
        cell.setContent(content)
        cell.setRawContent(rawContent)
        product.addCell(cell)

        val feature = features(nbFeatureRows - 1, c)
//        println(feature.getName)
        cell.setFeature(feature.asInstanceOf[Feature])
      }

      container.getMetadata.setProductPosition(product, r)
    }
  }

  /**
   * Normalize a matrix
   * @param matrix
   * @return
   */
  def normalize(matrix : Matrix) : Matrix = {
    // Duplicate cells with rowspan or colspan
    val normalizedMatrix = new Matrix

    for (cell <- matrix.cells.map(_._2)) {
      for (
        rowShift <- 0 until cell.rowspan;
        columnShift <- 0 until cell.colspan
      ) {

        val row = cell.row + rowShift
        val column = cell.column + columnShift

        val duplicate = new Cell(cell.content, cell.rawContent, cell.isHeader, row, 1, column, 1)
        normalizedMatrix.setCell(duplicate, row, column)
      }
    }

    fillMissingCells(normalizedMatrix)

    normalizedMatrix
  }

  /**
   * Detect holes in the matrix and add a cell if necessary
   * @param matrix
   */
  def fillMissingCells(matrix : Matrix) {

    for (row <- 0 until matrix.getNumberOfRows(); column <- 0 until matrix.getNumberOfColumns()) {
      if (!matrix.getCell(row, column).isDefined) {
        val emptyCell = new Cell("", "", false, row, 1, column, 1)
        matrix.setCell(emptyCell, row, column)
      }
    }
  }

}
