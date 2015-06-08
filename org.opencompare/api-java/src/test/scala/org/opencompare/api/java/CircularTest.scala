package org.opencompare.api.java

import java.net.URL

import org.opencompare.api.java.io.{PCMExporter, PCMLoader, CSVExporter, CSVLoader}
import org.opencompare.api.java.util.SimplePCMElementComparator
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source
import scala.reflect.io.{File, Directory}

/**
 * Created by smangin on 01/06/15.
 */
abstract class CircularTest(
  val resource : URL,
  val pcmFactory : PCMFactory,
  val initLoader : PCMLoader,
  val exporter : PCMExporter,
  val importer : PCMLoader
   ) extends FlatSpec with Matchers with BeforeAndAfterAll {

  var inputs : TableFor1[File] = _

  private def getResources(): List[File] = {
    val file = new java.io.File(resource.getPath)
    val folder = new Directory(file)
    folder.files.filter(_.isFile).toList
  }

  forAll(Table(
    ("Import test"),
    getResources(): _*
  )) {
    (file: File) => {
      val name = file.stripExtension
      "A " + name + " PCM" should "be the same as the one created with it's representation" in {
        val pcm1 = initLoader.load(Source.fromURI(file.toURI).mkString)
        pcm1.setName("Original")
        pcm1.normalize(pcmFactory)
        val pcm2 = importer.load(exporter.export(pcm1))
        pcm2.normalize(pcmFactory)
        pcm2.setName("From PCM1")

        var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)
        withClue(diff.toString) {
          diff.hasDifferences shouldBe false
        }
      }
    }
  }
}

