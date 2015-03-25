package org.diverse.pcm.io.bestbuy

import scala.collection.JavaConversions._
import org.diverse.pcm.api.java.{Feature, PCMFactory, PCM}

/**
 * Created by gbecan on 09/02/15.
 */
class BestBuyMiner(factory : PCMFactory) {

  private val bestBuyAPI = new BestBuyAPI

  def minePCM(skus : List[String]) : PCM = {
    // Create product, features and cells
    val productInfos = for (sku <- skus) yield {
      bestBuyAPI.getProductInfo(sku)
    }

    mergeSpecifications(productInfos)
  }

  def mergeSpecifications(productInfos : List[ProductInfo]): PCM = {
    val pcm = factory.createPCM();

    for (productInfo <- productInfos) {
      val productName = productInfo.name
      val product = factory.createProduct()
      product.setName(productName)
      pcm.addProduct(product)

      for ((featureName, value) <- productInfo.details) {
        val feature = pcm.getOrCreateFeature(featureName, factory)
        val cell = factory.createCell()
        cell.setContent(value)
        cell.setFeature(feature)
        product.addCell(cell)
      }
    }

    pcm.normalize(factory)

    pcm
  }

}
