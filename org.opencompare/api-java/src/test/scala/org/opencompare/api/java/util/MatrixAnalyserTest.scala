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
 * Created by smangin on 7/1/15.
 */
class MatrixAnalyserTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val featureList = getClass.getClassLoader.getResource("header_detection/")
  val productList = getClass.getClassLoader.getResource("product_detection/")
  var separator = ','
  var quote = '"'

  private def getResources(fileList : URL): List[File] = {
    val file = new java.io.File(fileList.getPath)
    val folder = new Directory(file)
    folder.files.filter(_.isFile).filter(_.name.endsWith(".csv")).toList
  }

  def createMatrix(reader : CSVReader): IOMatrix = {
    val csvMatrix = reader.readAll().asScala
    var i = 0
    val matrix = new IOMatrix()
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

  forAll(Table(("Header detection test"), getResources(featureList): _*)) {
    (file: File) => {
      var header : Integer = file.name.charAt(file.name.indexOf('_') + 1).toString.toInt
      val csvReader = new CSVReader(file.bufferedReader(), separator, quote)
      val matrix = createMatrix(csvReader)
      val detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl)
      detector.process();
      var size = detector.getHeaderHeight - detector.getHeaderOffset
      file.name + " matrix" should "return " + header + " for header size instead of " + size in {
        size.equals(header) shouldBe true
      }
      it should "be equal to reference matrix" in {
        matrix.equals(detector.getMatrix) shouldBe true
      }
    }
  }
  forAll(Table(("Product detection test"), getResources(productList): _*)) {
    (file: File) => {
      var header : Integer = file.name.charAt(file.name.indexOf('_') + 1).toString.toInt
      val csvReader = new CSVReader(file.bufferedReader(), separator, quote)
      val matrix = createMatrix(csvReader)
      val detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl).setTransposition(true)
      detector.process();
      var size = detector.getHeaderHeight - detector.getHeaderOffset
      file.name + " transposed matrix" should "return " + header + " for product size instead of " + size in {
        size.equals(header) shouldBe true
      }
      it should "be equal to reference matrix" in {
        matrix.equals(detector.getMatrix) shouldBe true
      }
    }
  }
}
