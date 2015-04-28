package org.opencompare.dataset.bestbuy

import org.opencompare.api.java
import org.opencompare.api.java.util.PCMElementComparator
import org.opencompare.api.java.{Cell, Feature, PCM}

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
 * Created by gbecan on 4/2/15.
 */
class PCMAnalyzer {

  /**
   * Diff of two PCMs
   * @param pcm1
   * @param pcm2
   * @return (features from pcm1 present in pcm2, cells from pcm1 present in pcm2)
   */
  def diff(pcm1 : PCM, pcm2 : PCM, comparator : PCMElementComparator) : (List[Feature], List[Cell]) = {
    var featuresInCommon = List.empty[Feature]
    var cellsInCommon = List.empty[Cell]

    for (feature1 <- pcm1.getConcreteFeatures) {
      if (pcm2.getConcreteFeatures.exists(feature2 => comparator.similarFeature(feature1, feature2))) {
        featuresInCommon = feature1 :: featuresInCommon
//        val f2 = pcm2.getConcreteFeatures.find(feature2 => comparator.similarFeature(feature1, feature2)).get
//        println(feature1.getName + " vs " + f2.getName)
      }
    }

    for (product1 <- pcm1.getProducts;
         cell1 <- product1.getCells) {

      val product2 = pcm2.getProducts.find(p => comparator.similarProduct(product1, p))

      if (product2.isDefined) {
        // A similar cell exists in pcm2 for a similar feature
        if (product2.get.getCells.exists(cell2 =>
          comparator.similarCell(cell1, cell2) &&
          comparator.similarFeature(cell1.getFeature, cell2.getFeature))) {

          cellsInCommon = cell1 :: cellsInCommon

        }
      }
    }

    (featuresInCommon, cellsInCommon)
  }

  /**
   * Compute the number of empty cells
   * @param pcm
   * @return number of empty cells in total, per feature and per product
   */
  def emptyCells(pcm : PCM) : (Int, Map[Feature, Int], Map[java.Product, Int]) = {

    var nbEmptyCells = 0
    val nbEmptyCellsPerFeature = mutable.Map.empty[Feature, Int]
    val nbEmptyCellsPerProduct = mutable.Map.empty[java.Product, Int]

    // Init
    for (feature <- pcm.getConcreteFeatures) {
      nbEmptyCellsPerFeature += (feature -> 0)
    }

    for (product <- pcm.getProducts) {
      nbEmptyCellsPerProduct += (product -> 0)
    }


    // Compute number of empty cells
    for (product <- pcm.getProducts) {
      for (cell <- product.getCells) {

        val content = cell.getContent
        val feature = cell.getFeature

        if (isEmpty(content)) {
          nbEmptyCells += 1
          nbEmptyCellsPerFeature += feature -> (nbEmptyCellsPerFeature(feature) + 1)
          nbEmptyCellsPerProduct += product -> (nbEmptyCellsPerProduct(product) + 1)
        }
      }
    }

    (nbEmptyCells, nbEmptyCellsPerFeature.toMap, nbEmptyCellsPerProduct.toMap)
  }


  /**
   * Partition features according to the type of the values they represent
   * @param pcm
   * @return (Boolean, Numeric, Textual)
   */
  def featureTypes(pcm : PCM) : (List[Feature], List[Feature], List[Feature]) = {

    // Init
    var booleanFeatures = List.empty[Feature]
    var numericFeatures = List.empty[Feature]
    var textualFeatures = List.empty[Feature]

    // Extract cells for each feature
    val cellsByFeature = mutable.Map.empty[Feature, List[Cell]]

    for (product <- pcm.getProducts;
         cell <- product.getCells) {
      val cells = cellsByFeature.getOrElse(cell.getFeature, Nil)
      cellsByFeature += cell.getFeature -> (cell :: cells)
    }

    // Detect type of each feature
    for ((feature, cells) <- cellsByFeature) {
      // Detect type of cells
      val cellTypes = cells.flatMap { cell =>
        val content = cell.getContent
        if (isEmpty(content)) {
          None
        } else if (isBoolean(content)) {
          Some("boolean")
        } else if (isNumeric(content)) {
          Some("numeric")
        } else {
          Some("textual")
        }
      }

      // Take the most represented type
      if (cellTypes.nonEmpty) {
        val mainType = cellTypes.groupBy(t => t).map(t => (t._1, t._2.size)).maxBy(_._2)._1
        mainType match {
          case "boolean" => booleanFeatures = feature :: booleanFeatures
          case "numeric" => numericFeatures = feature :: numericFeatures
          case "textual" => textualFeatures = feature :: textualFeatures
        }
      }
    }

    (booleanFeatures, numericFeatures, textualFeatures)
  }


  def isEmpty (content : String) : Boolean = {
    content.toLowerCase == "n/a" || content == ""
  }

  def isBoolean(content : String) : Boolean = {
    content.toLowerCase == "yes" ||
      content.toLowerCase == "no"
  }

  def isNumeric(content : String) : Boolean = {
    content.matches(".*\\d.*")
  }



}
