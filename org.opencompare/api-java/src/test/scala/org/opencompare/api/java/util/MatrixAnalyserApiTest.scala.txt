package org.opencompare.api.java.util

import java.io.FileReader

import com.opencsv.CSVReader
import org.opencompare.api.java.io.{IONode, IOCell, IOMatrix}
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.reflect.io.File
/**
 * Created by smangin on 7/8/15.
 */
class MatrixAnalyserApiTest  extends FlatSpec with Matchers with BeforeAndAfterAll{

//  var detector : MatrixAnalyser = _
//  var matrix : IOMatrix = _
//  val input = getClass.getClassLoader.getResource("csv/Comparison_of_audio_player_software-Audio_format_ability.csv")
//  var separator = ','
//  var quote = '"'
//  var refHeight = 0
//  var refWidth = 0
//
//  var inputs : TableFor1[File] = _
//
//  def createRefNodeTree(): IONode = {
//
//    val MP3 = new IONode("MP3", false, 1)
//    val WMA = new IONode("WMA", false, 2)
//    val RealAudio = new IONode("RealAudio", false, 3)
//    val Vorbis = new IONode("Vorbis", false, 4)
//    val Musepack = new IONode("Musepack", false, 5)
//    val AAC = new IONode("AAC", false, 6)
//    val Dolby = new IONode("Dolby", false, 7)
//    val VQF = new IONode("VQF", false, 8)
//    val Opus = new IONode("Opus", false, 9)
//    val lossy = new IONode("Lossy compression", true, 1)
//    lossy.add(MP3)
//    lossy.add(WMA)
//    lossy.add(RealAudio)
//    lossy.add(Vorbis)
//    lossy.add(Musepack)
//    lossy.add(AAC)
//    lossy.add(Dolby)
//    lossy.add(VQF)
//    lossy.add(Opus)
//
//    val APE = new IONode("APE", false, 10)
//    val FLAC = new IONode("FLAC", false, 11)
//    val ALAC = new IONode("ALAC", false, 12)
//    val SHN = new IONode("SHN", false, 13)
//    val WV = new IONode("WV", false, 14)
//    val WMA_L = new IONode("WMA_L", false, 15)
//    val lossless = new IONode("Lossless compression", true, 10)
//    lossless.add(APE)
//    lossless.add(FLAC)
//    lossless.add(ALAC)
//    lossless.add(SHN)
//    lossless.add(WV)
//    lossless.add(WMA_L)
//
//    val root = new IONode("root", true)
//    root.add(lossy)
//    root.add(lossless)
//    root
//  }
//
//  def createRefMatrix(reader : CSVReader): IOMatrix = {
//    val csvMatrix = reader.readAll().asScala
//    var i = 0
//    val matrix = new IOMatrix()
//    for (line <- csvMatrix.iterator) {
//      var j = 0
//      for (column <- line.iterator) {
//        val cell = new IOCell(column)
//        matrix.setCell(cell, i, j, 1, 1)
//        j+=1
//      }
//      i+=1
//    }
//    matrix
//  }
//
//  override def beforeAll() {
//    // reference file
//    val file = new java.io.File(input.getPath)
//    val refCsvReader = new CSVReader(new FileReader(file), separator, quote)
//    val refCsvMatrix = refCsvReader.readAll().asScala
//    refHeight = refCsvMatrix.size
//    refWidth = refCsvMatrix.head.size
//
//    val csvReader = new CSVReader(new FileReader(file), separator, quote)
//    matrix = createRefMatrix(csvReader)
//    detector = new MatrixAnalyser(matrix, new MatrixComparatorEqualityImpl)
//  }
//
//  "A matrix" should "have equal width with the reference matrix" in {
//    detector.getWidth.equals(refWidth) shouldBe true
//  }
//
//  it should "have equal height with the reference matrix" in {
//    detector.getHeight.equals(refHeight) shouldBe true
//  }
//
//  it should "be equal to the reference matrix" in {
//    detector.getMatrix.equals(matrix) shouldBe true
//  }
//
//  "A transposed matrix" should "have equal width with the reference matrix" in {
//    detector.setTransposition(true)
//    detector.getWidth.equals(refHeight) shouldBe true
//  }
//
//  it should "have equal height with the reference matrix" in {
//    detector.setTransposition(true)
//    detector.getHeight.equals(refWidth) shouldBe true
//  }
//
//  it should "be equal to the reference matrix" in {
//    detector.setTransposition(true)
//    detector.getMatrix.equals(matrix) shouldBe true
//  }
//
//  it should "create a node tree properly" in {
//    val node = detector.setTransposition(false).getHeaderNode
//    val refNode = createRefNodeTree()
//    node.equals(refNode) shouldBe true
//  }
}
