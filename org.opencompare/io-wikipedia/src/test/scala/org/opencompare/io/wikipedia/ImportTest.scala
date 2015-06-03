package org.opencompare.io.wikipedia

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
class ImportTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val miner = new WikipediaPageMiner
  val pcmFactory = new PCMFactoryImpl
  val pcmExporter = new PCMModelExporter
  val csvExporter = new CSVExporter
  val wikiTextExporter = new WikiTextExporter
  val csvLoader = new CSVLoader(new PCMFactoryImpl, ',', '"')

  val path = "resources/ImportTest/"
  val intputs = Table(
    ("Example"),
    ("basic"),
    ("basic_inline")
    //("amd_proc") //TODO: Must implement features group
  )

  forAll(intputs) {
    (filename: String) => {
      "A " + filename.replace('_', ' ') + " PCM" should "be identical to the wikitext it came from" in {
        val csv = Source.fromFile(path + filename + ".csv").mkString
        val code = Source.fromFile(path + filename + ".wikitext").mkString
        val preprocessedCode = miner.preprocess(code)
        val pcm1 = pcmExporter.export(miner.parse(preprocessedCode, "From Wikitext")).head
        val pcm2 = csvLoader.load(csv)
        pcm2.setName("From CSV")

        var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)
        println(diff.toString)
        diff.hasDifferences shouldBe false
      }
      it should "be the same as the one created with it's wikitext representation" in {
        val csv = Source.fromFile(path + filename + ".csv").mkString
        val code = Source.fromFile(path + filename + ".wikitext").mkString
        val precode1 = miner.preprocess(code)
        val pcm1 = pcmExporter.export(miner.parse(precode1, "From Wikitext")).head
        val precode2 = miner.preprocess(wikiTextExporter.toWikiText(pcm1))
        val pcm2 = pcmExporter.export(miner.parse(precode2, "From PCM1 Wikitext")).head

        var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)
        println(diff.toString)
        diff.hasDifferences shouldBe false
      }
    }
  }
}
