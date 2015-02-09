package org.diverse.pcm.io.bestbuy

import scala.xml.Elem
import scalaj.http.{Http, HttpOptions}

class BestBuyAPI {

  val apiURL = "http://api.remix.bestbuy.com/v1/"
  val apiKey = "apiKey=ye723adnkr5x3qafvvvgc7r3"

  var timeLastCall : Long = 0
  var callsInLastSecond : Int = 0

  val maxAPICallsPerSeconds = 4

  def callAPI(url : String) : Elem = {
    // Restrict API usage to at most 5 calls per seconds
    val time = System.currentTimeMillis() / 1000

    if (time > timeLastCall) { // OK
      timeLastCall = time
      callsInLastSecond = 1
    } else if (time == timeLastCall && callsInLastSecond < maxAPICallsPerSeconds) { // OK
      callsInLastSecond += 1
    } else if (time == timeLastCall && callsInLastSecond == maxAPICallsPerSeconds) { // WAIT
      Thread.sleep(1000) // wait 1s
      timeLastCall = System.currentTimeMillis() / 1000
      callsInLastSecond = 1
    }

    // Call API
    Http(url)
      .option(HttpOptions.connTimeout(1000))
      .option(HttpOptions.readTimeout(30000))
      .asXml
  }

  def listProductsSKU(productTemplate : Option[String] = None, page : Int = 1, pageSize : Int = 10) : List[String] = {

    // Create REST request
    var url = apiURL + "products"

    if (productTemplate.isDefined) {
      url += "(productTemplate=" + productTemplate.get + ")"
    }

    url += "?page=" + page + "&pageSize=" + pageSize + "&show=sku&format=xml&" + apiKey

    // Call API
    val result = callAPI(url)

    // Extract SKUs
    val skus = result \\ "sku"

    for (sku <- skus.toList) yield {
      sku.text
    }

  }


  def getProductInfo(sku : String) : ProductInfo = {

    // Show details (specification), long description and features (detailed textual description)
    val url = apiURL + "products/" + sku + ".xml?show=details%2Cfeatures%2ClongDescription&" + apiKey
    val result = callAPI(url)

    val productInfo = new ProductInfo

    productInfo.longDescription = (result \\ "longDescription").text

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