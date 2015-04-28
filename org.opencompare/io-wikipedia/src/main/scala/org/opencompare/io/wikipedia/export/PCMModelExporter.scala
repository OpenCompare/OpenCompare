package org.opencompare.io.wikipedia.export

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.{AbstractFeature, Feature, FeatureGroup, PCM}
import org.opencompare.io.wikipedia.WikipediaPageMiner
import org.opencompare.io.wikipedia.pcm.{Cell, Matrix, Page}

/**
 * Created by gbecan on 19/11/14.
 */
class PCMModelExporter {

  private val factory = new PCMFactoryImpl
  private val miner = new WikipediaPageMiner

  def export(page : Page) : List[PCM] = {
    toPCM(page)
  }


  private def toPCM(page : Page) : List[PCM] = {

    for (matrix <- page.getMatrices) yield {
      val pcm = factory.createPCM()

      pcm.setName(matrix.name)

      // Get number of rows (resp. columns) for features (resp. products)
      val nbFeatureRows = nFirstLine(matrix, 1).map(_.rowspan).max
      val nbProductColumns = nFirstColumns(matrix, 1).map(_.colspan).max


      // Detect holes in the matrix and add a cell if necessary
      miner.fillMissingCells(matrix)

      // Extract features
      val features = extractFeatures(matrix, pcm, nbFeatureRows)

      // Normalize matrix (remove row/colspan + add empty cell in matrix' hole)
      val normalizedMatrix = miner.normalize(matrix)

      // Extract products and cells
      extractProducts(normalizedMatrix, pcm, nbFeatureRows, nbProductColumns, features)

      pcm
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
   * @param pcm
   * @param nbFeatureRows
   */
  def extractFeatures(matrix : Matrix, pcm : PCM, nbFeatureRows : Int): Map[(Int, Int), AbstractFeature] = {

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
        pcm.addFeature(feature)

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
            pcm.addFeature(featureGroup)
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
   * @param pcm
   * @param nbFeatureRows
   * @param nbProductColumns
   */
  def extractProducts(matrix : Matrix, pcm : PCM, nbFeatureRows : Int, nbProductColumns : Int, features : Map[(Int, Int), AbstractFeature]): Unit = {
    for (r <- nbFeatureRows until matrix.getNumberOfRows()) {
      // Get product name
      val productName = (for (c <- 0 until nbProductColumns) yield {
        matrix.getCell(r, c).get.content
      }).mkString(".")

      // Create product
      val product = factory.createProduct()
      pcm.addProduct(product)
      product.setName(productName)

      // Create cells
      for (c <- nbProductColumns until matrix.getNumberOfColumns())  {
        val content = matrix.getCell(r, c).get.content

        val cell = factory.createCell()
        cell.setContent(content)
        product.addCell(cell)

        val feature = features(nbFeatureRows - 1, c)
//        println(feature.getName)
        cell.setFeature(feature.asInstanceOf[Feature])
      }
    }
  }

}
