package org.opencompare.experimental.io.wikipedia

import java.io.StringReader

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.scalatest.{Matchers, FlatSpec}

import scala.xml.parsing.NoBindingFactoryAdapter
import scalaj.http.{HttpOptions, Http}
import org.xml.sax.InputSource

/**
 * Created by gbecan on 5/20/15.
 */
class Sweble2Test extends FlatSpec with Matchers {

  val title = "Comparison of AMD processors"
  val parser = new Sweble2Parser

  "Sweble 2 parser" should "parse simple wikitext" in {

    val editPage = Http("http://en.wikipedia.org/w/index.php")
      .params("title" -> title.replaceAll(" ", "_"), "action" -> "edit")
      .option(HttpOptions.connTimeout(10000))
      .option(HttpOptions.readTimeout(30000))
      .asString
    val adapter = new NoBindingFactoryAdapter
    val htmlParser = (new SAXFactoryImpl).newSAXParser()
    val xml = adapter.loadXML(new InputSource(new StringReader(editPage)), htmlParser)
    var code = (xml \\ "textarea").text
    parser.parse(code, title)
  }


}
