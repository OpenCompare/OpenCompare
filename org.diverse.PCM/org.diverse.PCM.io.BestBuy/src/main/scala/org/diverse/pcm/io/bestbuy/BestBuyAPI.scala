package org.diverse.pcm.io.bestbuy

import scala.xml.Elem
import scalaj.http.{Http, HttpOptions}

class BestBuyAPI {

  val apiURL = "http://api.remix.bestbuy.com/v1/"
  val apiKey = "apiKey=ye723adnkr5x3qafvvvgc7r3"

  def getProducts() : List[String] = {
    val result = Http(apiURL + "products?format=xml&" + apiKey)
      .option(HttpOptions.connTimeout(1000))
      .option(HttpOptions.readTimeout(30000))
      .asXml

    val skus = result \\ "sku"

    for (sku <- skus.toList) yield {
      sku.text
    }

  }


  def getProductInfo(sku : String) : ProductInfo = {

    // Show details (specification), long description and features (detailed textual description)
    val result = Http(apiURL + "products/" + sku + ".xml?show=details%2Cfeatures%2ClongDescription&" + apiKey).asXml

    val productInfo = new ProductInfo

    productInfo.setLongDescription((result \\ "longDescription").text)

    for (feature <- result \\ "feature") {
      productInfo.addFeature(feature.text)
    }

    for (detail <- result \\ "detail") {
      val name = (detail \\ "name").text
      val value = (detail \\ "value").text
      productInfo.addDetail(name, value)
    }



    productInfo
  }
}