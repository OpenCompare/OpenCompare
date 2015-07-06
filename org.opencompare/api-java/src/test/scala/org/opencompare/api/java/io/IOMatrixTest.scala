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

  val input = getClass.getClassLoader.getResource("csv/Comparison_of_AMD_processors.csv")
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
    csvMatrix = new IOMatrix().loadFromCsv(csvReader)
    csvMatrix.setName(title)
    // From custom values
    cell = new IOCell(content, rawContent)
    cell.setRowspan(rowspan)
    cell.setColspan(colspan)
    matrix = new IOMatrix()
    matrix.setCell(cell, row, column)
  }

  "A matrix" should "be equal to the reference matrix" in {
    val newCsvReader = new CSVReader(new FileReader(file), ',', '"')
    val newCsvMatrix = new IOMatrix().loadFromCsv(newCsvReader)
    newCsvMatrix.setName(title)
    csvMatrix.isEqual(newCsvMatrix) shouldBe true
  }

  it should "have equal name with the reference matrix" in {
    title.equals(csvMatrix.getName) shouldBe true
  }

  it should "have equals cells with reference matrix" in {
    matrix.getCell(row, column).equals(cell) shouldBe true
  }

  it should "proper replace a cell with a new cell at same position" in {
    val cell = new IOCell("Glibebluk", "Humf")
    matrix.setCell(cell, 1, 1)
    matrix.getCell(1, 1).equals(cell) shouldBe true
  }

  it should "have equal number of rows/columns with the reference matrix" in {
    matrix.getNumberOfRows.equals(row)
    matrix.getNumberOfColumns.equals(column)
  }

  "A cell" should "have the same position in both matrix" in {
    val position = new Pair(cell.getRow, cell.getColumn)
    val newPosition = new Pair(row, column)
    newPosition.equals(position) shouldBe true
  }
}
