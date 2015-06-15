package org.opencompare.api.java

import java.net.URL

import org.opencompare.api.java.io.{PCMExporter, PCMLoader, CSVExporter, CSVLoader}
import org.opencompare.api.java.util.{ComplexePCMElementComparator, SimplePCMElementComparator}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source
import scala.reflect.io.{File, Directory}

/**
 * Created by smangin on 01/06/15.
 */
abstract class PCMCircularTest(
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

  forAll(Table(("Circular test"), getResources(): _*)) {
    (file: File) => {
      val name = file.stripExtension

      "A " + name + " PCM" should "be the same as the one created with it's representation" in {

        val container1 = initLoader.load(Source.fromURI(file.toURI).mkString).get(0)
        val pcm1 = container1.getPcm
        pcm1.setName("Original")
        pcm1.normalize(pcmFactory)

        val code = exporter.export(container1)
        val container2 = importer.load(code).get(0)
        val pcm2 = container2.getPcm
        pcm2.setName("From PCM1")
        pcm2.normalize(pcmFactory)

        var diff = pcm1.diff(pcm2, new ComplexePCMElementComparator)
        withClue(diff.toString) {
          diff.hasDifferences shouldBe false
        }
      }
    }
  }
}

