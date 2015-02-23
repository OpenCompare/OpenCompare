package org.diverse.pcm.io.bestbuy

import org.diverse.pcm.api.java.impl.PCMFactoryImpl
import org.diverse.pcm.api.java.io.HTMLExporter
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scalaj.http.HttpException

class BestBuyTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val api = new BestBuyAPI

  "The BestBuy API" should "list the products SKU" in {

    val skus = api.listProductsSKU(Some("Laptops"), pageSize=10)
    skus.isEmpty should be (false)

  }

  it should "get the info on a product" in {
    val info = api.getProductInfo("9925379")
    info.name shouldNot be ("")
  }


  "The BestBuy miner" should "generate a PCM from a list of product's SKU" in {
    val skus = List("8827378", "8790174", "8274088")
    val miner = new BestBuyMiner(new PCMFactoryImpl)
    val pcm = miner.minePCM(skus)

    val exporter = new HTMLExporter
    println(exporter.export(pcm))

    pcm.getProducts.size() should be (skus.size)
    pcm.getFeatures.size() shouldNot be (0)
    pcm.isValid should be (true)
  }


}