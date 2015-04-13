package org.diverse.pcm.io.bestbuy

import org.diverse.pcm.api.java.Product
import org.diverse.pcm.formalizer.clustering.HierarchicalClusterer

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 4/10/15.
 */
class ProductClusterer {


//  var productMap : Map[String, Product] = Map.empty

//  private def calculateProductSimilarityByIntersection(currentProduct: Product, toCompareProduct: Product): Int = {
//    val intersection: Set[String] = getFeatureNamesWithoutEmptyValuesOfAProduct(currentProduct)
//    intersection.intersect(getFeatureNamesWithoutEmptyValuesOfAProduct(toCompareProduct)).size
//  }
//
//  private def getFeatureNamesWithoutEmptyValuesOfAProduct(product: Product): Set[String] = {
//    val setOfFeaturesWithContent: Set[String] = mutable.Set.empty[String]
//    import scala.collection.JavaConversions._
//    for (cell <- product.getCells) if (cell.getContent ne "N/A") setOfFeaturesWithContent.add(cell.getFeature.getName)
//    setOfFeaturesWithContent
//  }

//  def productDissimilarityMetric(p1Name : String, p2Name : String) : Double = {
//
//    val p1 = productMap(p1Name)
//    val p2 = productMap(p2Name)
  def productDissimilarityMetric(p1 : Product, p2 : Product) : Double = {

    val featuresP1 = getFeatureNamesWithoutEmptyValues(p1)
    val featuresP2 = getFeatureNamesWithoutEmptyValues(p2)

    val intersection = featuresP1.intersect(featuresP2)

    -intersection.size.toDouble
  }

  def getFeatureNamesWithoutEmptyValues(product : Product) : Set[String] = {
    product.getCells.filter(_.getContent != "N/A").map(_.getFeature.getName).toSet
  }

  def computeClustersOfProducts(products : List[Product], threshold : Option[Double], maxClusterSize : Option[Int]): List[List[Product]] = {
//    productMap = products.map(p => (p.getName -> p)).toMap
//    val clusterer = new HierarchicalClusterer[String](productDissimilarityMetric, threshold, maxClusterSize)
//    val nameClusters = clusterer.cluster(products.map(_.getName))
//    val clusters = nameClusters.map(c => c.map(name => productMap(name)))
    val clusterer = new HierarchicalClusterer[Product](productDissimilarityMetric, threshold, maxClusterSize)
    clusterer.cluster(products)
  }



}
