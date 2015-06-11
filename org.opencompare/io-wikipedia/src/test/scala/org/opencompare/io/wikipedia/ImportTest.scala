package org.opencompare.io.wikipedia

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{CSVExporter, CSVLoader}
import org.opencompare.api.java.util.SimplePCMElementComparator
import org.opencompare.io.wikipedia.export.PCMModelExporter
import org.opencompare.io.wikipedia.io.{WikiTextExporter, WikiTextLoader}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source
import scala.reflect.io.{Directory, File}

/**
 * Created by smangin on 01/06/15.
 */
class ImportTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val miner = new WikiTextLoader
  val pcmFactory = new PCMFactoryImpl
  val pcmExporter = new PCMModelExporter
  val csvExporter = new CSVExporter
  val wikiTextExporter = new WikiTextExporter
  val csvLoader = new CSVLoader(new PCMFactoryImpl, ',', '"')

  def getResources: List[(java.io.File, java.io.File)] = {
    val classLoader = getClass().getClassLoader()
    val path = classLoader.getResource("ImportTest/")
    val file = new java.io.File(path.getPath)
    val folder = new Directory(file)
    val paths = folder.files.filter(_.isFile).map(f => f.parent.path + File.separator + f.stripExtension).toList.distinct
    paths.map(
      name => (
        new java.io.File(name + ".csv"),
        new java.io.File(name + ".wikitext")
        )
    )
  }

  val inputs = Table(
    ("Import test csv", "wiki"), getResources: _*
  )

  forAll(inputs) {
    (csv: java.io.File, wiki: java.io.File) => {
      val name = csv.getName.replace(".csv", "")
      val csvCode = Source.fromFile(csv).mkString
      val wikiCode = Source.fromFile(wiki).mkString
      val pcm1 = try {
        val pcms = miner.mine(wikiCode, "From Wikitext")

        if (pcms.nonEmpty) {
          pcms.head
        } else {
          pcmFactory.createPCM()
        }

      } catch {
        case e : Exception => {
          e.printStackTrace()
          pcmFactory.createPCM()
        }
      }

      "A " + name + " PCM" should "be identical to the wikitext it came from" in {
        val pcm2 = csvLoader.load(csvCode)
        pcm2.setName("From CSV")

        var diff = pcm1.diff(pcm2, new SimplePCMElementComparator)
        diff.hasDifferences shouldBe false
      }

      it should "be the same as the one created with it's wikitext representation" in {
        val wikiText = wikiTextExporter.export(pcm1)
        val pcm2 = miner.mine(wikiText, "From Wikitext").head

        val diff = pcm1.diff(pcm2, new SimplePCMElementComparator)
        diff.hasDifferences shouldBe false
      }

      it should "be the same as the one created with it's csv representation" in {
        val pcm2 = csvLoader.load(csvExporter.export(pcm1))
        pcm2.setName("From PCM1 Csv")

        val diff = pcm1.diff(pcm2, new SimplePCMElementComparator)
        diff.hasDifferences shouldBe false
      }
    }
  }
}
