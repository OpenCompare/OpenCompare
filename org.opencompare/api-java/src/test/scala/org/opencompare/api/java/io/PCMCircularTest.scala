package org.opencompare.api.java.io

import java.net.URL
import java.nio.file._
import java.util.stream.Collectors

import org.opencompare.api.java.{PCMContainer, PCMFactory}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConversions._
import scala.io.Source
import scala.reflect.io.{Directory, File}

/**
 * Created by smangin on 01/06/15.
 */
abstract class PCMCircularTest(
      val datasetPath : String,
      val pcmFactory : PCMFactory,
      val initLoader : PCMLoader,
      val exporter : PCMExporter,
      val importer : PCMLoader
   ) extends FlatSpec with Matchers with BeforeAndAfterAll {

  var inputs : TableFor1[File] = _


  private def getResources(): List[Path] = {
    val uri = getClass.getClassLoader.getResource(datasetPath).toURI
    val path = if (uri.getScheme == "jar") {
      val fs = try {
        FileSystems.getFileSystem(uri)
      } catch {
        case _ : FileSystemNotFoundException => FileSystems.newFileSystem(uri, Map.empty[String, Object])
      }
      fs.getPath(datasetPath)
    } else {
      Paths.get(uri)
    }

    val testPaths = Files.walk(path, 1).collect(Collectors.toList[Path]).toList.filterNot(Files.isDirectory(_))

//    println(resource)
//    println(file)
//    println(folder.files)
//    folder.files.filter(_.isFile).toList
    testPaths
  }

  forAll(Table(("Circular test"), getResources(): _*)) {
    (path: Path) => {
      val name = path.getFileName.toString

      it should "export -> import " + name  in {

        val inputStream = Files.newInputStream(path)
        val containers = initLoader.load(Source.fromInputStream(inputStream).mkString)
        for (inputContainer: PCMContainer <- containers) {

          val code = exporter.export(inputContainer)
          val outputContainer = importer.load(code).get(0)

//          if (!inputContainer.equals(outputContainer)) {
//            println(code)
//
//            println(inputContainer.getPcm.getConcreteFeatures.toSet == outputContainer.getPcm.getConcreteFeatures.toSet)
//            println(inputContainer.getPcm.getConcreteFeatures.size)
//            println(outputContainer.getPcm.getConcreteFeatures.size)
//            println(inputContainer.getPcm.getProducts.toSet == outputContainer.getPcm.getProducts.toSet)
//            println(inputContainer.getPcm.getProducts.size)
//            println(outputContainer.getPcm.getProducts.size)
//            println(inputContainer.getPcm.getProductsKey == outputContainer.getPcm.getProductsKey)
//          }


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

        inputStream.close()
      }
    }
  }
}

