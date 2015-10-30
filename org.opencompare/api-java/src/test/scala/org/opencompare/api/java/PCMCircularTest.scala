package org.opencompare.api.java

import java.net.URL

import org.opencompare.api.java.io._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.io.Source
import scala.reflect.io.{Directory, File}

import scala.collection.JavaConversions._

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

      it should "export -> import " + name  in {

        val containers = initLoader.load(Source.fromURI(file.toURI).mkString)
        for (inputContainer: PCMContainer <- containers.asScala) {

          val code = exporter.export(inputContainer)
          val outputContainer = importer.load(code).get(0)

          withClue("PCM: ") {
            inputContainer.getPcm.equals(outputContainer.getPcm) shouldBe true
          }
          withClue("PCM metadata: ") {
            inputContainer.getMetadata.equals(outputContainer.getMetadata) shouldBe true
          }
          withClue("PCM container: ") {
            inputContainer.equals(outputContainer) shouldBe true
          }

        }
      }
    }
  }
}

