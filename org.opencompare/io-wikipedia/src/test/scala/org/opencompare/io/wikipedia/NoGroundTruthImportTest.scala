package org.opencompare.io.wikipedia

import org.opencompare.io.wikipedia.io.{MediaWikiAPI, WikiTextLoader, WikiTextTemplateProcessor}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.Tables.Table
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._
import scala.io.Source
import scala.reflect.io.Directory

/**
  * Created by gbecan on 17/11/15.
  */
class NoGroundTruthImportTest extends FlatSpec with Matchers {

  val language = "en"
  val url = "wikipedia.org"
  val mediaWikiAPI = new MediaWikiAPI(url)
  val miner = new WikiTextLoader(new WikiTextTemplateProcessor(mediaWikiAPI))

  def getResources: List[java.io.File] = {
    val classLoader = getClass().getClassLoader()
    val path = classLoader.getResource("wikitext/")
    val file = new java.io.File(path.getPath)
    val folder = new Directory(file)
    val files = folder.files.filter(_.isFile)
    files.map(file => new java.io.File(file.path)).toList
  }

  val inputs = Table(
    "wikitext", getResources: _*
  )

  forAll(inputs) { file =>
    it should "import " + file.getName in {
      val wikitext = Source.fromFile(file).mkString
      val containers = miner.mine(language, wikitext, file.getName)

      for (container <- containers) {
        val pcm = container.getPcm
        withClue("Name")(pcm.getName.size should not be (0))
        withClue("Features")(pcm.getConcreteFeatures.size() should not be (0))
        withClue("Products")(pcm.getProducts.size() should not be (0))
      }
    }
  }



}
