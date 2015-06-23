package org.opencompare.io.bestbuy

import scala.xml.{XML, Elem}
import scalaj.http.{HttpException, Http, HttpOptions}

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
    try {
      XML.loadString(Http(url)
        .option(HttpOptions.connTimeout(10000))
        .option(HttpOptions.readTimeout(20000))
        .asString.body)
    } catch {
      case e : HttpException => callAPI(url)
    }

  }

  def listProductsSKU(categoryName : Option[String] = None, page : Int = 1, pageSize : Int = 10) : List[String] = {

    // Create REST request
    var url = apiURL + "products"

    if (categoryName.isDefined) {
      url += "(categoryPath.name=" + categoryName.get.replaceAll(" ", "%20") + "&marketplace=*)"
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
    val url = apiURL + "products/" + sku + ".xml?show=all&" + apiKey
    val result = callAPI(url)

    val productInfo = new ProductInfo

    productInfo.sku = sku

    productInfo.completeXMLDescription = result

    productInfo.name = (result.\("name")).text

    productInfo.longDescription = (result.\("longDescription")).text

    for (feature <- result.\("features").\("feature")) {
      productInfo.addFeature(feature.text)
    }

    for (detail <- result.\("details").\("detail")) {
      val name = (detail \\ "name").text
      val value = (detail \\ "value").text
      productInfo.addDetail(name, value)
    }

    productInfo
  }
}