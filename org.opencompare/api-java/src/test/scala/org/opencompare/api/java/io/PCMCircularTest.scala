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

          if (!inputContainer.equals(outputContainer)) {
            val iPath = (new File("/tmp/" + name + "_in.html")).toPath
            Files.write(iPath, code.getBytes)

            val oPath = (new File("/tmp/" + name + "_out.html")).toPath
            Files.write(oPath, exporter.export(outputContainer).getBytes)

            val thisProductSet = inputContainer.getPcm.getProducts.toSet
            val pcmProductSet = outputContainer.getPcm.getProducts.toSet

//            println(thisProductSet)
//            println(pcmProductSet)

//            println(thisProductSet == pcmProductSet)
//            println("in : " + thisProductSet.size)
//            println("out : " + pcmProductSet.size)
//            println("intersect : " + thisProductSet.intersect(pcmProductSet).size)
//            println("in - out : " + thisProductSet.diff(pcmProductSet).size)
//            println("out - in : " + pcmProductSet.diff(thisProductSet).size)

//            for (weirdProduct <- thisProductSet.diff(pcmProductSet)) {
//              println("---")
//              println(weirdProduct)
//              println()
//
//              val equivalentIn = thisProductSet.find(_.getKeyContent == weirdProduct.getKeyContent).get
//              println(equivalentIn)
//              println()
//              println("toString : " + (equivalentIn.toString == weirdProduct.toString))
//              println("equal : " + (equivalentIn == weirdProduct))
//              println()
//
//              val equivalentOut = pcmProductSet.find(_.getKeyContent == weirdProduct.getKeyContent).get
//              println(equivalentOut)
//              println()
//              println("toString : " + (equivalentOut.toString == weirdProduct.toString))
//              println("equal : " + (equivalentOut == weirdProduct))
//            }

////            println(code)
//
//            println("---")
//            println("features")
//            println(inputContainer.getPcm.getConcreteFeatures.toSet == outputContainer.getPcm.getConcreteFeatures.toSet)
//            println("input")
//            println(inputContainer.getPcm.getConcreteFeatures.size)
//            inputContainer.getPcm.getConcreteFeatures.foreach(println)
//            println("output")
//            println(outputContainer.getPcm.getConcreteFeatures.size)
//            outputContainer.getPcm.getConcreteFeatures.foreach(println)
////            println("diff")
////            for (iFeature <- inputContainer.getPcm.getConcreteFeatures) {
////              for (oFeature <- outputContainer.getPcm.getConcreteFeatures) {
////                if (iFeature.getName == oFeature.getName) {
////                  println("match found : " + iFeature)
////                }
////              }
////            }
//
//            println("products key")
//            println(inputContainer.getPcm.getProductsKey == outputContainer.getPcm.getProductsKey)
//
//
//            println("products")
//            println(inputContainer.getPcm.getProducts.map(_.getKeyContent).toSet == outputContainer.getPcm.getProducts.map(_.getKeyContent).toSet)
//            println("input")
//            println(inputContainer.getPcm.getProducts.size)
//            inputContainer.getPcm.getProducts.foreach(p => println("\t" + p.getKeyContent))
//            println("output")
//            println(outputContainer.getPcm.getProducts.size)
//            outputContainer.getPcm.getProducts.foreach(p => println("\t" + p.getKeyContent))
////            println("diff")
////            for (iProduct <- inputContainer.getPcm.getProducts) {
////              for (oProduct <- outputContainer.getPcm.getProducts) {
////                if (iProduct.getKeyContent == oProduct.getKeyContent) {
////                  println("\tmatch found : " + iProduct.getKeyContent)
////                }
////              }
////            }
//
//
//            println("cells")
//            println("input")
//            for (product <- inputContainer.getPcm.getProducts) {
//              println("\t" + product.getKeyContent)
//              for (cell <- product.getCells) {
//                println("\t\t" + cell.getContent)
//              }
//            }
//            println("output")
//            for (product <- outputContainer.getPcm.getProducts) {
//              println("\t" + product.getKeyContent)
//              for (cell <- product.getCells) {
//                println("\t\t" + cell.getContent)
//              }
//            }
//
//            println("---")
//            println()
          }


          withClue("PCM: ") {
            inputContainer.getPcm should be (outputContainer.getPcm)
          }
          withClue("PCM metadata: ") {
            inputContainer.getMetadata should be (outputContainer.getMetadata)
          }
          withClue("PCM container: ") {
            inputContainer should be (outputContainer)
          }

        }

        inputStream.close()
      }
    }
  }
}

