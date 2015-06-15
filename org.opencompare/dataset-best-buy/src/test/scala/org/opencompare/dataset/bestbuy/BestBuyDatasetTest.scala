package org.opencompare.dataset.bestbuy

import java.io.{File, FileWriter}

import com.github.tototoshi.csv.CSVWriter
import org.opencompare.api.java.PCMContainer
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import org.opencompare.api.java.io.{CSVExporter, HTMLExporter}
import org.opencompare.io.bestbuy.{BestBuyMiner, BestBuyAPI}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 2/23/15.
 */
class BestBuyDatasetTest extends FlatSpec with Matchers {

  val api = new BestBuyAPI
  //val categories = List("Laptops", "Washing Machines", "Digital SLR Cameras", "Refrigerators", "TVs", "No-Contract Phones", "All Printers", "Dishwashers", "Ranges")
  val categories = List("Laptops")
  val baseOutputDirPath = "bestbuy-dataset/"


  def writeToFile(path : String, content : String) = {
    val writer = new FileWriter(path)
    writer.write(content)
    writer.close()
  }

  ignore should "generate a dataset of product descriptions" in { // "BestBuy API"

    val miner = new BestBuyMiner(new PCMFactoryImpl)

    for (category <- categories) {
      println(category)

      val outputDirPath = baseOutputDirPath + category + "/"

      val skus = (for (i <- 1 to 20) yield {
        api.listProductsSKU(Some(category), page=i, pageSize=50)
      }).flatten.toList

      val outputDir = new File(outputDirPath)
      outputDir.mkdirs()


      val productInfos = for (sku <- skus) yield {
        val productInfo = api.getProductInfo(sku)

        // Information (XML dump)
        writeToFile(outputDirPath + sku + ".xml", productInfo.completeXMLDescription.toString())

        // Overview
        val text = new StringBuilder
        text.append(productInfo.longDescription + "\n")

        for (feature <- productInfo.features) {
          text.append(feature.replaceAll("\n", ". ") + "\n")

        }

        writeToFile(outputDirPath + sku + ".txt", text.toString())

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
      val mergedSpecificationsContainer = new PCMContainer(mergedSpecifications)

      // Export to several formats
      val jsonExporter = new KMFJSONExporter
      val json = jsonExporter.export(mergedSpecificationsContainer)
      writeToFile(outputDirPath +  category + ".pcm", json)

      val htmlExporter = new HTMLExporter
      val html = htmlExporter.export(mergedSpecificationsContainer)
      writeToFile(outputDirPath +  category + ".html", html)

      val csvExporter = new CSVExporter
      val csv = csvExporter.export(mergedSpecificationsContainer)
      writeToFile(outputDirPath +  category + ".csv", csv)

    }

  }

  "BestBuy analysis" should "compute stats on dataset" in {
    for (category <- categories) {
      val pcmFile = new File(baseOutputDirPath + category + "/" + category + ".pcm")

      if (pcmFile.exists()) {
        val loader = new KMFJSONLoader
        val pcmContainer = loader.load(pcmFile)(0)
        val pcm = pcmContainer.getPcm

        val csvExporter = new CSVExporter
        val csv = csvExporter.export(pcmContainer)

        val csvWriter = new FileWriter(baseOutputDirPath +  category + "/" + category + ".csv")
        csvWriter.write(csv)
        csvWriter.close()



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

}
