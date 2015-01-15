package org.diverse.pcm.io.bestbuy

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class BestBuyTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val api = new BestBuyAPI

  "The BestBuy miner" should "list the products" in {

    val products = api.getProducts()
    for (product <- products) {
      println(product)
    }

  }

  it should "get the info on a product" in {
    val info = api.getProductInfo("9925379")

    println(info)
  }


}