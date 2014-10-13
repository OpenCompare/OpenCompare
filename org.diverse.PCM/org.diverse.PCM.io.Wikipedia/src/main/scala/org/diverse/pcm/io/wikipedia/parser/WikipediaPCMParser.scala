package org.diverse.pcm.io.wikipedia.parser

import java.io.StringReader

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.diverse.pcm.io.wikipedia.pcm.Page
import org.sweble.wikitext.`lazy`.{LazyParser, LazyPreprocessor}
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration
import org.xml.sax.InputSource

import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter
import scalaj.http.{Http, HttpOptions}

class WikipediaPCMParser {
  
  private val config = new SimpleWikiConfiguration()
  private val parser = new LazyParser(config)
  private val preprocessor = new LazyPreprocessor(config)
  
  /**
   * Parse PCM from online MediaWiki code
   */
  def parseOnlineArticle(title : String) : Page = {
    val code = retrieveCodeFromOnlineArticle(title)
    preprocessAndParse(code)
  }
  
  /**
   * Retrieve MediaWiki code of an article from URL
   */
  private def retrieveCodeFromOnlineArticle(title : String) : String = {
     val editPage = Http("http://en.wikipedia.org/w/index.php")
     .params("title" -> title.replaceAll(" ", "_"), "action" -> "edit")
    .option(HttpOptions.connTimeout(1000))
    .option(HttpOptions.readTimeout(30000))
    .asString
    val xml = parseHTMLAsXML(editPage)
    val code = (xml \\ "textarea").text
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
  
  
  /**
   * Preprocess then parse PCM from MediaWiki code 
   */
  def preprocessAndParse(code : String) : Page = {
    val preprocessedCode = preprocess(code)
    parseAndNormalizePCM(preprocessedCode)
  }
  
  /**
   * Parse preprocessed MediaWiki code
   */
  def parse(preprocessedCode : String) : Page = {
    parseAndNormalizePCM(preprocessedCode)
  }
  
  /**
   * Preprocess MediaWiki code
   * Remove templates with the help of https://en.wikipedia.org/wiki/Special:ExpandTemplates
   */
  private def preprocess(code : String) : String = {
    val ast = preprocessor.parseArticle(code, "")
    val visitor = new PreprocessVisitor()
    visitor.go(ast)
    visitor.getPreprocessedCode()
  }
  
  /**
   * Parse preprocessed MediaWiki code to extract normalized PCMs
   */
  private def parseAndNormalizePCM(code : String) : Page = {
	val ast = parser.parseArticle(code, "");
    val visitor = new PageVisitor
    visitor.go(ast)
    visitor.pcm
  }
  
  
  /**
   * Retrieve Wikimedia code from online article and expand templates
   * @param title : online article title
   * @return preprocessed code of the article (without templates)
   */
  def preprocessOnlineArticle(title : String) : String = {
    val code = retrieveCodeFromOnlineArticle(title)
    preprocess(code)
  }
  
  
}