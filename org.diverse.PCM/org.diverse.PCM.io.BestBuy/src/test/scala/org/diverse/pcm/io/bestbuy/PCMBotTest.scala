package org.diverse.pcm.io.bestbuy

import java.io.File

import org.diverse.pcm.api.java.impl.PCMFactoryImpl
import org.diverse.pcm.api.java.impl.io.KMFJSONLoader
import org.diverse.pcm.api.java.io.CSVLoader
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by gbecan on 4/2/15.
 */
class PCMBotTest extends FlatSpec with Matchers {

  val analyzer = new PCMAnalyzer

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
      val loader = new KMFJSONLoader
      val pcm = loader.load(new File(path))
      val (emptyCells, emptyCellsPerFeature, emptyCellsPerProduct) = analyzer.emptyCells(pcm)
      val (booleanFeature, numericFeatures, textualFeature) = analyzer.featureTypes(pcm)

    }
  }


  it should "run on BestBuy overviews" in {
    forAll (bestbuyOverviewPCMs) { (path : String) =>
      val loader = new CSVLoader(new PCMFactoryImpl)
      val pcm = loader.load(new File(path))
      val (emptyCells, emptyCellsPerFeature, emptyCellsPerProduct) = analyzer.emptyCells(pcm)
      val (booleanFeature, numericFeatures, textualFeature) = analyzer.featureTypes(pcm)
      
      println(emptyCells)
    }
  }




}

