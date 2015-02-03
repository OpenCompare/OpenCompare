package org.diverse.pcm.formalizer

import java.io.{FilenameFilter, File, FileWriter}
import org.diverse.pcm.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import org.diverse.pcm.api.java.io.HTMLExporter
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

  it should "interpret the cell of all the Wikipedia PCMs" in {

    val interpreter = new CellContentInterpreter
    val loader = new KMFJSONLoader
    val exporter = new KMFJSONExporter

    val files = new File("../org.diverse.PCM.io.Wikipedia/output/model").listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.endsWith(".pcm")
    })

    // Create output directory
    new File("output/model").mkdirs()

    // Interpret cells for every PCM
    for (file <- files) {
      // Interpret cells
      val pcm = loader.load(file)
      interpreter.interpretCells(pcm)
      val json = exporter.export(pcm)

      // Write modified PCM
      val writer = new FileWriter("output/model/" + file.getName)
      writer.write(json)
      writer.close()
    }
  }

}
