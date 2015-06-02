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
    ("xml_tag")
  )

  def readFailedUnitTests(title : String) : String = {
    Source.fromFile("resources/FailedUnitTests/" + title).mkString
  }
  def readUnitTests(title : String) : String = {
    Source.fromFile("resources/UnitTests/" + title).mkString
  }

  "Each wikitext syntax" should "be identical to this corresponding Csv representation" in {
    forAll (syntaxes) { (filename: String) => {
      val wiki = readUnitTests(filename + ".wikitext")
      val csv = readUnitTests(filename + ".csv")

      val wikiPcm = pcmExporter.export(miner.parse(wiki, "Title")).head
      val renderingPcm = csvLoader.load(csv)
      val diff = wikiPcm.diff(renderingPcm, new SimplePCMElementComparator)

      diff.hasDifferences shouldBe false
    }}
  }

  "Each wikitext syntax" should "not be identical to this corresponding Csv representation" in {
    forAll (syntaxes) { (filename: String) => {
      val wiki = readFailedUnitTests(filename + ".wikitext")
      val csv = readFailedUnitTests(filename + ".csv")

      val wikiPcm = pcmExporter.export(miner.parse(wiki, "Title")).head
      val renderingPcm = csvLoader.load(csv)
      val diff = wikiPcm.diff(renderingPcm, new SimplePCMElementComparator)

      println(readUnitTests(filename + ".csv"))
      println(csvExporter.export(renderingPcm))

      diff.hasDifferences shouldBe true
    }}
  }
}
