package org.opencompare.api.java.io

import java.io.File
import java.nio.file._
import java.util.stream.Collectors

import org.opencompare.api.java.{PCMContainer, PCMFactory}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConversions._
import scala.io.Source

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

    testPaths
  }

  forAll(Table(("Circular test"), getResources(): _*)) {
    (path: Path) => {
      val name = path.getFileName.toString

      it should "export -> import " + name  in {

        val inputStream = Files.newInputStream(path)
        val containers = initLoader.load(Source.fromInputStream(inputStream).mkString)
        inputStream.close()

        for (inputContainer: PCMContainer <- containers) {

          val code = exporter.export(inputContainer)
          val outputContainer = importer.load(code).head

          // Ignore PCM name
          inputContainer.getPcm.setName("")
          outputContainer.getPcm.setName("")

          if (inputContainer != outputContainer) {
            val baseName = exporter.getClass.getName + "-" + importer.getClass.getName + "_" + name
            val csvExporter = new CSVExporter
            Files.write(Paths.get("/tmp", baseName + "_in"), code.getBytes())
            Files.write(Paths.get("/tmp", baseName + "_in.csv"), csvExporter.export(inputContainer).getBytes())
            Files.write(Paths.get("/tmp", baseName + "_out"), exporter.export(outputContainer).getBytes())
            Files.write(Paths.get("/tmp", baseName + "_out.csv"), csvExporter.export(outputContainer).getBytes())
          }

          withClue("PCM: ") {
            inputContainer.getPcm should be(outputContainer.getPcm)
          }
          withClue("PCM metadata: ") {
            inputContainer.getMetadata should be(outputContainer.getMetadata)
          }
          withClue("PCM container: ") {
            inputContainer should be(outputContainer)
          }

        }
      }
    }
  }
}

