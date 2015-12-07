package org.opencompare.io.wikipedia

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.io.wikipedia.io.{WikiTextTemplateProcessor, WikiTextLoader, MediaWikiAPI}
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by gbecan on 07/12/15.
  */
class HardCodedGroundTruthImportTest extends FlatSpec with Matchers {

  val pcmFactory = new PCMFactoryImpl
  val url = "wikipedia.org"
  val mediaWikiAPI = new MediaWikiAPI(url)
  val miner = new WikiTextLoader(new WikiTextTemplateProcessor(mediaWikiAPI))

  it should "import a PCM with special characters or nested tables (don't remember what the test is supposed to do)" in  {
    val language = "ja"
    val title = "%E6%A0%83%E6%9C%A8%E7%9C%8C"
    val decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8.name)
    val wikitext = mediaWikiAPI.getWikitextFromTitle(language, decodedTitle)
    val pcms = miner.mine(language, wikitext, title)

    pcms.size() shouldNot be (0)
  }

}
