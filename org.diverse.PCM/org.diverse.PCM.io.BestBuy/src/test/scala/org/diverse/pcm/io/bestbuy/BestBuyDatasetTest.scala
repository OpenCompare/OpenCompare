package org.diverse.pcm.io.bestbuy

import java.io.{FileWriter, File}

import com.github.tototoshi.csv.CSVWriter
import org.diverse.pcm.api.java.impl.PCMFactoryImpl
import org.diverse.pcm.api.java.impl.io.{KMFJSONLoader, KMFJSONExporter}
import org.diverse.pcm.api.java.io.HTMLExporter
import org.scalatest.{Matchers, FlatSpec}

import collection.JavaConversions._

/**
 * Created by gbecan on 2/23/15.
 */
class BestBuyDatasetTest extends FlatSpec with Matchers {

  val api = new BestBuyAPI
  val categories = List("Laptops", "Washing Machines", "Digital SLR Cameras", "Refrigerators", "TVs", "No-Contract Phones", "All Printers", "Dishwashers", "Ranges")
  val baseOutputDirPath = "bestbuy-dataset/"

  ignore should "generate a dataset of product descriptions" in { // "BestBuy API"

    val miner = new BestBuyMiner(new PCMFactoryImpl)

    for (category <- categories) {
      println(category)

      val outputDirPath = baseOutputDirPath + category + "/"

      val skus = (for (i <- 1 to 3) yield {
        api.listProductsSKU(Some(category), page=i, pageSize=100)
      }).flatten.toList

      val outputDir = new File(outputDirPath)
      outputDir.mkdirs()


      val productInfos = for (sku <- skus) yield {
        val productInfo = api.getProductInfo(sku)

        // Overview
        val text = new StringBuilder
        text.append(productInfo.longDescription + "\n")

        for (feature <- productInfo.features) {
          text.append(feature.replaceAll("\n", ". ") + "\n")

        }

        val overviewWriter = new FileWriter(outputDirPath + sku + ".txt")
        overviewWriter.write(text.toString())
        overviewWriter.close()

        // Specification

        val spec = productInfo.details.toList
        val features = spec.map(_._1)
        val product = spec.map(_._2)

        val specFile = new File(outputDirPath + sku + ".csv")
        val specWriter = CSVWriter.open(specFile)

        specWriter.writeRow(features)
        specWriter.writeRow(product)

        specWriter.close();

        productInfo
      }

      // Merge specifications
      val mergedSpecifications = miner.mergeSpecifications(productInfos)

      val jsonExporter = new KMFJSONExporter
      val json = jsonExporter.export(mergedSpecifications)

      val htmlExporter = new HTMLExporter
      val html = htmlExporter.export(mergedSpecifications)

      val pcmWriter = new FileWriter(outputDirPath +  category + ".pcm")
      pcmWriter.write(json)
      pcmWriter.close()

      val htmlWriter = new FileWriter(outputDirPath +  category + ".html")
      htmlWriter.write(html)
      htmlWriter.close()

    }

  }

  "BestBuy analysis" should "compute stats on dataset" in {
    for (category <- categories) {
      val pcmFile = new File(baseOutputDirPath + category + "/" + category + ".pcm")

      val loader = new KMFJSONLoader
      val pcm = loader.load(pcmFile)

      val cells = pcm.getProducts.flatMap(_.getCells)
      val nbNA = cells.count(_.getContent == "N/A")

      val featuresWithoutNA = pcm.getConcreteFeatures.filter(f => pcm.getProducts.forall(_.findCell(f).getContent != "N/A"))

      println(category)
      println("products : " + pcm.getProducts.size())
      println("features : " + pcm.getConcreteFeatures.size())

      println("N/A : " + nbNA + " (" + (nbNA * 100) / cells.size  + "%)")
      println("features without NA : " + featuresWithoutNA.size + " (" + (featuresWithoutNA.size * 100) / pcm.getConcreteFeatures.size  + "%)")
      println(featuresWithoutNA.map(_.getName).mkString("[", ", ", "]"))

      println()
    }
  }

}
