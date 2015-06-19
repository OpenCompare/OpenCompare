package org.opencompare.io.wikipedia

import org.opencompare.io.wikipedia.io.MediaWikiAPI
import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by gbecan on 6/19/15.
 */
class MediaWikiAPITest extends FlatSpec with Matchers {

  val wikipediaURL = "wikipedia.org"
  val mediaWikiAPI = new MediaWikiAPI(wikipediaURL)

  it should "retrieve the wikitext of Comparison of Nikon DSLR cameras page" in {
    val title = "Comparison of Nikon DSLR cameras"
    val wikitext = mediaWikiAPI.getWikitextFromTitle("en", title)

    wikitext.length shouldNot be (0)
  }

}
