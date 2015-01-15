import scala.xml.Elem
import scalaj.http.{HttpOptions, Http}

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


  def getProductInfo(sku : String) : Elem = {
    val result = Http(apiURL + "products/16306454.xml?" + apiKey).asXml

    // Show details (specification), long description and features (detailed textual description)
    // http://api.remix.bestbuy.com/v1/products/9925379.xml?show=details%2Cfeatures%2ClongDescription&apiKey=ye723adnkr5x3qafvvvgc7r3

    result
  }
}