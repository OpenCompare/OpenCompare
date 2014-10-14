package org.diverse.pcm.io.wikipedia.pcm

import org.diverse.pcm.api.java.{FeatureGroup, AbstractFeature, Feature, PCM}
import org.diverse.pcm.api.java.impl.PCMFactoryImpl
import scala.collection.JavaConversions._

/**
 * Created by gbecan on 13/10/14.
 */
class WikipediaPageMiner {

  private val factory = new PCMFactoryImpl

  def toPCM(page : Page) : PCM = {
    val pcm = factory.createPCM()

    for (matrix <- page.getMatrices) {
      // Get number of rows (resp. columns) for features (resp. products)
      val nbFeatureRows = nFirstLine(matrix, 1).map(_.rowspan).max
      val nbProductColumns = nFirstColumns(matrix, 1).map(_.colspan).max

      println((nbFeatureRows, nbProductColumns))

      // Normalize matrix (remove row/colspan + add empty cell in matrix' hole)
      val normalizedMatrix = normalize(matrix)

      // Extract features
      val features = extractFeatures(normalizedMatrix, pcm, nbFeatureRows)
      features.values.foreach{ f => println(f.getName + " " + f.getClass)}

      // Extract products and cells
      extractProducts(normalizedMatrix, pcm, nbFeatureRows, nbProductColumns, features)
    }

    pcm
  }

  def nFirstLine(matrix : Matrix, n : Int) : List[Cell] = {
    matrix.cells.filter(_._1._1 < n).map(_._2).toList
  }

  def nFirstColumns(matrix : Matrix, n : Int) : List[Cell] = {
    matrix.cells.filter(_._1._2 < n).map(_._2).toList
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

          val duplicate = new Cell(cell.content, cell.isHeader, row, 1, column, 1)
          normalizedMatrix.setCell(duplicate, row, column)
      }
    }

    // Detect holes in the matrix and add a cell if necessary
    for (row <- 0 until normalizedMatrix.getNumberOfRows(); column <- 0 until normalizedMatrix.getNumberOfColumns()) {
      if (!normalizedMatrix.getCell(row, column).isDefined) {
        val emptyCell = new Cell("", false, row, 1, column, 1)
        normalizedMatrix.setCell(emptyCell, row, column)
      }
    }

    normalizedMatrix
  }

  /**
   * Extract features from a normalized matrix
   * @param matrix
   * @param pcm
   * @param nbFeatureRows
   */
  def extractFeatures(matrix : Matrix, pcm : PCM, nbFeatureRows : Int): Map[String, AbstractFeature] = {

    var features = Map.empty[String, AbstractFeature]

    for (c <- 1 until matrix.getNumberOfColumns()) {

      // Extract features
      var previous : AbstractFeature = {
        val cell = matrix.getCell(nbFeatureRows - 1, c)
        val featureName = cell.get.content

        if (features.contains(featureName)) {
          features(featureName)
        } else {
          val feature = factory.createFeature()
          feature.setName(featureName)
          pcm.addFeature(feature)
          features += featureName -> feature
          feature
        }
      }


      // Extract feature groups
      for (r <- (0 until nbFeatureRows - 1).reverse) {
        val cell = matrix.getCell(r, c)
        val groupName = cell.get.content
        if (groupName != previous.getName) {
          previous = if (features.contains(groupName)) {
            val featureGroup = features(groupName).asInstanceOf[FeatureGroup]
            featureGroup.addFeature(previous)
            featureGroup
          } else {
            val featureGroup = factory.createFeatureGroup()
            featureGroup.setName(groupName)
            pcm.addFeature(featureGroup)
            featureGroup.addFeature(previous)
            features += groupName -> featureGroup
            featureGroup
          }
        }
      }

    }

    features
  }

  /**
   * Extract products and cells from a normalized matrix
   * @param matrix
   * @param pcm
   * @param nbFeatureRows
   * @param nbProductColumns
   */
  def extractProducts(matrix : Matrix, pcm : PCM, nbFeatureRows : Int, nbProductColumns : Int, features : Map[String, AbstractFeature]): Unit = {
    for (r <- nbFeatureRows until matrix.getNumberOfRows()) {
      // Get product name
      val productName = for (c <- 0 until nbProductColumns) yield {
        matrix.getCell(r, c).get.content
      }

      // Create product
      val product = factory.createProduct()
      pcm.addProduct(product)
      product.setName(productName.mkString("."))

      // Create cells
      for (c <- nbProductColumns until matrix.getNumberOfColumns())  {
        val content = matrix.getCell(r, c).get.content

        val cell = factory.createCell()
        cell.setContent(content)
        product.addCell(cell)

        val featureName = matrix.getCell(nbFeatureRows - 1, c).get.content
        println(featureName)
        val feature = features(featureName).asInstanceOf[Feature]
        cell.setFeature(feature)
      }
     }
  }
}
