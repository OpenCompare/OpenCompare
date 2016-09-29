package org.opencompare.api.scala.io

import java.io.File
import java.nio.file._
import java.util.stream.Collectors

import org.opencompare.api.scala.PCM
import org.opencompare.api.scala.metadata.{Orientation, Positions}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConversions._
import scala.io.Source

abstract class CircularTest(
      val datasetPath : String,
      val initLoader : PCMLoader,
      val exporter : PCMExporter,
      val importer : PCMLoader
   ) extends FlatSpec with Matchers with BeforeAndAfterAll {

  var inputs : TableFor1[File] = _


  private def getResources: List[Path] = {
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

  forAll(Table("Circular test", getResources: _*)) {
    (path: Path) => {
      val name = path.getFileName.toString

      it should "export -> import " + name  in {

        val inputStream = Files.newInputStream(path)
        val pcms = initLoader.load(Source.fromInputStream(inputStream).mkString).map(_.asInstanceOf[PCM with Orientation with Positions])
        inputStream.close()

        for (inputPCM <- pcms) {

          val code = exporter.export(inputPCM)
          val outputPCMs = importer.load(code).map(_.asInstanceOf[PCM with Orientation with Positions])
          outputPCMs should not be empty
          val outputPCM = outputPCMs.head

          // Ignore PCM name
          inputPCM.name = ""
          outputPCM.name = ""

          if (inputPCM != outputPCM) {

            val baseName = exporter.getClass.getName + "-" + importer.getClass.getName + "_" + name
            val csvExporter = new CSVExporter
            Files.write(Paths.get("/tmp", baseName + "_in"), code.getBytes())
            Files.write(Paths.get("/tmp", baseName + "_in.csv"), csvExporter.export(inputPCM).getBytes())
            Files.write(Paths.get("/tmp", baseName + "_out"), exporter.export(outputPCM).getBytes())
            Files.write(Paths.get("/tmp", baseName + "_out.csv"), csvExporter.export(outputPCM).getBytes())
          }

          inputPCM should be (outputPCM)

        }
      }
    }
  }
}

