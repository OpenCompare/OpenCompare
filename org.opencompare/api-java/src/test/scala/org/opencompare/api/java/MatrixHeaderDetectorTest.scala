package org.opencompare.api.java

import java.io.{Writer, FileReader}

import com.opencsv.{CSVWriter, CSVReader}
import org.opencompare.api.java.io.{IOCell, IOMatrix}
import org.opencompare.api.java.util.MatrixHeaderDetector
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.{mutable, JavaConverters}
import scala.reflect.io.{Directory, File}
import scala.collection.JavaConverters._

/**
 * Created by smangin on 7/1/15.
 */
class MatrixHeaderDetectorTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  var detector : MatrixHeaderDetector = _
  val input = getClass.getClassLoader.getResource("csv/Comparison_of_AMD_processors.csv")
  val fileList = getClass.getClassLoader.getResource("header_detection/")
  var separator = ','
  var quote = '"'
  var refHeight = 0
  var refWidth = 0

  var inputs : TableFor1[File] = _

  private def getResources(): List[File] = {
    val file = new java.io.File(fileList.getPath)
    val folder = new Directory(file)
    folder.files.filter(_.isFile).filter(_.name.endsWith(".csv")).toList
  }

  override def beforeAll() {
    // reference file
    val file = new java.io.File(input.getPath)
    val refCsvReader = new CSVReader(new FileReader(file), separator, quote)
    val refCsvMatrix : mutable.Buffer[Array[String]] = refCsvReader.readAll().asScala
    refHeight = refCsvMatrix.size
    refWidth = refCsvMatrix.head.size

    val csvReader = new CSVReader(new FileReader(file), separator, quote)
    val matrix = new IOMatrix()
    var i = 0;
    var j = 0;
    for (line <- csvReader.readNext()) {
      for (column <- line.toArray) {
        val cell = new IOCell(column.toString, column.toString)
        matrix.setCell(cell, i, j)
        j += 1
      }
      i += 1
    }
    detector = new MatrixHeaderDetector(matrix);
  }

  "Parsed matrix width" should "be equal to reference matrix width" in {
    detector.getWidth.equals(refWidth) shouldBe true
  }

  "Parsed matrix height" should "be equal to reference matrix height" in {
    detector.getHeight.equals(refHeight) shouldBe true
  }

  forAll(Table(("Header detection test"), getResources(): _*)) {
    (file: File) => {
      var header : Integer = file.name.charAt(file.name.indexOf('_') + 1).toString.toInt
      file.name + " matrix" should "return " + header + " for header size" in {
        val csvReader = new CSVReader(file.bufferedReader(), separator, quote)
        val csvMatrix = csvReader.readAll().asScala;
        val matrix = new IOMatrix()
        var i = 0;
        var j = 0;
        for (line <- csvMatrix) {
          for (column <- line) {
            val cell = new IOCell(column, column)
            matrix.setCell(cell, i, j)
            j += 1
          }
          i += 1
        }
        detector = new MatrixHeaderDetector(matrix)
        var size = detector.getHeaderHeight
        size.equals(header) shouldBe true
      }
    }
  }
}
