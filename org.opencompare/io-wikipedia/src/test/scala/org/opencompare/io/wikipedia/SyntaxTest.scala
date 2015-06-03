package org.opencompare.io.wikipedia

import java.util.concurrent.Executors

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{CSVExporter, CSVLoader}
import org.opencompare.api.java.util.SimplePCMElementComparator
import org.opencompare.io.wikipedia.export.PCMModelExporter
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.ExecutionContext
import scala.io.Source

class SyntaxTest extends FlatSpec with Matchers with BeforeAndAfterAll {
  
  val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(20))
  val miner = new WikipediaPageMiner
  val pcmExporter = new PCMModelExporter
  val csvExporter = new CSVExporter
  val csvLoader = new CSVLoader(new PCMFactoryImpl, ',', '"')

  val path = "resources/SyntaxTest/"
  val syntaxes = Table(
    ("Syntax test"),
    ("boolean"),
    ("colspan"),
    ("core_functions"),
    ("includes"),
    ("internal_link"),
    ("rowspan"),
    ("rowspan_colspan"),
    ("uri"),
    ("xml_tag"),
    ("empty")
  )

  def loadWiki(title : String) : String = {
    Source.fromFile(path + title + ".wikitext").mkString
  }
  def loadCsv(title : String) : String = {
    Source.fromFile(path + title + ".csv").mkString
  }

  forAll (syntaxes) {
    (filename: String) => {
      "Wikitext syntax for " + filename should "match this csv representation" in {
        val wiki = loadWiki(filename)
        val csv = loadCsv(filename)

        val wikiPcm = pcmExporter.export(
          miner.parse(
            miner.preprocess(wiki), filename + " from wikitext")
        ).head
        val waitingPcm = csvLoader.load(csv)
        waitingPcm.setName(filename + " from Csv")
        val diff = wikiPcm.diff(waitingPcm, new SimplePCMElementComparator)
        println("#### Waiting for : ####")
        println(csv)
        println("#### Received : ####")
        println(csvExporter.export(wikiPcm))
        println("#### Diff result : ####")
        println(diff.toString)
        diff.hasDifferences shouldBe false
      }
    }
  }

}
