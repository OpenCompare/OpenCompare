package org.opencompare.api.java.util

import java.io.FileReader

import com.opencsv.CSVReader
import org.opencompare.api.java.io.IOMatrix
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.reflect.io.{Directory, File}

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
    val matrix = new IOMatrix().loadFromCsv(csvReader)
    detector = new MatrixHeaderDetector(matrix);
  }

  "A matrix" should "have equal width with the reference matrix" in {
    detector.getWidth.equals(refWidth) shouldBe true
  }

  it should "have equal height with the reference matrix" in {
    detector.getHeight.equals(refHeight) shouldBe true
  }

  forAll(Table(("Header detection test"), getResources(): _*)) {
    (file: File) => {
      var header : Integer = file.name.charAt(file.name.indexOf('_') + 1).toString.toInt
      file.name + " matrix" should "return " + header + " for header size" in {
        println(file.name + "################################################")
        val csvReader = new CSVReader(file.bufferedReader(), separator, quote)
        val matrix = new IOMatrix().loadFromCsv(csvReader)
        val detector = new MatrixHeaderDetector(matrix)
        var size = detector.getHeaderHeight
        size.equals(header) shouldBe true
      }
    }
  }
}
