package org.opencompare.api.java.io

import java.io.FileReader

import com.opencsv.CSVReader
import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpec, FunSuite}

import scala.collection.immutable.HashMap
import scala.collection.{mutable, JavaConverters}
import scala.collection.JavaConverters._

/**
 * Created by smangin on 7/3/15.
 */
class IOMatrixTest extends FlatSpec with Matchers with BeforeAndAfterAll  {

  val input = getClass.getClassLoader.getResource("csv/Comparison_of_AMD_processors.csv")
  val file = new java.io.File(input.getPath)
  var refHeight = 0
  var refWidth = 0
  var title = "Blah, Blah : Blah"
  var csvMatrix : IOMatrix = _

  val row = 549
  val column = 12984
  val rowspan = 5
  val colspan = 12
  val content = "fdgqe÷ýt:!u4i19:u8yus:81ts"
  val rawContent = "{{dfkhf|sdhikfs <ref fdgqe÷ýt:!u4i19:u8yus:81ts>"
  var cell : IOCell = _
  var matrix : IOMatrix = _

  override def beforeAll() = {
    // From Csv
    val refCsvReader = new CSVReader(new FileReader(file), ',', '"')
    val refCsvMatrix : mutable.Buffer[Array[String]] = refCsvReader.readAll().asScala
    refHeight = refCsvMatrix.size
    refWidth = refCsvMatrix.head.size

    val csvReader = new CSVReader(new FileReader(file), ',', '"')
    csvMatrix = new IOMatrix()
    csvMatrix.setName(title)
    var i = 0;
    var j = 0;
    for (line <- csvReader.readNext()) {
      for (column <- line.toArray) {
        val cell = new IOCell(column.toString, column.toString, 0, 0)
        csvMatrix.setCell(cell, i, j)
        j += 1
      }
      i += 1
    }
    // From custom values
    cell = new IOCell(content, rawContent, rowspan, colspan)
    matrix = new IOMatrix()
    matrix.setCell(cell, row, column)
  }

  //"A matrix" should "be equals to the reference matrix" in {
  //  val csvReader = new CSVReader(new FileReader(file), ',', '"')
  //  val newCsvMatrix = new IOMatrix()
  //  var i = 0;
  //  var j = 0;
  //  for (line <- csvReader.readNext()) {
  //    for (column <- line.toArray) {
  //      val cell = new IOCell(column.toString, column.toString, 0, 0)
  //      newCsvMatrix.setCell(cell, i, j)
  //      j += 1
  //    }
  //    i += 1
  //  }
  //  csvMatrix.equals(newCsvMatrix) shouldBe true
  //}

  //it should "have equal name with the reference matrix" in {
  //  title.equals(csvMatrix.getName) shouldBe true
  //}

  //it should "have equals cells with reference matrix" in {
  //  matrix.getCell(row, column).equals(cell) shouldBe true
  //}

  //it should "proper replace a cell with a new cell at same position" in {
  //  val cell = new IOCell("Glibebluk", "Humf", 1, 5)
  //  matrix.setCell(cell, row, column)
  //  matrix.getCell(row, column).equals(cell) shouldBe true
  //}

  "A matrix" should "have equal number of rows/columns with the reference matrix" in {
    matrix.getNumberOfRows.equals(row)
    matrix.getNumberOfColumns.equals(column)
  }

  "A cell" should "have the same position in both matrix" in {
    val position = matrix.getPosition(cell)
    val newPosition = new Pair(row, column)
    position._1 shouldBe newPosition._1
    position._2 shouldBe newPosition._2
  }
}
