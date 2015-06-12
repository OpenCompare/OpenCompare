package org.opencompare.api.java

import java.net.URL

import org.opencompare.api.java.io.{PCMExporter, PCMLoader}
import org.opencompare.api.java.util.ComplexePCMElementComparator
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source
import scala.reflect.io.{Directory, File}

/**
 * Created by smangin on 01/06/15.
 */
abstract class PCMMetadataCircularTest(
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

  forAll(Table(("PCMMetadata Circular test"), getResources(): _*)) {
    (file: File) => {
      val name = file.stripExtension

      "A " + name + " PCM" should "be the same as the one created with it's representation" in {

        val container1 = initLoader.load(Source.fromURI(file.toURI).mkString).get(0) // TODO check for multiple container
        container1.getPcm.setName("Original")
        container1.getPcm.normalize(pcmFactory)

        val code = exporter.export(container1)
        val container2 = importer.load(code).get(0) // TODO check for multiple container
        container2.getPcm.setName("From PCM1")
        container2.getPcm.normalize(pcmFactory)

        println(container1.getMetadata.toString)
        println(container2.getMetadata.toString)

        container1.getMetadata.hasDifferences(container2.getMetadata) shouldBe false
      }
    }
  }
}

