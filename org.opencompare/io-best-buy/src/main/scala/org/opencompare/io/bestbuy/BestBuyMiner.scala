package org.opencompare.io.bestbuy

import org.opencompare.api.java.{PCM, PCMFactory}

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
    val pcm = factory.createPCM()

    // Create products key feature
    val skuFeature = factory.createFeature()
    pcm.addFeature(skuFeature)
    pcm.setProductsKey(skuFeature)

    for (productInfo <- productInfos) {
      val product = factory.createProduct()
      pcm.addProduct(product)

      val productName = productInfo.sku
      val skuCell = factory.createCell()
      skuCell.setFeature(skuFeature)
      skuCell.setContent(productName)

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
