package org.opencompare.io.wikipedia

import org.opencompare.io.wikipedia.io.MediaWikiAPI
import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by gbecan on 6/19/15.
 */
class MediaWikiAPITest extends FlatSpec with Matchers {

  val wikipediaURL = "wikipedia.org"
  val english = "en"
  val french = "fr"
  val mediaWikiAPI = new MediaWikiAPI(wikipediaURL)

  it should "retrieve the wikitext of Comparison of Nikon DSLR cameras page" in {
    val title = "Comparison of Nikon DSLR cameras"
    val wikitext = mediaWikiAPI.getWikitextFromTitle(english, title)

    wikitext shouldNot be ("")
  }

  it should "retrieve the wikitext of Comparaison_de_lecteurs_multimédia page" in {
    val title = "Comparaison de lecteurs multimédia"
    val wikitext = mediaWikiAPI.getWikitextFromTitle(french, title)

    wikitext shouldNot be ("")
  }

  it should "expand the template {{yes}}" in {
    val template = "{{yes}}"
    val expandedTemplate = mediaWikiAPI.expandTemplate(english, template)

    expandedTemplate shouldNot be ("")
  }

}
