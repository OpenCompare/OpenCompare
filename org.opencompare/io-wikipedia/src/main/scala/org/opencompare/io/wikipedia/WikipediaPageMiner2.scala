package org.opencompare.io.wikipedia

import java.io.StringReader

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.opencompare.api.java.PCM
import org.opencompare.io.wikipedia.export.PCMModelExporter
import org.opencompare.io.wikipedia.parser2.PageVisitor
import org.opencompare.io.wikipedia.pcm.Page
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp
import org.sweble.wikitext.parser.{WikitextParser, WikitextPreprocessor}
import org.sweble.wikitext.parser.utils.SimpleParserConfig
import org.xml.sax.InputSource

import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter
import scalaj.http.{HttpOptions, Http}

/**
 * Created by gbecan on 6/9/15.
 */
class WikipediaPageMiner2 {

  private val parserConfig = new SimpleParserConfig()
  private val preprocessor = new WikitextPreprocessor(parserConfig)
  private val parser = new WikitextParser(parserConfig)

  private val wikiConfig = DefaultConfigEnWp.generate()


  /**
   * Retrieve Wikitext code of an article on Wikipedia servers
   * @param title : title of the article on English version of Wikipedia
   */
  def getPageCodeFromWikipedia(title : String): String = {
    val editPage = Http("http://en.wikipedia.org/w/index.php")
      .params("title" -> title.replaceAll(" ", "_"), "action" -> "edit")
      .option(HttpOptions.connTimeout(10000))
      .option(HttpOptions.readTimeout(30000))
      .asString
    val xml = parseHTMLAsXML(editPage)
    var code = (xml \\ "textarea").text

    // Manage the page redirection if any
    if (code.contains("#REDIRECT")) {
      val titleMatch = """\[\[(.*?)\]\]""".r findFirstMatchIn  code
      if (titleMatch.isDefined) {
        val title = titleMatch.get.group(1)
        code = getPageCodeFromWikipedia(title)
      }
    }
    code
  }

  /**
   * Clean HTML to get strict XML
   */
  private def parseHTMLAsXML(htmlCode : String) : Node = {
    val adapter = new NoBindingFactoryAdapter
    val htmlParser = (new SAXFactoryImpl).newSAXParser()
    val xml = adapter.loadXML(new InputSource(new StringReader(htmlCode)), htmlParser)
    xml
  }

  def mine(code : String, title : String): List[PCM] = {

    val ast = parser.parseArticle(code, title)
    val structuralVisitor = new PageVisitor
    structuralVisitor.go(ast)
    val page = structuralVisitor.page

    val exporter = new PCMModelExporter
    exporter.export(page)
  }


}
