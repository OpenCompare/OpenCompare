package org.opencompare.io.wikipedia

import java.io.{File, FileReader}

import org.opencompare.api.java.PCM
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{CSVExporter, CSVLoader}
import org.opencompare.api.java.util.SimplePCMElementComparator
import org.opencompare.io.wikipedia.export.{PCMModelExporter, WikiTextExporter}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source

/**
 * Created by smangin on 01/06/15.
 */
class ImportSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  //Local vars
  var pcm1 : PCM = _
  var pcm2 : PCM = _
  var csv : String = _
  var code : String = _
  val syntaxes = Table(
    ("Syntax test"),
    ("boolean"),
    ("colspan"),
    ("core_functions"),
    ("includes"),
    ("internal_link"),
    ("rownspan"),
    ("uri"),
    ("xml_tag")
  )

  // Tools
  val miner = new WikipediaPageMiner
  val pcmFactory = new PCMFactoryImpl
  val pcmExporter = new PCMModelExporter
  val csvExporter = new CSVExporter
  val wikiTextExporter = new WikiTextExporter
  val csvLoader = new CSVLoader(new PCMFactoryImpl, ',', '"')

  def parseFile(title : String) : PCM = {
    val code = Source.fromFile("resources/ImportSpec/UnitTests/" + title).getLines.mkString("\n")
    miner.parse(code, title)
    pcmExporter.export(miner.parse(code, title)).head
  }

  override def beforeAll(): Unit = {
    csv = Source.fromFile("resources/ImportSpec/Base/base.csv").getLines.mkString("\n")
    code = Source.fromFile("resources/ImportSpec/Base/base.wikitext").getLines.mkString("\n")
    pcm1 = pcmFactory.createPCM()
    pcm2 = pcmFactory.createPCM()
    pcm1 = pcmExporter.export(miner.parse(code, "Title")).head
  }

  "A PCM" should "be identical to the wikitext it came from" in {
    pcm2 = csvLoader.load(csv)
    var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)

    diff.hasDifferences shouldBe false
  }
  it should "be the same as the one created from it's wikitext representation" in {
    pcm2 = pcmExporter.export(miner.parse(wikiTextExporter.toWikiText(pcm1), "Title")).head
    var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)

    diff.hasDifferences shouldBe false
  }
  "Each wikitext syntaxes" should "be identical to this corresponding Csv" in {
    forAll (syntaxes) { (filename: String) => {
      val wikiPcm = parseFile(filename + ".wikitext")
      val renderingPcm = csvLoader.load(new FileReader(filename + ".csv"))
      val diff = wikiPcm.diff(renderingPcm, new SimplePCMElementComparator)
      diff.hasDifferences shouldBe false
    }}
  }
}
