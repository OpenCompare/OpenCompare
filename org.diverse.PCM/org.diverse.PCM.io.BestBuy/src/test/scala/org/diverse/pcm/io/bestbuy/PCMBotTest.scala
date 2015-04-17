package org.diverse.pcm.io.bestbuy

import java.io.{File, FileWriter, FilenameFilter}
import java.nio.file.Files

import ch.usi.inf.sape.hac.agglomeration.SingleLinkage
import com.github.tototoshi.csv.CSVWriter
import org.diverse.pcm.api.java
import org.diverse.pcm.api.java.{AbstractFeature, Cell, PCM}
import org.diverse.pcm.api.java.impl.PCMFactoryImpl
import org.diverse.pcm.api.java.impl.io.KMFJSONLoader
import org.diverse.pcm.api.java.io.{HTMLExporter, CSVExporter, CSVLoader}
import org.diverse.pcm.api.java.util.PCMElementComparator
import org.diverse.pcm.io.bestbuy.filters._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FlatSpec, Matchers}
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein

import scala.collection.JavaConversions._
import scala.io.Source
import scala.util.Random

/**
 * Created by gbecan on 4/2/15.
 */
class PCMBotTest extends FlatSpec with Matchers {

  val outputDir = new File("experiment_results")
  outputDir.mkdirs()

  val analyzer = new PCMAnalyzer
  val factory = new PCMFactoryImpl
  val miner = new BestBuyMiner(factory)
  val csvExporter = new CSVExporter
  val api = new BestBuyAPI

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

  //  val bestbuyOverviewPCMs = Table(
  //    ("Path to PCM"),
  //    ("vminer-dataset-diff/All Printers/Epson/finalPCM.csv"),
  //    ("vminer-dataset-diff/All Printers/Canon/finalPCM.csv"),
  //    ("vminer-dataset-diff/All Printers/Brother/finalPCM.csv"),
  //    ("vminer-dataset-diff/Ranges/Whirlpool/finalPCM.csv"),
  //    ("vminer-dataset-diff/Ranges/KitchenAid/finalPCM.csv"),
  //    ("vminer-dataset-diff/Ranges/Frigidaire/finalPCM.csv"),
  //    ("vminer-dataset-diff/Refrigerators/Whirlpool/finalPCM.csv"),
  //    ("vminer-dataset-diff/Refrigerators/Samsung/finalPCM.csv"),
  //    ("vminer-dataset-diff/Refrigerators/GE/GE1/finalPCM.csv"),
  //    ("vminer-dataset-diff/Refrigerators/GE/GE2/finalPCM.csv"),
  //    ("vminer-dataset-diff/Camera/Canon/finalPCM.csv"),
  //    ("vminer-dataset-diff/TVs/Sony/finalPCM.csv"),
  //    ("vminer-dataset-diff/TVs/LG/finalPCM.csv"),
  //    ("vminer-dataset-diff/TVs/Samsung/finalPCM.csv"),
  //    ("vminer-dataset-diff/Washing Machines/LG/finalPCM.csv"),
  //    ("vminer-dataset-diff/Washing Machines/Samsung/finalPCM.csv"),
  //    ("vminer-dataset-diff/Washing Machines/GE/finalPCM.csv"),
  //    ("vminer-dataset-diff/Laptops/Dell/finalPCM.csv"),
  //    ("vminer-dataset-diff/Laptops/Asus/finalPCM.csv"),
  //    ("vminer-dataset-diff/Laptops/Hp/finalPCM.csv"),
  //    ("vminer-dataset-diff/Cell phones/Motorola/finalPCM.csv"),
  //    ("vminer-dataset-diff/Cell phones/LG/finalPCM.csv"),
  //    ("vminer-dataset-diff/Cell phones/Samsung/finalPCM.csv")
  //  )


  val bestbuyOverviewPCMs = Table(
    "Path to PCM",
    "manual-dataset/All Printers/Filter-Category/Laser Printers/finalPCM.csv",
    "manual-dataset/All Printers/Filter-Category/All-in-one-Printers/finalPCM.csv",
    "manual-dataset/All Printers/Filter-Brand-Category/Brother-Laser-Printers/finalPCM.csv",
    "manual-dataset/All Printers/Filter-Brand-Category/Brother-All-In-One Printers/finalPCM.csv",
    "manual-dataset/All Printers/Filter-Brand-Category/Canon-Laser-Printers/finalPCM.csv",
    "manual-dataset/All Printers/Filter-Brand-Category/Epson-All-In-One Printers/finalPCM.csv",
    "manual-dataset/All Printers/Filter-Brand-Category/Canon-All-In-One Printers/finalPCM.csv",
    "manual-dataset/All Printers/Filter-Brand/Epson/finalPCM.csv",
    "manual-dataset/All Printers/Filter-Brand/Canon/finalPCM.csv",
    "manual-dataset/All Printers/Filter-Brand/Brother/finalPCM.csv"
  )

  ignore should "run on BestBuy specifications" in {
    forAll(bestbuySpecificationPCMs) { (path: String) =>
      if (new File(path).exists()) {
        val loader = new KMFJSONLoader
        val pcm = loader.load(new File(path))
        val (emptyCells, emptyCellsPerFeature, emptyCellsPerProduct) = analyzer.emptyCells(pcm)
        val (booleanFeature, numericFeatures, textualFeature) = analyzer.featureTypes(pcm)
      }
    }
  }


  "toto" should "run on BestBuy overviews" in {
    forAll(bestbuyOverviewPCMs) { (path: String) =>
      if (new File(path).exists()) {
        val loader = new CSVLoader(new PCMFactoryImpl, ';', '"', false)
        val pcm = loader.load(new File(path))
        val (emptyCells, emptyCellsPerFeature, emptyCellsPerProduct) = analyzer.emptyCells(pcm)
        val (booleanFeature, numericFeatures, textualFeature) = analyzer.featureTypes(pcm)

        println(path)
        println("#products = " + pcm.getProducts().size)
        println("#features = " + pcm.getConcreteFeatures().size)
        println("#cells = " + pcm.getConcreteFeatures.size() * pcm.getProducts.size())
        println("#empty cells = " + emptyCells)
        //println("boolean features = " + booleanFeature)
      }
    }
  }


  def loadDataset(path: String): (List[String], List[ProductInfo]) = {
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

  def writeToFile(path: String, content: String) = {
    val writer = new FileWriter(path)
    writer.write(content)
    writer.close()
  }

  ignore should "be applied on BestBuy dataset" in {

    forAll(bestBuyDatasets) { (path: String) =>
      if (new File(path).exists()) {
        val (skus, products) = loadDataset(path)

        val filter = new ProductFilter with ManufacturerFilter with PriceFilter with MarketPlaceFilter with CategoryFilter {
          override def manufacturers: Set[String] = Set("HP")

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

  ignore should "generate homogeneous PCMs" in {

    val maxNumberOfProducts = 10
    val csvExporter = new CSVExporter

    forAll(bestBuyDatasets) { (path: String) =>
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

        //        writeToFile(testOutputDir.getAbsolutePath + "/pcm.csv", csv)

        println(analyzer.emptyCells(pcm)._1)

      }
    }
  }


  def copy(inDir: File, inFile: String, outDir: File) {
    val in = new File(inDir.getAbsolutePath + "/" + inFile)
    val out = new File(outDir.getAbsolutePath + "/" + in.getName)
    Files.copy(in.toPath, out.toPath)
  }

  ignore should "cluster products" in {
    // "PCMBot experiment"
    forAll(bestBuyDatasets) { (path: String) =>
      val datasetDir = new File(path)
      if (datasetDir.exists()) {

        println("loading product infos")
        val (skus, productsInfo) = loadDataset(path)

        println("creating PCM")
        val filter = new ProductFilter with MarketPlaceFilter
        val pcm = miner.mergeSpecifications(filter.select(productsInfo))
        val products = pcm.getProducts.toList



        println("clustering")
        val maxSize = 10
        val clusterer = new ProductClusterer
        val clusters = clusterer.computeClustersOfProducts(products, None, Some(maxSize), None)

        println("output")
        // Debug
        println(clusters.map(_.size).sum)
        println("10 elements clusters = " + clusters.filter(_.size == 10).size)
        println()

        // Create output directory
        val testOutputDir = new File(outputDir.getAbsolutePath + File.separator + "clustering" + File.separator + path)
        testOutputDir.mkdirs()

        // Export global PCM to CSV
        val csv = csvExporter.export(pcm)
        writeToFile(testOutputDir.getAbsolutePath + "/pcm.csv", csv)

        // Export clusters
        for ((cluster, index) <- clusters.zipWithIndex if cluster.size == 10) {
          val outputDirCluster = new File(testOutputDir.getAbsolutePath + "/cluster_" + index)
          outputDirCluster.mkdirs()

          val productList = cluster.map(_.getName).mkString("\n")
          writeToFile(outputDirCluster.getAbsolutePath + "/products.txt", productList)

          val clusterProductInfo = productsInfo.filter(p => cluster.map(_.getName).contains(p.sku))
          val clusterPCM = miner.mergeSpecifications(clusterProductInfo)
          val clusterCSV = csvExporter.export(clusterPCM)
          writeToFile(outputDirCluster.getAbsolutePath + "/spec.csv", clusterCSV)

          for (product <- cluster) {
            val sku = product.getName
            copy(datasetDir, sku + ".txt", outputDirCluster)
            copy(datasetDir, sku + ".csv", outputDirCluster)
            copy(datasetDir, sku + ".xml", outputDirCluster)
          }


        }


      }
    }
  }

  ignore should "randomly select products" in {
    forAll(bestBuyDatasets) { (path: String) =>

      val filter = new ProductFilter with MarketPlaceFilter with RandomFilter {
        override def numberOfProducts: Int = 10
      }

      val datasetDir = new File(path)
      if (datasetDir.exists()) {

        println("loading product infos")
        val (skus, productsInfo) = loadDataset(path)

        println("#filtered products = " + (new ProductFilter with MarketPlaceFilter).select(productsInfo).size)
        println("#filtered products + random = " + filter.select(productsInfo).size)

        // Create output directory
        val testOutputDir = new File(outputDir.getAbsolutePath + File.separator + "random" + File.separator + path)
        testOutputDir.mkdirs()


        for (index <- 1 to 100) {
          val selectedProducts = filter.select(productsInfo)

          val outputDirCluster = new File(testOutputDir.getAbsolutePath + "/random_" + index)
          outputDirCluster.mkdirs()

          val productList = selectedProducts.map(_.sku).mkString("\n")
          writeToFile(outputDirCluster.getAbsolutePath + "/products.txt", productList)

          val clusterPCM = miner.mergeSpecifications(selectedProducts)
          val clusterCSV = csvExporter.export(clusterPCM)
          writeToFile(outputDirCluster.getAbsolutePath + "/spec.csv", clusterCSV)

          for (product <- selectedProducts) {
            val sku = product.sku
            copy(datasetDir, sku + ".txt", outputDirCluster)
            copy(datasetDir, sku + ".csv", outputDirCluster)
            copy(datasetDir, sku + ".xml", outputDirCluster)
          }

        }


      }
    }
  }


  ignore should "count products" in {
    forAll(bestBuyDatasets) { (path: String) =>
      val datasetDir = new File(path)
      if (datasetDir.exists()) {

        println("loading product infos")
        val (skus, productsInfo) = loadDataset(path)

        val filter = new ProductFilter with MarketPlaceFilter
        println("#products = " + filter.select(productsInfo).size)
      }
    }
  }


  "PCMBot experiment" should "compute metrics on manual dataset" in { //"PCMBot experiment"
    val manualDatasetDir = new File("manual-dataset")
    val statsFile = new File("manual-dataset/metrics.csv")
    val statsWriter = CSVWriter.open(statsFile)

    statsWriter.writeRow(Seq(
      "category",
      "filter",
      "name",

      "over #products",
      "over #features",
      "over #N/A",

      "over #boolean",
      "over #numeric",
      "over #textual",

      "over min #N/A per feature",
      "over max #N/A per feature",
      "over median #N/A per feature",
      "over avg #N/A per feature",

      "over min #N/A per product",
      "over max #N/A per product",
      "over median #N/A per product",
      "over avg #N/A per product",

      "spec #products",
      "spec #features",
      "spec #N/A",

      "spec #boolean",
      "spec #numeric",
      "spec #textual",

      "spec min #N/A per feature",
      "spec max #N/A per feature",
      "spec median #N/A per feature",
      "spec avg #N/A per feature",

      "spec min #N/A per product",
      "spec max #N/A per product",
      "spec median #N/A per product",
      "spec avg #N/A per product",

      "diff #features of overview in spec",
      "diff #cells of overview in spec",
      "diff #features of spec in overview",
      "diff #cells of spec in overview"))

    val loader = new CSVLoader(factory, ';', '"', false)

    if (manualDatasetDir.exists()) {
      // Load specifications
      for (categoryDir <- manualDatasetDir.listFiles() if categoryDir.isDirectory) {
        val category = categoryDir.getName

        for (filterDir <- categoryDir.listFiles() if filterDir.isDirectory) {
          val filter = filterDir.getName

          for (pcmDir <- filterDir.listFiles() if pcmDir.isDirectory) {

            var pcmFile = new File(pcmDir.getAbsolutePath + "/finalPCM.csv")

            if (pcmFile.exists()) {
              analyzePCM(statsWriter, loader, category, filter, pcmDir, pcmFile)
            } else {
              // Subfolder
              for (pcmSubDir <- pcmDir.listFiles() if pcmSubDir.isDirectory) {
                pcmFile = new File(pcmSubDir.getAbsolutePath + "/finalPCM.csv")
                analyzePCM(statsWriter, loader, category, filter, pcmSubDir, pcmFile)
              }
            }

          }


        }
      }

      // Generate specification

      // Compute metrics

    }

    statsWriter.close()
  }

  def analyzePCM(statsWriter: CSVWriter, loader: CSVLoader, category: String, filter: String, pcmDir: File, pcmFile: File): Unit = {
    val name = pcmDir.getName
    val overviewPCM = loader.load(pcmFile)
    println(pcmFile.getAbsolutePath.substring(64))
    println("exists? = " + pcmFile.exists())
    val htmlExporter = new HTMLExporter
    writeToFile(pcmDir.getAbsolutePath + "/finalPCM.html", htmlExporter.export(overviewPCM))

    // Stats on overview

    val numberOfProducts = overviewPCM.getProducts.size()
    val numberOfFeatures = overviewPCM.getConcreteFeatures.size()

    val (nas, nasByFeature, nasByProduct) = analyzer.emptyCells(overviewPCM)
    val minNAsByFeature = nasByFeature.map(_._2).min
    val maxNAsByFeature = nasByFeature.map(_._2).max
    val medNAsByFeature = nasByFeature.map(_._2).toList.sorted.get(nasByFeature.size/2)
    val avgNAsByFeature = nasByFeature.map(_._2).sum.toDouble / numberOfFeatures

    val minNAsByProduct = nasByProduct.map(_._2).min
    val maxNAsByProduct = nasByProduct.map(_._2).max
    val medNAsByProduct = nasByProduct.map(_._2).toList.sorted.get(nasByProduct.size/2)
    val avgNAsByProduct = nasByProduct.map(_._2).sum.toDouble / numberOfProducts

    val (booleans, numeric, textual) = analyzer.featureTypes(overviewPCM)

    // Create spec PCM
    val specFiles = pcmDir.listFiles(new FilenameFilter {
      override def accept(file: File, s: String): Boolean = s.endsWith(".csv") && s != "finalPCM.csv" && s != "spec.csv"
    })


    val productInfoLoader = new ProductInfoLoader
    val path = pcmDir.getAbsolutePath

    val productInfos = for (specFile <- specFiles.toList) yield {
      val sku = specFile.getName.substring(0, specFile.getName.length - 4)
      val productInfo = productInfoLoader.load(new File(path + "/" + sku + ".txt"), new File(path + "/" + sku + ".csv"), new File(path + "/" + sku + ".xml"))
      productInfo.sku = sku
      productInfo
    }

    val specPCM = miner.mergeSpecifications(productInfos)

    val specWriter = new CSVExporter
    writeToFile(pcmDir.getAbsolutePath + "/spec.csv", specWriter.export(specPCM))

    // Stats on specification

    val specNumberOfProducts = specPCM.getProducts.size()
    val specNumberOfFeatures = specPCM.getConcreteFeatures.size()

    val (specNAs, specNAsByFeature, specNAsByProduct) = analyzer.emptyCells(specPCM)
    val specMinNAsByFeature = specNAsByFeature.map(_._2).min
    val specMaxNAsByFeature = specNAsByFeature.map(_._2).max
    val specMedNAsByFeature = specNAsByFeature.map(_._2).toList.sorted.get(specNAsByFeature.size/2)
    val specAvgNAsByFeature = specNAsByFeature.map(_._2).sum.toDouble / specNumberOfFeatures

    val specMinNAsByProduct = specNAsByProduct.map(_._2).min
    val specMaxNAsByProduct = specNAsByProduct.map(_._2).max
    val specMedNAsByProduct = specNAsByProduct.map(_._2).toList.sorted.get(specNAsByProduct.size/2)
    val specAvgNAsByProduct = specNAsByProduct.map(_._2).sum.toDouble / specNumberOfProducts

    val (specBooleans, specNumeric, specTextual) = analyzer.featureTypes(specPCM)

    // Diff

    val pcmComparator = new PCMElementComparator {

      val comparator = new Levenshtein

      override def similarFeature(f1: AbstractFeature, f2: AbstractFeature): Boolean = {
        //comparator.getSimilarity(f1.getName, f2.getName) >= 0.5
        val nameF1 = f1.getName.toLowerCase
        val nameF2 = f2.getName.toLowerCase
        nameF1.contains(nameF2) || nameF2.contains(nameF1)
      }
      // Levenshtein 0.6

      override def similarCell(c1: Cell, c2: Cell): Boolean = {
        val contentC1 = c1.getContent.toLowerCase
        val contentC2 = c2.getContent.toLowerCase
        contentC1.contains(contentC2) || contentC2.contains(contentC1)
      }
      // c1 substring c2 || c2 substring c1

      override def similarProduct(p1: java.Product, p2: java.Product): Boolean = p1.getName.contains(p2.getName) || p2.getName.contains(p1.getName)
    }

    val (featuresInCommonOverVSSpec, cellsInCommonOverVSSpec) = analyzer.diff(overviewPCM, specPCM, pcmComparator)
//    println
//    println("------------------")
//    println
    val (featuresInCommonSpecVSOver, cellsInCommonSpecVSOver) = analyzer.diff(specPCM, overviewPCM, pcmComparator)

    // Write stats
    statsWriter.writeRow(Seq(
      category, filter, name,
      // Overview
      numberOfProducts, numberOfFeatures, nas,
      booleans.size, numeric.size, textual.size,

      minNAsByFeature,
      maxNAsByFeature,
      medNAsByFeature,
      avgNAsByFeature,

      minNAsByProduct,
      maxNAsByProduct,
      medNAsByProduct,
      avgNAsByProduct,

      // Spec
      specNumberOfProducts, specNumberOfFeatures, specNAs,
      specBooleans.size, specNumeric.size, specTextual.size,
      specMinNAsByFeature,
      specMaxNAsByFeature,
      specMedNAsByFeature,
      specAvgNAsByFeature,

      specMinNAsByProduct,
      specMaxNAsByProduct,
      specMedNAsByProduct,
      specAvgNAsByProduct,
      // Diff
      featuresInCommonOverVSSpec.size, cellsInCommonOverVSSpec.size, featuresInCommonSpecVSOver.size, cellsInCommonSpecVSOver.size
    ))

  }





  ignore should "find missing specifications in manual dataset" in {
    val list = new File("manual-dataset/notfound_sku.txt")
    val outputDirPath = "manual-dataset/missing/"
    new File(outputDirPath).mkdirs()

    if (list.exists()) {
      val skus = Source.fromFile(list).getLines().toList

      for (sku <- skus) {
        println(sku)
        val productInfo = api.getProductInfo(sku)
        println(productInfo.name)

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

      }
    }
  }



  //"PCMBot experiment" should "compare overview PCM with spec PCM"




  // ---------------------------------------------------------------------------------------------------------------- //



  ignore should "cluster products" in {
    //"AFM Synthesis experiment

    val resultingDatasetDir = new File("afm-synthesis-dataset/dataset")
    resultingDatasetDir.mkdirs()
    val resultingDatasetStatsFile = new File("afm-synthesis-dataset/stats_bestbuy.csv")
    val resultingDatasetStatsWriter = CSVWriter.open(resultingDatasetStatsFile)
    resultingDatasetStatsWriter.writeRow(Seq("id", "size", "percentage of N/A"))

    forAll(bestBuyDatasets) { (path: String) =>
      val datasetDir = new File(path)
      if (datasetDir.exists()) {

        // Load dataset
        println("loading product infos")
        val (skus, productsInfo) = loadDataset(path)

        // Create PCM
        println("creating PCM")
        val pcm = miner.mergeSpecifications(productsInfo)
        val products = pcm.getProducts.toList

        // Filters features that have too much N/As
        // FIXME : the clustering never ends when filtering
        //        val filteredFeatures = analyzer.emptyCells(pcm)._2.filter(_._2 > (0.2 * products.size)).map(_._1).toList // 20% of N/A is too much !
        //
        //        for (product <- products) {
        //          for (cell <- product.getCells) {
        //            if (filteredFeatures.contains(cell.getFeature)) {
        //              product.removeCell(cell)
        //            }
        //          }
        //        }
        //
        //        filteredFeatures.foreach(pcm.removeFeature(_))

        println("valid? = " + pcm.isValid)

        // Clustering
        println("clustering")
        val clusterer = new ProductClusterer

        val threshold = None //Some(0.5)
        val maxClusterSize = None
        val percentageOfNAThreshold = 0.25

        val mergingCondition = Some((c1: List[java.Product], c2: List[java.Product]) => {
          val clusterProductInfo = productsInfo.filter(p => c1.map(_.getName).contains(p.sku) || c2.map(_.getName).contains(p.sku))
          val clusterPCM = miner.mergeSpecifications(clusterProductInfo)
          val percentageOfNA = analyzer.emptyCells(clusterPCM)._1 / (clusterPCM.getConcreteFeatures.size() * clusterPCM.getProducts.size).toDouble
          percentageOfNA <= percentageOfNAThreshold
        })
        val agglomerationMethod = new SingleLinkage

        val clusters = clusterer.computeClustersOfProducts(products, threshold, maxClusterSize, mergingCondition, agglomerationMethod)

        // Print stats
        println("output")
        // Debug
        println(clusters.map(_.size).sum)
        println("number of products per clusters = " + (products.size.toDouble / clusters.size))
        println("max cluster size = " + (clusters.map(_.size).max))
        println()

        // Create output directory
        val testOutputDir = new File("afm-synthesis-dataset/" + path)
        testOutputDir.mkdirs()

        // Export global PCM to CSV
        val csv = csvExporter.export(pcm)
        writeToFile(testOutputDir.getAbsolutePath + "/pcm.csv", csv)

        // Create file for statistics
        val statsFile = new File(testOutputDir.getAbsolutePath + "/stats.csv")
        val statsWriter = CSVWriter.open(statsFile)
        statsWriter.writeRow(Seq("id", "size", "percentage of N/A"))

        // Export clusters
        for ((cluster, index) <- clusters.zipWithIndex) {
          // Create PCM
          val clusterProductInfo = productsInfo.filter(p => cluster.map(_.getName).contains(p.sku))
          val clusterPCM = miner.mergeSpecifications(clusterProductInfo)


          // Mutate cluster by filtering features that have too much N/As
          val mutatedPCMs = mutate(clusterProductInfo, clusterPCM)

          for ((mutatedPCM, indexMutated) <- mutatedPCMs.zipWithIndex) {
            // Export to CSV
            val mutatedCSV = csvExporter.export(mutatedPCM)
            writeToFile(testOutputDir.getAbsolutePath + "/cluster_" + index + "_" + indexMutated + ".csv", mutatedCSV)

            // Compute statistics
            val percentageOfNA = analyzer.emptyCells(mutatedPCM)._1 / (mutatedPCM.getConcreteFeatures.size() * mutatedPCM.getProducts.size).toDouble
            statsWriter.writeRow(Seq(index, cluster.size, percentageOfNA))

            // Regroup interesting clusters
            if (percentageOfNA <= percentageOfNAThreshold && mutatedPCM.getProducts.size() >= 10) {
              val category = path.substring("bestbuy-dataset/".size)
              val name = category + "_" + index + "_" + indexMutated
              writeToFile(resultingDatasetDir.getAbsolutePath + "/" + name + ".csv", mutatedCSV)
              resultingDatasetStatsWriter.writeRow(Seq(name, cluster.size, percentageOfNA))
            }
          }

        }

        statsWriter.close()

      }
    }

    resultingDatasetStatsWriter.close()
  }


  def mutate(productInfos: List[ProductInfo], pcm: PCM): List[PCM] = {

    val badFeatures = analyzer.emptyCells(pcm)._2.filter(_._2 > (0.2 * pcm.getProducts.size)).map(_._1).toList // 20% of N/A is too much !

    val mutatedPCMs = for (i <- 1 to 10) yield {
      val selectedBadFeatures = Random.shuffle(badFeatures).take(badFeatures.size / 2)

      val copyOfProductInfos = copyProductInfos(productInfos)
      for (copyOfProductInfo <- copyOfProductInfos) {
        for (selectedBadFeature <- selectedBadFeatures) {
          copyOfProductInfo.details -= selectedBadFeature.getName
        }
      }

      val mutatedPCM = miner.mergeSpecifications(copyOfProductInfos)
      mutatedPCM
    }

    pcm :: mutatedPCMs.toList
  }

  def copyProductInfos(productInfos: List[ProductInfo]): List[ProductInfo] = {
    for (productInfo <- productInfos) yield {
      val copyOfProductInfo = new ProductInfo
      copyOfProductInfo.sku = productInfo.sku
      copyOfProductInfo.name = productInfo.name
      copyOfProductInfo.longDescription = productInfo.longDescription
      copyOfProductInfo.features = for (feature <- productInfo.features) yield {
        feature
      }
      copyOfProductInfo.details = productInfo.details
      copyOfProductInfo.completeXMLDescription = productInfo.completeXMLDescription
      copyOfProductInfo
    }
  }

}


