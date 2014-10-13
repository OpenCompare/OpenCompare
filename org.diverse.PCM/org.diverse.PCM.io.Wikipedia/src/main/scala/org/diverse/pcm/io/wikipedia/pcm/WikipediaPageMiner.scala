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
      extractFeatures(matrix, pcm, nbFeatureRows)
      pcm.getFeatures.foreach{ f =>
        println(f.getName + " " + f.getClass)
        f match {
          case g : FeatureGroup => g.getFeatures.foreach(sf => println(sf.getName + " " + sf.getClass))
          case _ =>
        }
      }

      // Extract products
      extractProducts(matrix, pcm, nbProductColumns)


      // Extract cells

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
    // TODO : normalize matrix
    matrix
  }

  /**
   * Extract features from a normalized matrix
   * @param matrix
   * @param pcm
   * @param nbFeatureRows
   */
  def extractFeatures(matrix : Matrix, pcm : PCM, nbFeatureRows : Int): Unit = {

    val features = collection.mutable.Map.empty[String, AbstractFeature]

    for (c <- 0 until matrix.getNumberOfColumns()) {

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
  }

  def extractProducts(matrix : Matrix, pcm : PCM, nbProductColumns : Int): Unit = {
    for (r <- 0 until matrix.getNumberOfRows()) {
      val productName = for (c <- 0 until nbProductColumns) yield {
        matrix.getCell(r, c).get.content
      }

      val product = factory.createProduct()
      pcm.addProduct(product)
      product.setName(productName.mkString("."))

      for (c <- nbProductColumns until matrix.getNumberOfColumns())  {
        val content = matrix.getCell(r, c).get.content

        val cell = factory.createCell()
        cell.setContent(content)
        product.addCell(cell)

        // TODO : assign feature
      }
     }
  }
}
