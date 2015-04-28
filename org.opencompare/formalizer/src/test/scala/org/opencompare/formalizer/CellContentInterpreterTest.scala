package org.opencompare.formalizer

import java.io.{FilenameFilter, File, FileWriter}
import org.opencompare.api.java.exception.MergeConflictException
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import org.opencompare.api.java.io.HTMLExporter
import org.opencompare.formalizer.extractor.CellContentInterpreter
import org.opencompare.io.wikipedia.WikipediaPageMiner
import org.opencompare.io.wikipedia.export.PCMModelExporter
import org.scalatest.{Matchers, FlatSpec}

import scala.io.Source

/**
 * Created by gbecan on 14/10/14.
 */
class CellContentInterpreterTest extends FlatSpec with Matchers {

  "CellContentInterpreter" should "interpret every cell in Wikipedia PCMs" in {

    val path = "../io-wikipedia/input/Comparison_of_AMD_processors.txt"
    //val path = "../org.diverse.PCM.io.Wikipedia/input/Comparison_of_disk_encryption_software.txt"

    // Parse
    val miner = new WikipediaPageMiner
    val page = miner.parse(Source.fromFile(path).getLines().mkString("\n"), "Comparison of AMD processors")

    val exporter = new PCMModelExporter
    val pcms = exporter.export(page)


    val interpreter = new CellContentInterpreter
    val serializer = new HTMLExporter

    for ((pcm, index) <- pcms.zipWithIndex) {
      // Interpret cells
      interpreter.interpretCells(pcm)

      // Serialize
      val html = serializer.toHTML(pcm)
      new File("output/").mkdirs() // Create output directory
      val writer = new FileWriter("output/out_" + index + ".html")
      writer.write(html)
      writer.close()
    }


  }


}
