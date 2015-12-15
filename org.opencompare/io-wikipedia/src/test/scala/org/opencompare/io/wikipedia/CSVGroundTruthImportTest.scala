package org.opencompare.io.wikipedia

import java.nio.file.{Files, Paths}

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.impl.io.KMFJSONExporter
import org.opencompare.api.java.io.CSVLoader
import org.opencompare.io.wikipedia.io.{MediaWikiAPI, WikiTextLoader, WikiTextTemplateProcessor}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.JavaConversions._
import scala.io.Source
import scala.reflect.io.{Directory, File}

/**
 * Created by smangin on 01/06/15.
 */
class CSVGroundTruthImportTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val pcmFactory = new PCMFactoryImpl
  val csvLoader = new CSVLoader(pcmFactory, ',', '"')

  val language = "en"
  val url = "wikipedia.org"
  val mediaWikiAPI = new MediaWikiAPI(url)
  val miner = new WikiTextLoader(new WikiTextTemplateProcessor(mediaWikiAPI))


  def getResources: List[(java.io.File, java.io.File)] = {
    val classLoader = getClass.getClassLoader
    val path = classLoader.getResource("wikitext-and-csv/")
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

  forAll(inputs) { (csv: java.io.File, wiki: java.io.File) => {
    val name = csv.getName.replace(".csv", "")

    it should "import the same PCM from both wikitext and CSV for " + name in {

      val csvCode = Source.fromFile(csv).mkString
      val csvContainer = csvLoader.load(csvCode).head
      csvContainer.getPcm.setName(name)

      val wikiCode = Source.fromFile(wiki).mkString
      val wikitextContainer = miner.mine(language, wikiCode, name).head

      // FIXME : We cannot compare CSV and wikitext loaders because, wikitext is based on raw content while CSV is based on content
      // FIXME : Moreover, we loose the Wikipedia nature when using CSV


      wikitextContainer should be (csvContainer)
    }

  }}


}
