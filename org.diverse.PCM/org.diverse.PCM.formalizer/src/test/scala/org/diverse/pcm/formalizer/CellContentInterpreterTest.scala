package org.diverse.pcm.formalizer

import java.io.{File, FileWriter}

import org.diverse.pcm.api.java.export.PCMtoHTML
import org.diverse.pcm.formalizer.extractor.CellContentInterpreter
import org.diverse.pcm.io.wikipedia.WikipediaPageMiner
import org.diverse.pcm.io.wikipedia.export.PCMModelExporter
import org.scalatest.{Matchers, FlatSpec}

import scala.io.Source

/**
 * Created by gbecan on 14/10/14.
 */
class CellContentInterpreterTest extends FlatSpec with Matchers {

  "CellContentInterpreter" should "interpret every cell in Wikipedia PCMs" in {

    val path = "../org.diverse.PCM.io.Wikipedia/input/Comparison_of_AMD_processors.txt"

    // Parse
    val miner = new WikipediaPageMiner
    val page = miner.parse(Source.fromFile(path).getLines().mkString("\n"))

    val exporter = new PCMModelExporter
    val pcm = exporter.export(page)

    // Interpret cells
    val interpreter = new CellContentInterpreter
    interpreter.interpretCells(pcm)

    // Serialize
    val serializer = new PCMtoHTML
    val html = serializer.toHTML(pcm)
    new File("output/").mkdirs() // Create output directory
    val writer = new FileWriter("output/out.html")
    writer.write(html)
    writer.close()
  }

}
