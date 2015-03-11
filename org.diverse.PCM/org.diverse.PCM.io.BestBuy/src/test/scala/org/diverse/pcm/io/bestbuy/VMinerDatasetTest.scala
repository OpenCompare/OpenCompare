package org.diverse.pcm.io.bestbuy

import java.io.{FileWriter, File}

import org.scalatest.{Matchers, FlatSpec}


/**
 * Created by gbecan on 2/23/15.
 */
class VMinerDatasetTest extends FlatSpec with Matchers {

  val api = new BestBuyAPI


  //"BestBuy API"
  ignore should "generate a dataset of product descriptions" in {

    val categories = List("Laptops", "Washing Machines", "Digital SLR Cameras", "Refrigerators", "TVs", "Cell Phones", "All Printers", "Dishwashers", "Ranges")

    for (category <- categories) {
      val outputDirPath = "vminer-dataset/" + category + "/"

      val skus = api.listProductsSKU(Some(category), pageSize=50)

      val outputDir = new File(outputDirPath)
      outputDir.mkdirs()


      for (sku <- skus) {
        val productInfo = api.getProductInfo(sku)

        val text = new StringBuilder

        text.append(productInfo.longDescription + "\n")

        for (feature <- productInfo.features) {
          text.append(feature.replaceAll("\n", ". ") + "\n")

        }

        val writer = new FileWriter(outputDirPath + sku + ".txt")
        writer.write(text.toString())
        writer.close()
      }
    }

  }
}
