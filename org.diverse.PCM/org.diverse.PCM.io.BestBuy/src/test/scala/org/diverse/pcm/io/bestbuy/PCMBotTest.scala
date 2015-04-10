package org.diverse.pcm.io.bestbuy

import java.io.{FileWriter, FilenameFilter, File}

import org.diverse.pcm.api.java.impl.PCMFactoryImpl
import org.diverse.pcm.api.java.impl.io.KMFJSONLoader
import org.diverse.pcm.api.java.io.{CSVExporter, CSVLoader}
import org.diverse.pcm.io.bestbuy.filters._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{Matchers, FlatSpec}

import collection.JavaConversions._
import scala.util.Random

/**
 * Created by gbecan on 4/2/15.
 */
class PCMBotTest extends FlatSpec with Matchers {

  val outputDir = new File("experiment_results")
  outputDir.mkdirs()

  val analyzer = new PCMAnalyzer

  val bestBuyDatasets = Table(
    ("Path to Best Buy dataset"),
    ("bestbuy-dataset/All Printers"),
    ("bestbuy-dataset/Ranges"),
    ("bestbuy-dataset/Refrigerators"),
    ("bestbuy-dataset/Dishwashers"),
    ("bestbuy-dataset/Digital SLR Cameras"),
    ("bestbuy-dataset/TVs"),
    ("bestbuy-dataset/Washing Machines"),
    ("bestbuy-dataset/Laptops"),
    ("bestbuy-dataset/No-Contract Phones")

  )


  val bestbuySpecificationPCMs = Table(
    ("Path to PCM"),
    ("bestbuy-dataset/All Printers/All Printers.pcm"),
    ("bestbuy-dataset/Ranges/Ranges.pcm"),
    ("bestbuy-dataset/Refrigerators/Refrigerators.pcm"),
    ("bestbuy-dataset/Dishwashers/Dishwashers.pcm"),
    ("bestbuy-dataset/Digital SLR Cameras/Digital SLR Cameras.pcm"),
    ("bestbuy-dataset/TVs/TVs.pcm"),
    ("bestbuy-dataset/Washing Machines/Washing Machines.pcm"),
    ("bestbuy-dataset/Laptops/Laptops.pcm"),
    ("bestbuy-dataset/No-Contract Phones/No-Contract Phones.pcm")

  )

  val bestbuyOverviewPCMs = Table(
    ("Path to PCM"),
    ("vminer-dataset-diff/All Printers/Epson/finalPCM.csv"),
    ("vminer-dataset-diff/All Printers/Canon/finalPCM.csv"),
    ("vminer-dataset-diff/All Printers/Brother/finalPCM.csv"),
    ("vminer-dataset-diff/Ranges/Whirlpool/finalPCM.csv"),
    ("vminer-dataset-diff/Ranges/KitchenAid/finalPCM.csv"),
    ("vminer-dataset-diff/Ranges/Frigidaire/finalPCM.csv"),
    ("vminer-dataset-diff/Refrigerators/Whirlpool/finalPCM.csv"),
    ("vminer-dataset-diff/Refrigerators/Samsung/finalPCM.csv"),
    ("vminer-dataset-diff/Refrigerators/GE/GE1/finalPCM.csv"),
    ("vminer-dataset-diff/Refrigerators/GE/GE2/finalPCM.csv"),
    ("vminer-dataset-diff/Camera/Canon/finalPCM.csv"),
    ("vminer-dataset-diff/TVs/Sony/finalPCM.csv"),
    ("vminer-dataset-diff/TVs/LG/finalPCM.csv"),
    ("vminer-dataset-diff/TVs/Samsung/finalPCM.csv"),
    ("vminer-dataset-diff/Washing Machines/LG/finalPCM.csv"),
    ("vminer-dataset-diff/Washing Machines/Samsung/finalPCM.csv"),
    ("vminer-dataset-diff/Washing Machines/GE/finalPCM.csv"),
    ("vminer-dataset-diff/Laptops/Dell/finalPCM.csv"),
    ("vminer-dataset-diff/Laptops/Asus/finalPCM.csv"),
    ("vminer-dataset-diff/Laptops/Hp/finalPCM.csv"),
    ("vminer-dataset-diff/Cell phones/Motorola/finalPCM.csv"),
    ("vminer-dataset-diff/Cell phones/LG/finalPCM.csv"),
    ("vminer-dataset-diff/Cell phones/Samsung/finalPCM.csv")
  )




  "PCMBot experiment" should "run on BestBuy specifications" in {
    forAll (bestbuySpecificationPCMs) { (path : String) =>
      if (new File(path).exists()) {
        val loader = new KMFJSONLoader
        val pcm = loader.load(new File(path))
        val (emptyCells, emptyCellsPerFeature, emptyCellsPerProduct) = analyzer.emptyCells(pcm)
        val (booleanFeature, numericFeatures, textualFeature) = analyzer.featureTypes(pcm)
      }
    }
  }


  it should "run on BestBuy overviews" in {
    forAll (bestbuyOverviewPCMs) { (path : String) =>
      if(new File(path).exists()) {
        val loader = new CSVLoader(new PCMFactoryImpl, ';', '"', false)
        val pcm = loader.load(new File(path))
        val (emptyCells, emptyCellsPerFeature, emptyCellsPerProduct) = analyzer.emptyCells(pcm)
        val (booleanFeature, numericFeatures, textualFeature) = analyzer.featureTypes(pcm)

        println(path)
        println("#cells = " + pcm.getConcreteFeatures.size() * pcm.getProducts.size())
        println("#empty cells = " + emptyCells)
        //println("boolean features = " + booleanFeature)
      }
    }
  }


  def loadDataset(path : String) : (List[String], List[ProductInfo]) = {
    println("dataset = " + path)

    val skus = new File(path).listFiles(new FilenameFilter {
      override def accept(file: File, s: String): Boolean = s.endsWith(".xml")
    }).map(file => file.getName.substring(0, file.getName.length - 4)).toList

    println("#products = " + skus.size)

    // Load products
    val products = for (sku <- skus) yield {
      val loader = new ProductInfoLoader
      val productInfo = loader.load(new File(path + "/" + sku + ".txt"), new File(path + "/" + sku + ".csv"), new File(path + "/" + sku + ".xml"))
      productInfo.sku = sku
      productInfo
    }

    (skus, products)
  }

  def writeToFile(path : String, content : String) = {
    val writer = new FileWriter(path)
    writer.write(content)
    writer.close()
  }

  "Product filters" should "be applied on BestBuy dataset" in {

    forAll (bestBuyDatasets) { (path : String) =>
      if (new File(path).exists()) {
        val (skus, products) = loadDataset(path)

        val filter = new ProductFilter with ManufacturerFilter with PriceFilter with MarketPlaceFilter with CategoryFilter {
          override def manufacturers : Set[String] = Set("HP")

          override def minPrice: Double = 549.99
          override def maxPrice: Double = 549.99

          override def categories: Set[String] = Set("Laptops", "PC Laptops")
        }

        val filteredProducts = filter.select(products)

        println("#remaining products = " + filteredProducts.size)

        println(filteredProducts.map(_.sku))
      }

    }

  }

  it should "generate homogeneous PCMs" in {

    val maxNumberOfProducts = 10
    val miner = new BestBuyMiner(new PCMFactoryImpl)
    val csvExporter = new CSVExporter

    forAll (bestBuyDatasets) { (path: String) =>
      if (new File(path).exists()) {
        val (skus, products) = loadDataset(path)

        val filter = new ProductFilter with ManufacturerFilter with MarketPlaceFilter with RandomFilter {
          override def numberOfProducts: Int = maxNumberOfProducts

          override def manufacturers: Set[String] = Set("HP")
        }

        val filteredProducts = filter.select(products)

        println("#remaining products = " + filteredProducts.size)

        println(filteredProducts.map(_.sku))

        val pcm = miner.mergeSpecifications(filteredProducts)
        val csv = csvExporter.export(pcm)
        val testOutputDir = new File(outputDir.getAbsolutePath + File.separator + path)
        testOutputDir.mkdirs()

        writeToFile(testOutputDir.getAbsolutePath + "/pcm.csv", csv)

        println(analyzer.emptyCells(pcm)._1)

      }
    }
  }

}

