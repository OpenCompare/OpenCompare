package org.opencompare.io.wikipedia

import java.io.StringReader

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.opencompare.io.wikipedia.parser.{PageVisitor, PreprocessVisitor}
import org.opencompare.io.wikipedia.pcm.{Cell, Matrix, Page}
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp
import org.sweble.wikitext.parser.{WikitextParser, WikitextPreprocessor}
import org.sweble.wikitext.parser.utils.SimpleParserConfig
import org.xml.sax.InputSource

import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter
import scalaj.http.{Http, HttpOptions}

/**
 * Created by gbecan on 13/10/14.
 */
class WikipediaPageMiner {


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

  /**
   * Preprocess Wikitext
   * Remove templates with the help of https://en.wikipedia.org/wiki/Special:ExpandTemplates
   */
  def preprocess(code : String) : String = {
    val ast = preprocessor.parseArticle(code, "")
    val visitor = new PreprocessVisitor()
    visitor.go(ast)
    visitor.getPreprocessedCode()
  }


  /**
   * Parse preprocessed wikitext
   * @param preprocessedCode : preprocessed wikitext code
   * @param pageTitle : title of the parsed page
   */
  def parse(preprocessedCode : String, pageTitle : String) : Page = {
    val ast = parser.parseArticle(preprocessedCode, "")
    val visitor = new PageVisitor(pageTitle)
    visitor.go(ast)
    visitor.pcm
  }

  /**
   * Normalize a matrix
   * @param matrix
   * @return
   */
  def normalize(matrix : Matrix) : Matrix = {
    // Duplicate cells with rowspan or colspan
    val normalizedMatrix = new Matrix

    for (cell <- matrix.cells.map(_._2)) {
        for (
          rowShift <- 0 until cell.rowspan;
          columnShift <- 0 until cell.colspan
        ) {

          val row = cell.row + rowShift
          val column = cell.column + columnShift

          val duplicate = new Cell(cell.content, cell.isHeader, row, 1, column, 1)
          normalizedMatrix.setCell(duplicate, row, column)
      }
    }

    fillMissingCells(normalizedMatrix)

    normalizedMatrix
  }

  /**
   * Detect holes in the matrix and add a cell if necessary
   * @param matrix
   */
  def fillMissingCells(matrix : Matrix) {

    for (row <- 0 until matrix.getNumberOfRows(); column <- 0 until matrix.getNumberOfColumns()) {
      if (!matrix.getCell(row, column).isDefined) {
        val emptyCell = new Cell("", false, row, 1, column, 1)
        matrix.setCell(emptyCell, row, column)
      }
    }
  }

}
