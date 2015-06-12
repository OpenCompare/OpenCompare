package org.opencompare.io.wikipedia.io

import java.io.{File, StringReader}

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.opencompare.api.java.PCM
import org.opencompare.api.java.io.PCMLoader
import org.opencompare.io.wikipedia.export.PCMModelExporter
import org.opencompare.io.wikipedia.parser.PageVisitor
import org.opencompare.io.wikipedia.pcm.{Page, Cell, Matrix}
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp
import org.sweble.wikitext.parser.utils.SimpleParserConfig
import org.sweble.wikitext.parser.{WikitextParser, WikitextPreprocessor}
import org.xml.sax.InputSource

import scala.io.Source
import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter
import scalaj.http.{Http, HttpOptions}

/**
 * Created by  on 26/11/14.
 */
class WikiTextLoader(
                    val templateProcessor: WikiTextTemplateProcessor = new WikiTextTemplateProcessor()
                      )  extends PCMLoader {

  // Constructor for Java compatibility with default parameters
  def this() {
    this(new WikiTextTemplateProcessor())
  }

  private val parserConfig = new SimpleParserConfig()
  private val preprocessor = new WikitextPreprocessor(parserConfig)
  private val parser = new WikitextParser(parserConfig)

  private val wikiConfig = DefaultConfigEnWp.generate()

  private val exporter = new PCMModelExporter

  override def load(code: String): PCM = {
    val pcms = mine(code, "")
    if (pcms.isEmpty) {
      null
    } else {
      pcms.head
    }
  }

  override def load(file: File): PCM = {
    this.load(Source.fromFile(file).mkString)
  }


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


  def mineInternalRepresentation(code : String, title : String): Page = {
    val ast = parser.parseArticle(code, title)
    val structuralVisitor = new PageVisitor(wikiConfig, preprocessor, templateProcessor, parser)
    val page = structuralVisitor.extractPage(ast, title)
    page
  }

  def mine(code : String, title : String) : List[PCM] = {
    val page = mineInternalRepresentation(code, title)
    exporter.export(page)
  }

}
