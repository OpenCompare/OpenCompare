package org.opencompare.experimental.io.wikipedia

import java.time.LocalTime

import org.joda.time.DateTime
import org.sweble.wikitext.engine.PageTitle
import org.sweble.wikitext.engine.config.ParserConfigImpl
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp
import org.sweble.wikitext.parser.parser.PreprocessorToParserTransformer
import org.sweble.wikitext.parser.utils.SimpleParserConfig
import org.sweble.wikitext.parser.{WikitextParser, WikitextPreprocessor}
import org.sweble.wikitext.parser.nodes._
import org.sweble.wom3.swcadapter.AstToWomConverter
import org.sweble.wom3.util.Wom3Toolbox

/**
 * Created by gbecan on 5/20/15.
 */
class Sweble2Parser {

  def parse(code : String, title : String): Unit = {

    val wikiConfig = DefaultConfigEnWp.generate()
    val parserConfig = new SimpleParserConfig()

    val preprocessor = new WikitextPreprocessor(parserConfig)
    val preprocessVisitor = new PreprocessVisitor
    val preproAST = preprocessor.parseArticle(code, title).asInstanceOf[WtPreproWikitextPage]
    val preprocessArticle = PreprocessorToParserTransformer.transform(preproAST)

    preprocessVisitor.go(preproAST)

    println()
    println("--------------------------")
    println()

    val parser = new WikitextParser(parserConfig)
    val ast = parser.parseArticle(code, title)
    val pageTitle = PageTitle.make(wikiConfig, "title")
    val wom3Doc = AstToWomConverter.convert(wikiConfig, pageTitle, "author", DateTime.now(), ast)

    val roundTripCode = Wom3Toolbox.womToWmXPath(wom3Doc)

    println(roundTripCode)
    println(code.equals(roundTripCode))

    println()
    println("--------------------------")
    println()

    val textNode = wom3Doc.getDocumentElement.getElementsByTagName("text").item(0)
    println("REMOVING : " + textNode.getTextContent)
    textNode.getParentNode.removeChild(textNode)

    println(Wom3Toolbox.womToWmXPath(wom3Doc))

  }

}
