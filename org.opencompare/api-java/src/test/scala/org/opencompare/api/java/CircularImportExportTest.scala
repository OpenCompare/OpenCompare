package org.opencompare.api.java

import java.net.URL

import org.opencompare.api.java.io.{PCMExporter, PCMLoader, CSVExporter, CSVLoader}
import org.opencompare.api.java.util.SimplePCMElementComparator
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source
import scala.reflect.io.{File, Directory}

/**
 * Created by smangin on 01/06/15.
 */
abstract class CircularImportExportTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  // Inidicates a folder with a list of file to test concordingly to the import/export classes
  val resource : URL
  val pcmFactory : PCMFactory
  val exporter : PCMExporter
  val loader : PCMLoader

  def getResources: List[File] = {
    val file = new java.io.File(resource.getPath)
    println(resource.getPath)
    val folder = new Directory(file)
    folder.files.filter(_.isFile).toList
  }

  val inputs = Table(
    ("Import test for " + resource.getPath),
    getResources: _*
  )

  forAll(inputs) {
    (file: File) => {
      val name = file.stripExtension
      val pcm1 = loader.load(Source.fromURI(file.toURI).mkString)
      "A " + name + " PCM" should "be the same as the one created with it's " + exporter.getClass.getName + " representation" in {
        val pcm2 = loader.load(exporter.export(pcm1))
        pcm2.setName("From PCM1 Csv")

        var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)
        diff.hasDifferences shouldBe false
      }
    }
  }
}

