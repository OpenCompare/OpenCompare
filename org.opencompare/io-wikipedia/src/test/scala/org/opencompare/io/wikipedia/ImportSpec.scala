package org.opencompare.io.wikipedia

import java.io.{File, FileWriter}
import java.util.concurrent.Executors

import org.opencompare.api.java
import org.opencompare.api.java.{PCMFactory, AbstractFeature, Cell, PCM}
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import org.opencompare.api.java.io.{CSVLoader, CSVExporter}
import org.opencompare.api.java.util.{SimplePCMElementComparator, PCMElementComparator}
import org.opencompare.io.wikipedia.export.{WikiTextExporter, PCMModelExporter}
import org.opencompare.io.wikipedia.export.{WikiTextExporter, PCMModelExporter}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent._
import scala.io.Source

/**
 * Created by smangin on 01/06/15.
 */
class ImportSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(20))
  val miner = new WikipediaPageMiner
  val pcmFactory = new PCMFactoryImpl
  val pcmExporter = new PCMModelExporter
  val csvExporter = new CSVExporter
  val wikiTextExporter = new WikiTextExporter
  val csvLoader = new CSVLoader(new PCMFactoryImpl, ',', '"')

  val title = "Comparison (grammar)"
  val csv = """"Positive","Comparative","Superlative"
"good","better","best"
"well","better","best"
"bad","worse","worst"
"ill","worse","worst"
"far","farther","farthest"
"far","further","furthest"
"little","smaller, less(er)","smallest, least"
"many, much","more","most""""
  var code = """{| class="wikitable"
|-
! Positive !! Comparative !! Superlative
|-
| good || better || best
|-
| well || better || best
|-
| bad || worse || worst
|-
| ill || worse || worst
|-
| far || farther || farthest
|-
| far || further || furthest
|-
| little || smaller, less(er) || smallest, least
|-
| many, much || more || most
|}"""

  var pcm1 : PCM = _
  var pcm2 : PCM = _

  override def beforeAll(): Unit = {
    pcm1 = pcmFactory.createPCM()
    pcm2 = pcmFactory.createPCM()
    pcm1 = pcmExporter.export(miner.parse(miner.preprocess(code), title)).head
  }

  "A PCM" should "be identical to the wikitext it came from" in {
    pcm2 = csvLoader.load(csv)
    var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)

    diff.hasDifferences shouldBe false
  }
  it should "be the same that the creation of an another one from it's wikitext representation" in {
    val preprocessedCode2 = miner.preprocess(wikiTextExporter.toWikiText(pcm1))
    pcm2 = pcmExporter.export(miner.parse(preprocessedCode2, title)).head
    var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)

    diff.hasDifferences shouldBe false
  }
}
