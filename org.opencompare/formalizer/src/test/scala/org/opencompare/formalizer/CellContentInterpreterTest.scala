package org.opencompare.formalizer

import java.io.{File, FileWriter}

import org.opencompare.api.java.io.HTMLExporter
import org.opencompare.formalizer.extractor.CellContentInterpreter
import org.opencompare.io.wikipedia.io.WikiTextLoader
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

/**
 * Created by gbecan on 14/10/14.
 */
class CellContentInterpreterTest extends FlatSpec with Matchers {

  "CellContentInterpreter" should "interpret every cell in Wikipedia PCMs" in {

    val path = "../io-wikipedia/input/Comparison_of_AMD_processors.txt"
    //val path = "../org.diverse.PCM.io.Wikipedia/input/Comparison_of_disk_encryption_software.txt"

    // Parse
    val miner = new WikiTextLoader
    val pcms = miner.mine(Source.fromFile(path).getLines().mkString("\n"), "Comparison of AMD processors")

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
