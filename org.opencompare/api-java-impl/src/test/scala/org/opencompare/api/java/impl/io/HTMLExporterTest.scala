package org.opencompare.api.java.impl.io

import java.io.{FileReader, File, BufferedReader}
import java.util.function.Consumer
import javax.sql.rowset.Joinable

import com.opencsv.CSVReader
import com.sun.deploy.util.StringUtils
import org.opencompare.api.java.PCMContainer
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{HTMLExporter, CSVLoader, IOMatrix}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source
import scala.reflect.internal.util.Collections

/**
 * Created by smangin on 7/15/15.
 */
class HTMLExporterTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val input = getClass.getClassLoader.getResource("csv/Comparison_of_audio_player_software-Audio_format_ability.csv")
  val output = getClass.getClassLoader.getResource("html/Comparison_of_audio_player_software-Audio_format_ability.html")
  val title = "Comparison_of_digital_audio_editors"
  var separator = ','
  var quote = '"'
  var container : PCMContainer = _
  var exporter : HTMLExporter = _

  override def beforeAll(): Unit = {
    val file = new java.io.File(input.getPath)
    val csvReader = new CSVReader(new FileReader(file), separator, quote)
    val matrix = CSVLoader.createMatrix(csvReader).setName(title)
    val csvLoader = new CSVLoader(new PCMFactoryImpl, separator, quote)
    container = csvLoader.load(matrix).get(0)
    container.getPcm.setName(title)
    exporter = new HTMLExporter
  }

  "HTML exporter" should "export to HTML from Container" in {
    val content = Source.fromURI(new File(output.getPath).toURI).mkString
    println(exporter.toHTML(container))
    exporter.toHTML(container).equals(content) shouldBe true
  }

}
