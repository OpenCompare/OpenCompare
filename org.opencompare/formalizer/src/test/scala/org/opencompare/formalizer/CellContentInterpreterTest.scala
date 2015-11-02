package org.opencompare.formalizer

import java.io.{File, FileWriter}

import org.opencompare.api.java.io.CSVExporter
import org.opencompare.formalizer.extractor.CellContentInterpreter
import org.opencompare.io.wikipedia.io.{MediaWikiAPI, WikiTextLoader, WikiTextTemplateProcessor}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._
import scala.io.Source

/**
 * Created by gbecan on 14/10/14.
 */
class CellContentInterpreterTest extends FlatSpec with Matchers {

  "CellContentInterpreter" should "interpret every cell in Wikipedia PCMs" in {

    val input = getClass.getClassLoader.getResource("Comparison_of_AMD_processors.txt")

    // Parse
    val language = "en"
    val url = "wikipedia.org"
    val mediaWikiAPI = new MediaWikiAPI(url)
    val miner = new WikiTextLoader(new WikiTextTemplateProcessor(mediaWikiAPI))
    val pcms = miner.mine(language, Source.fromFile(input.getPath).getLines().mkString("\n"), "Comparison of AMD processors")

    val interpreter = new CellContentInterpreter
    val serializer = new CSVExporter

    for ((pcmContainer, index) <- pcms.zipWithIndex) {
      // Interpret cells
      interpreter.interpretCells(pcmContainer.getPcm)

      // Serialize
      val csv = serializer.export(pcmContainer)
      new File("output/").mkdirs() // Create output directory
      val writer = new FileWriter("output/out_" + index + ".csv")
      writer.write(csv)
      writer.close()
    }


  }


}
