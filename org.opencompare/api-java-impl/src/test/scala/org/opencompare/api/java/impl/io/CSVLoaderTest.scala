package org.opencompare.api.java.impl.io

import java.io.FileReader

import com.opencsv.CSVReader
import org.opencompare.api.java.PCMContainer
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{CSVLoader, IOCell, IOMatrix}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConverters._
/**
 * Created by smangin on 7/8/15.
 */
class CSVLoaderTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val input = getClass.getClassLoader.getResource("csv/Comparison_of_audio_player_software-Audio_format_ability.csv")
  val title = "Comparison_of_digital_audio_editors"
  var separator = ','
  var quote = '"'
  var refMatrix : IOMatrix = _
  var refContainer : PCMContainer = _
  var matrix : IOMatrix = _
  var container : PCMContainer = _

  override def beforeAll(): Unit = {
    val file = new java.io.File(input.getPath)
    val csvReader = new CSVReader(new FileReader(file), separator, quote)
    matrix = CSVLoader.createMatrix(csvReader).setName(title)
    val refCsvLoader = new CSVLoader(new PCMFactoryImpl, separator, quote)
    container = refCsvLoader.load(matrix).get(0)

    val refCsvReader = new CSVReader(new FileReader(file), separator, quote)
    val csvLoader = new CSVLoader(new PCMFactoryImpl, separator, quote)
    refMatrix = createMatrix(refCsvReader)
    refContainer = csvLoader.load(refMatrix).get(0)
  }

  def createMatrix(reader : CSVReader): IOMatrix = {
    val csvMatrix = reader.readAll().asScala
    var i = 0
    val matrix = new IOMatrix().setName(title)
    for (line <- csvMatrix.iterator) {
      var j = 0;
      for (column <- line.iterator) {
        val cell = new IOCell(column)
        matrix.setCell(cell, i, j, 1, 1)
        j+=1
      }
      i+=1
    }
    matrix
  }

  it should "create a matrix" in {
    matrix.equals(refMatrix) shouldBe true
  }

  it should "create a pcm conform to the original througt the matrix" in {
    container.equals(refContainer) shouldBe true
  }

}
