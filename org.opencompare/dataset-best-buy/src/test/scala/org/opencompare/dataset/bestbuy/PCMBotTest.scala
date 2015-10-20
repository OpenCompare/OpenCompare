package org.opencompare.dataset.bestbuy

import java.io.{File, FileWriter, FilenameFilter}
import java.nio.file.Files
import java.util

import org.opencompare.hac.agglomeration.SingleLinkage
import com.github.tototoshi.csv.CSVWriter
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.KMFJSONLoader
import org.opencompare.api.java.io.{CSVExporter, CSVLoader}
import org.opencompare.api.java.util.PCMElementComparator
import org.opencompare.api.java._
import org.opencompare.io.bestbuy._
import org.opencompare.io.bestbuy.filters._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FlatSpec, Matchers}
import org.simmetrics.metrics.Levenshtein

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

    override def similarProduct(p1: Product, p2: Product): Boolean = p1.getKeyContent.contains(p2.getKeyContent) || p2.getKeyContent.contains(p1.getKeyContent)

    override def disambiguateProduct(product: Product, products: util.List[Product]): Product = products.head

    override def disambiguateFeature(feature: AbstractFeature, features: util.List[AbstractFeature]): AbstractFeature = features.head
  }


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
        val pcm = loader.load(new File(path))(0).getPcm
        val (emptyCells, emptyCellsPerFeature, emptyCellsPerProduct) = analyzer.emptyCells(pcm)
        val (booleanFeature, numericFeatures, textualFeature) = analyzer.featureTypes(pcm)
      }
    }
  }


  ignore should "run on BestBuy overviews" in {
    forAll(bestbuyOverviewPCMs) { (path: String) =>
      if (new File(path).exists()) {
        val loader = new CSVLoader(new PCMFactoryImpl, ';', '"', false)
        val pcm = loader.load(new File(path))(0).getPcm
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
        val csv = csvExporter.export(new PCMContainer(pcm))
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
        val csv = csvExporter.export(new PCMContainer(pcm))
        writeToFile(testOutputDir.getAbsolutePath + "/pcm.csv", csv)

        // Export clusters
        for ((cluster, index) <- clusters.zipWithIndex if cluster.size == 10) {
          val outputDirCluster = new File(testOutputDir.getAbsolutePath + "/cluster_" + index)
          outputDirCluster.mkdirs()

          val productList = cluster.map(_.getKeyContent).mkString("\n")
          writeToFile(outputDirCluster.getAbsolutePath + "/products.txt", productList)

          val clusterProductInfo = productsInfo.filter(p => cluster.map(_.getKeyContent).contains(p.sku))
          val clusterPCM = miner.mergeSpecifications(clusterProductInfo)
          val clusterCSV = csvExporter.export(new PCMContainer(clusterPCM))
          writeToFile(outputDirCluster.getAbsolutePath + "/spec.csv", clusterCSV)

          for (product <- cluster) {
            val sku = product.getKeyContent
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
          val clusterCSV = csvExporter.export(new PCMContainer(clusterPCM))
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


  // ---------------------------------------------------------------------------------------------------------------- //

  "PCMBot experiment" should "compute metrics on manual dataset" in { //"PCMBot experiment"
    val manualDatasetDir = new File("manual-dataset")
    val statsFile = new File("manual-dataset/metrics.csv")
    val statsWriter = CSVWriter.open(statsFile)
    initStatsFile(statsWriter)

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

    }

    statsWriter.close()
  }


  it should "compute metrics on random dataset" in { //"PCMBot experiment"
  val datasetDir = new File("random-dataset")
    val statsFile = new File(datasetDir.getAbsolutePath + "/metrics.csv")
    val statsWriter = CSVWriter.open(statsFile)
    initStatsFile(statsWriter)

    val loader = new CSVLoader(factory, ';', '"', false)

    if (datasetDir.exists()) {
      for (categoryDir <- datasetDir.listFiles() if categoryDir.isDirectory) {
        val category = categoryDir.getName
        val filter = "random"

          for (pcmDir <- categoryDir.listFiles() if pcmDir.isDirectory) {
            var pcmFile = new File(pcmDir.getAbsolutePath + "/finalPCM.csv")
            analyzePCM(statsWriter, loader, category, filter, pcmDir, pcmFile)
          }

      }

    }

    statsWriter.close()
  }

  it should "compute metrics on clustering dataset" in { //"PCMBot experiment"
  val datasetDir = new File("clustering-dataset")
    val statsFile = new File(datasetDir.getAbsolutePath + "/metrics.csv")
    val statsWriter = CSVWriter.open(statsFile)
    initStatsFile(statsWriter)

    val loader = new CSVLoader(factory, ';', '"', false)

    if (datasetDir.exists()) {
      for (categoryDir <- datasetDir.listFiles() if categoryDir.isDirectory) {
        val category = categoryDir.getName
        val filter = "clustering"

        for (pcmDir <- categoryDir.listFiles() if pcmDir.isDirectory) {
          var pcmFile = new File(pcmDir.getAbsolutePath + "/finalPCM.csv")
          analyzePCM(statsWriter, loader, category, filter, pcmDir, pcmFile)
        }

      }

    }

    statsWriter.close()
  }

  def initStatsFile(statsWriter : CSVWriter): Unit = {

    var stats = List(
      "category",
      "filter",
      "name")

    val pcmStats = List(
      "#products",
      "#features",
      "#N/A",

      "min #N/A per feature",
      "max #N/A per feature",
      "median #N/A per feature",
      "avg #N/A per feature",

      "min #N/A per product",
      "max #N/A per product",
      "median #N/A per product",
      "avg #N/A per product",

      "#boolean",
      "boolean #N/A",
      "boolean min #N/A per feature",
      "boolean max #N/A per feature",
      "boolean median #N/A per feature",
      "boolean avg #N/A per feature",

      "#numeric",
      "numeric #N/A",
      "numeric min #N/A per feature",
      "numeric max #N/A per feature",
      "numeric median #N/A per feature",
      "numeric avg #N/A per feature",

      "#textual",
      "textual #N/A",
      "textual min #N/A per feature",
      "textual max #N/A per feature",
      "textual median #N/A per feature",
      "textual avg #N/A per feature")

    stats = stats ::: pcmStats.map(header => "over " + header)
    stats = stats ::: pcmStats.map(header => "spec " + header)

    stats = stats ::: List("diff #features of overview in spec",
      "diff #cells of overview in spec",
      "diff #features of spec in overview",
      "diff #cells of spec in overview")

    statsWriter.writeRow(stats.toSeq)
  }

  def analyzePCM(statsWriter: CSVWriter, loader: CSVLoader, category: String, filter: String, pcmDir: File, pcmFile: File): Unit = {
    val name = pcmDir.getName
    println(pcmFile.getAbsolutePath)
    val specLoader = new CSVLoader(factory)

    var stats : List[Any] = List(category, filter, name)


    // Overview
    stats = stats ::: statsOnPCM(loader, pcmFile)

    // Create spec PCM
//    val specFiles = pcmDir.listFiles(new FilenameFilter {
//      override def accept(file: File, s: String): Boolean = s.endsWith(".csv") && s != "finalPCM.csv" && s != "spec.csv" && s != "pcm.csv"
//    })
//
//
//    val productInfoLoader = new ProductInfoLoader
//    val path = pcmDir.getAbsolutePath
//
//    val productInfos = for (specFile <- specFiles.toList) yield {
//      val sku = specFile.getName.substring(0, specFile.getName.length - 4)
//      val productInfo = productInfoLoader.load(new File(path + "/" + sku + ".txt"), new File(path + "/" + sku + ".csv"), new File(path + "/" + sku + ".xml"))
//      productInfo.sku = sku
//      productInfo
//    }
//
//    val specPCM = miner.mergeSpecifications(productInfos)
//
//    val specWriter = new CSVExporter
//    writeToFile(pcmDir.getAbsolutePath + "/spec.csv", specWriter.export(specPCM))

    // Spec
    val specFile = new File(pcmDir.getAbsolutePath + "/spec.csv")
    stats = stats ::: statsOnPCM(specLoader, specFile)

    // Diff
    if (pcmFile.exists() && specFile.exists()) {
      val overviewPCM = loader.load(pcmFile)(0).getPcm
      val specPCM = specLoader.load(specFile)(0).getPcm

      val (featuresInCommonOverVSSpec, cellsInCommonOverVSSpec) = analyzer.diff(overviewPCM, specPCM, pcmComparator)
      val (featuresInCommonSpecVSOver, cellsInCommonSpecVSOver) = analyzer.diff(specPCM, overviewPCM, pcmComparator)

      stats = stats ::: List(featuresInCommonOverVSSpec.size, cellsInCommonOverVSSpec.size, featuresInCommonSpecVSOver.size, cellsInCommonSpecVSOver.size)
    } else {
      stats = stats ::: List("NA", "NA", "NA", "NA")
    }

    // Write stats
    statsWriter.writeRow(stats.toSeq)

  }


  def statsOnPCM(loader : CSVLoader, pcmFile : File) : List[Any] = {
    if (pcmFile.exists()) {

      val pcm = loader.load(pcmFile)(0).getPcm

      val numberOfProducts = pcm.getProducts.size()
      val numberOfFeatures = pcm.getConcreteFeatures.size()

      val (nas, nasByFeature, nasByProduct) = analyzer.emptyCells(pcm)
      val minMaxMedAvgNAsByFeature = minMaxMedAvg(nasByFeature.map(_._2).toList)
      val minMaxMedAvgNAsByProduct = minMaxMedAvg(nasByProduct.map(_._2).toList)

      val (booleans, numeric, textual) = analyzer.featureTypes(pcm)
      val booleanNAsByFeature = booleans.map(f => nasByFeature(f))
      val booleanMinMaxMedAvgNAsByFeature = minMaxMedAvg(booleanNAsByFeature)
      val booleanNAs = booleanNAsByFeature.sum

      val numericNAsByFeature = numeric.map(f => nasByFeature(f))
      val numericMinMaxMedAvgNAsByFeature = minMaxMedAvg(numericNAsByFeature)
      val numericNAs = numericNAsByFeature.sum

      val textualNAsByFeature = textual.map(f => nasByFeature(f))
      val textualMinMaxMedAvgNAsByFeature = minMaxMedAvg(textualNAsByFeature)
      val textualNAs = textualNAsByFeature.sum

      List(numberOfProducts, numberOfFeatures, nas) :::
        minMaxMedAvgNAsByFeature :::
          minMaxMedAvgNAsByProduct :::
        List(booleans.size, booleanNAs) ::: booleanMinMaxMedAvgNAsByFeature :::
        List(numeric.size, numericNAs) ::: numericMinMaxMedAvgNAsByFeature :::
        List(textual.size, textualNAs) ::: textualMinMaxMedAvgNAsByFeature

    } else {
      (for (i <- 1 to 29) yield {"NA"}).toList
    }
  }


  def minMaxMedAvg(numbers : List[Int]) : List[Any] = {
    if (numbers.isEmpty) {
      List("NA", "NA", "NA", "NA")
    } else {
      List(numbers.min,
        numbers.max,
        numbers.sorted.get(numbers.size/2),
        numbers.sum.toDouble / numbers.size)
    }
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

        val mergingCondition = Some((c1: List[Product], c2: List[Product]) => {
          val clusterProductInfo = productsInfo.filter(p => c1.map(_.getKeyContent).contains(p.sku) || c2.map(_.getKeyContent).contains(p.sku))
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
        val csv = csvExporter.export(new PCMContainer(pcm))
        writeToFile(testOutputDir.getAbsolutePath + "/pcm.csv", csv)

        // Create file for statistics
        val statsFile = new File(testOutputDir.getAbsolutePath + "/stats.csv")
        val statsWriter = CSVWriter.open(statsFile)
        statsWriter.writeRow(Seq("id", "size", "percentage of N/A"))

        // Export clusters
        for ((cluster, index) <- clusters.zipWithIndex) {
          // Create PCM
          val clusterProductInfo = productsInfo.filter(p => cluster.map(_.getKeyContent).contains(p.sku))
          val clusterPCM = miner.mergeSpecifications(clusterProductInfo)


          // Mutate cluster by filtering features that have too much N/As
          val mutatedPCMs = mutate(clusterProductInfo, clusterPCM)

          for ((mutatedPCM, indexMutated) <- mutatedPCMs.zipWithIndex) {
            // Export to CSV
            val mutatedCSV = csvExporter.export(new PCMContainer(mutatedPCM))
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


