package org.opencompare.api.java.io

import java.io.FileReader

import com.opencsv.CSVReader
import org.opencompare.api.java.util.PrettyPrinter
import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpec, FunSuite}

import scala.collection.immutable.HashMap
import scala.collection.{mutable, JavaConverters}
import scala.collection.JavaConverters._

/**
 * Created by smangin on 7/3/15.
 */
class IOMatrixTest extends FlatSpec with Matchers with BeforeAndAfterAll  {

  val input = getClass.getClassLoader.getResource("csv/Comparison_of_audio_player_software-Audio_format_ability.csv")
  val file = new java.io.File(input.getPath)
  var refHeight = 0
  var refWidth = 0
  var title = "Blah, Blah : Blah"
  var csvMatrix : IOMatrix = _

  val row = 3
  val column = 5
  val rowspan = 2
  val colspan = 3
  val content = "fdgqe÷ýt:!u4i19:u8yus:81ts"
  var cell : IOCell = _
  var matrix : IOMatrix = _

  def createMatrix(reader : CSVReader): IOMatrix = {
    val csvMatrix = reader.readAll().asScala
    var i = 0
    var j = 0
    val matrix = new IOMatrix()
    for (line <- csvMatrix.iterator) {
      for (column <- line.iterator) {
        val cell = new IOCell(column)
        matrix.setCell(cell, i, j, 1, 1)
        j+=1
      }
      i+=1
    }
    matrix
  }

  override def beforeAll() = {
    // From Csv
    val refCsvReader = new CSVReader(new FileReader(file), ',', '"')
    val refCsvMatrix : mutable.Buffer[Array[String]] = refCsvReader.readAll().asScala
    refHeight = refCsvMatrix.size
    refWidth = refCsvMatrix.head.size

    val csvReader = new CSVReader(new FileReader(file), ',', '"')
    csvMatrix = createMatrix(csvReader)
    csvMatrix.setName(title)
    // From custom values
    cell = new IOCell(content)
    matrix = new IOMatrix()
    matrix.setCell(cell, row, column, 1, 1)
  }

  "A matrix" should "have equal name with the reference matrix" in {
    title.equals(csvMatrix.getName) shouldBe true
  }

  it should "have equals cells with reference matrix" in {
    matrix.getCell(row, column).equals(cell) shouldBe true
  }

  it should "proper replace a cell with a new cell at same position" in {
    val cell = new IOCell("Glibebluk")
    matrix.setCell(cell, 1, 1, 1, 1)
    matrix.getCell(1, 1).equals(cell) shouldBe true
  }

  it should "proper expand a cell with colspan/rowspan" in {
    val cell = new IOCell("Glibeblok")
    matrix.setCell(cell, 1, 1, 3, 2)
    for (i <- 1 to 3) {
      for (j <- 1 to 2){
        matrix.getCell(i, j).equals(cell) shouldBe true
      }
    }
  }

  it should "proper give null if a cell does not exist" in {
    matrix.getCell(5, 9) shouldBe null
  }

  it should "proper create a new empty cell if it does not exist" in {
    val cell = new IOCell("")
    matrix.getOrCreateCell(5, 9).equals(cell) shouldBe true
    matrix.getCell(5, 9).equals(cell) shouldBe true
  }

  it should "have equal number of rows/columns with the reference matrix" in {
    matrix.getNumberOfRows.equals(row)
    matrix.getNumberOfColumns.equals(column)
  }
}
