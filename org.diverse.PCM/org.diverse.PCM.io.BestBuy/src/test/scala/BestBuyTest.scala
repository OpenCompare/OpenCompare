import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpec}

import scalaj.http.{HttpOptions, Http}

class BestBuyTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val api = new BestBuyAPI

  "The BestBuy miner" should "list the products" in {

    val products = api.getProducts()
    for (product <- products) {
      println(product)
    }

  }

  it should "get the info on a product" in {
    val info = api.getProductInfo("19914095")
    println(info)
  }


}