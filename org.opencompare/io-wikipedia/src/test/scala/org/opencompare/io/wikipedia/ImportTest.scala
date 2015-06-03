package org.opencompare.io.wikipedia

import org.opencompare.api.java.PCM
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{CSVExporter, CSVLoader}
import org.opencompare.api.java.util.SimplePCMElementComparator
import org.opencompare.io.wikipedia.export.{PCMModelExporter, WikiTextExporter}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source

/**
 * Created by smangin on 01/06/15.
 */
class ImportTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  var pcm1 : PCM = _
  var pcm2 : PCM = _
  var csv : String = _
  var code : String = _

  val miner = new WikipediaPageMiner
  val pcmFactory = new PCMFactoryImpl
  val pcmExporter = new PCMModelExporter
  val csvExporter = new CSVExporter
  val wikiTextExporter = new WikiTextExporter
  val csvLoader = new CSVLoader(new PCMFactoryImpl, ',', '"')

  val path = "resources/ImportTest/"

  override def beforeAll(): Unit = {
    csv = Source.fromFile(path + "base.csv").mkString
    code = Source.fromFile(path + "base.wikitext").mkString
    pcm1 = pcmFactory.createPCM()
    pcm2 = pcmFactory.createPCM()
    pcm1 = pcmExporter.export(miner.parse(code, "Title")).head
  }

  "A PCM" should "be identical to the wikitext it came from" in {
    pcm2 = csvLoader.load(csv)
    var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)

    println(csvExporter.export(pcm1))
    println(csvExporter.export(pcm2))
    diff.print()

    diff.hasDifferences shouldBe false
  }

  it should "be the same as the one created from it's wikitext representation" in {
    pcm2 = pcmExporter.export(miner.parse(wikiTextExporter.toWikiText(pcm1), "Title")).head
    var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)

    println(csvExporter.export(pcm1))
    println(csvExporter.export(pcm2))
    diff.print()

    diff.hasDifferences shouldBe false
  }

}
