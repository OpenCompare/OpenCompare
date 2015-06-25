package org.opencompare.io.wikipedia

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

import org.opencompare.io.wikipedia.io.{WikiTextTemplateProcessor, WikiTextLoader, MediaWikiAPI}
import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by gbecan on 6/25/15.
 */
class SpecificPCMTest extends FlatSpec with Matchers{

  val mediaWikiAPI = new MediaWikiAPI("wikipedia.org")
  val miner = new WikiTextLoader(new WikiTextTemplateProcessor(mediaWikiAPI))

  it should "parse this PCM" in  {
    val lang = "ja"
    val title = "%E6%A0%83%E6%9C%A8%E7%9C%8C"
    val decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8.name)
    val wikitext = mediaWikiAPI.getWikitextFromTitle(lang, decodedTitle)
    val pcms = miner.mine(lang, wikitext, title)

    pcms.size() shouldNot be (0)
  }
}
