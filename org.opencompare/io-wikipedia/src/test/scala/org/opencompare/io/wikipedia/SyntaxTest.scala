package org.opencompare.io.wikipedia

import java.util.concurrent.Executors

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.KMFJSONExporter
import org.opencompare.api.java.io.{CSVExporter, CSVLoader}
import org.opencompare.api.java.util.SimplePCMElementComparator
import org.opencompare.io.wikipedia.export.PCMModelExporter
import org.opencompare.io.wikipedia.io.WikiTextLoader
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.concurrent.ExecutionContext
import scala.io.Source
import scala.reflect.io.{File, Directory}

class SyntaxTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(20))
  val miner = new WikiTextLoader
  val pcmExporter = new PCMModelExporter
  val csvLoader = new CSVLoader(new PCMFactoryImpl, ',', '"')
  val kmfJSONExporter = new KMFJSONExporter

  def getResources: List[(java.io.File, java.io.File)] = {
    val classLoader = getClass().getClassLoader()
    val path = classLoader.getResource("SyntaxTest/")
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

  val syntaxes = Table(
    ("Syntax test csv", "wiki"), getResources: _*
  )

  forAll(syntaxes) {
    (csv: java.io.File, wiki: java.io.File) => {
      val name = csv.getName.replace(".csv", "")
      "Wikitext syntax for " + name should "match this csv representation" in {
        val csvCode = Source.fromFile(csv).mkString
        val wikiCode = Source.fromFile(wiki).mkString
        val wikiContainer = miner.mine(wikiCode, name).get(0)
        val csvPcm = csvLoader.load(csvCode).get(0).getPcm
        csvPcm.setName(name + " from Csv")
        val diff = wikiContainer.getPcm.diff(csvPcm, new SimplePCMElementComparator)

        withClue(kmfJSONExporter.export(wikiContainer)) {
          diff.hasDifferences shouldBe false
        }

      }
    }
  }

}

