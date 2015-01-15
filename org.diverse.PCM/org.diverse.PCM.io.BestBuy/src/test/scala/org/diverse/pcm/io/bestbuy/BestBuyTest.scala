package org.diverse.pcm.io.bestbuy

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class BestBuyTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val api = new BestBuyAPI

  "The BestBuy miner" should "list the products SKU" in {

    val skus = api.listProductsSKU(Some("Laptop_Computers"), pageSize=100)
    for (sku <- skus) {
      println(sku)
      val info = api.getProductInfo(sku)
      println(info.details("System Memory (RAM)"))
    }

  }

  it should "get the info on a product" in {
    val info = api.getProductInfo("9925379")

    println(info)
  }


}