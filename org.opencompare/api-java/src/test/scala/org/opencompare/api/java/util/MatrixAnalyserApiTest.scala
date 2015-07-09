package org.opencompare.api.java.util

import java.io.FileReader
import java.net.URL

import com.opencsv.CSVReader
import org.opencompare.api.java.io.{IOCell, IOMatrix}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.reflect.io.{Directory, File}
/**
 * Created by smangin on 7/8/15.
 */
class MatrixAnalyserApiTest  extends FlatSpec with Matchers with BeforeAndAfterAll{

  var matrix : IOMatrix = _
  val input = getClass.getClassLoader.getResource("csv/Comparison_of_digital_audio_editors.csv")
  var separator = ','
  var quote = '"'
  var refHeight = 0
  var refWidth = 0

  var inputs : TableFor1[File] = _

  def createMatrix(reader : CSVReader): IOMatrix = {
    val csvMatrix = reader.readAll().asScala
    var i = 0
    val matrix = new IOMatrix()
    for (line <- csvMatrix.iterator) {
      var j = 0
      for (column <- line.iterator) {
        val cell = new IOCell(column)
        matrix.setCell(cell, i, j, 1, 1)
        j+=1
      }
      i+=1
    }
    matrix
  }

  override def beforeAll() {
    // reference file
    val file = new java.io.File(input.getPath)
    val refCsvReader = new CSVReader(new FileReader(file), separator, quote)
    val refCsvMatrix = refCsvReader.readAll().asScala
    refHeight = refCsvMatrix.size
    refWidth = refCsvMatrix.head.size

    val csvReader = new CSVReader(new FileReader(file), separator, quote)
    matrix = createMatrix(csvReader)
  }

  "A matrix" should "have equal width with the reference matrix" in {
    val detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl);
    detector.getWidth.equals(refWidth) shouldBe true
  }

  it should "have equal height with the reference matrix" in {
    val detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl);
    detector.getHeight.equals(refHeight) shouldBe true
  }

  it should "be equal to the reference matrix" in {
    val detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl);
    detector.getMatrix.equals(matrix) shouldBe true
  }

  "A transposed matrix" should "have equal width with the reference matrix" in {
    val detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl).setTransposition(true);
    detector.getWidth.equals(refHeight) shouldBe true
  }

  it should "have equal height with the reference matrix" in {
    val detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl).setTransposition(true);
    detector.getHeight.equals(refWidth) shouldBe true
  }

  it should "be equal to the reference matrix" in {
    val detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl).setTransposition(true);
    detector.getMatrix.equals(matrix) shouldBe true
  }
}
