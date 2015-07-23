package org.opencompare.api.java

import java.net.URL

import org.opencompare.api.java.io._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.io.Source
import scala.reflect.io.{Directory, File}

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

      name should "have equal PCM" in {

        val containers = initLoader.load(Source.fromURI(file.toURI).mkString)
        for (container: PCMContainer <- containers.asScala) {
          val code = exporter.export(container)
          val container2 = importer.load(code).get(0)

          container.getPcm.equals(container2.getPcm) shouldBe true
        }
      }
      it should "have equal METADATA" in {

        val containers = initLoader.load(Source.fromURI(file.toURI).mkString)
        for (container: PCMContainer <- containers.asScala) {
          val code = exporter.export(container)
          val container2 = importer.load(code).get(0)

          container.getMetadata.equals(container2.getMetadata) shouldBe true
        }
      }
      it should "have equal CONTAINER" in {

        val containers = initLoader.load(Source.fromURI(file.toURI).mkString)
        for (container: PCMContainer <- containers.asScala) {
          val code = exporter.export(container)
          val container2 = importer.load(code).get(0)

          container.equals(container2) shouldBe true
        }
      }
    }
  }
}

