package org.diverse.pcm.io.wikipedia.export

import org.diverse.pcm.api.java.impl.PCMFactoryImpl
import org.diverse.pcm.api.java.{AbstractFeature, Feature, FeatureGroup, PCM}
import org.diverse.pcm.io.wikipedia.WikipediaPageMiner
import org.diverse.pcm.io.wikipedia.pcm.{Cell, Matrix, Page}
/**
 * Created by yoannlt on 09/01/15.
 */
class PCMModelExporterOld {


    private val factory = new PCMFactoryImpl
    private val miner = new WikipediaPageMiner

    def export(page : Page) : PCM = {
      toPCM(page)
    }


    private def toPCM(page : Page) : PCM = {
      val pcm = factory.createPCM()

      for (matrix <- page.getMatrices) {
        // Get number of rows (resp. columns) for features (resp. products)
        val nbFeatureRows = nFirstLine(matrix, 1).map(_.rowspan).max
        val nbProductColumns = nFirstColumns(matrix, 1).map(_.colspan).max

        // Normalize matrix (remove row/colspan + add empty cell in matrix' hole)
        val normalizedMatrix = miner.normalize(matrix)

        // Extract features
        val features = extractFeatures(normalizedMatrix, pcm, nbFeatureRows)

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
          val feature = features(featureName).asInstanceOf[Feature]
          cell.setFeature(feature)
        }
      }
    }




}
