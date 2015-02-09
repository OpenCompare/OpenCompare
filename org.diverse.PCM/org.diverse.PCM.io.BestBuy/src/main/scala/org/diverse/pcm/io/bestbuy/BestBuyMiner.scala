package org.diverse.pcm.io.bestbuy

import scala.collection.JavaConversions._
import org.diverse.pcm.api.java.{Feature, PCMFactory, PCM}

/**
 * Created by gbecan on 09/02/15.
 */
class BestBuyMiner(factory : PCMFactory) {

  private val bestBuyAPI = new BestBuyAPI

  def minePCM(skus : List[String]) : PCM = {
    val pcm = factory.createPCM();

    // Create product, features and cells
    for (sku <- skus) {
      val productInfo = bestBuyAPI.getProductInfo(sku)

      val productName = productInfo.name
      val product = factory.createProduct()
      product.setName(productName)
      pcm.addProduct(product)

      for ((featureName, value) <- productInfo.details) {
        val feature = getFeature(pcm, featureName)
        val cell = factory.createCell()
        cell.setContent(value)
        cell.setFeature(feature)
        product.addCell(cell)
      }
    }

    // Normalize PCM
    for (product <- pcm.getProducts) {
      for (aFeature <- pcm.getFeatures) {
        aFeature match {
          case feature : Feature =>
            if (!Option(product.findCell(feature)).isDefined) {
              val cell = factory.createCell()
              cell.setFeature(feature)
              cell.setContent("N/A")
              cell.setInterpretation(factory.createNotAvailable())
              product.addCell(cell)
            }
          case _ =>
        }


      }
    }



    pcm
  }

  /**
   * Get a feature in a PCM or create if it does not exist
   * @param pcm
   * @param featureName
   * @return
   */
  private def getFeature(pcm : PCM, featureName: String) : Feature = {
    val searchFeature = pcm.getFeatures.find(_.getName == featureName)
    val feature = if (!searchFeature.isDefined) {
      val newFeature = factory.createFeature()
      newFeature.setName(featureName)
      newFeature
    } else {
      searchFeature.get.asInstanceOf[Feature]
    }

    pcm.addFeature(feature)
    feature
  }

}
