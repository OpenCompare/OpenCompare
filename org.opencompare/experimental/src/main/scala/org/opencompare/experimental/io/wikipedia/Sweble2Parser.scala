package org.opencompare.experimental.io.wikipedia

import java.io.FileWriter
import java.time.LocalTime

import org.joda.time.DateTime
import org.opencompare.experimental.io.wikipedia.parser._
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

  /**
   * Used for reading/writing to database, files, etc.
   * Code From the book "Beginning Scala"
   * http://www.amazon.com/Beginning-Scala-David-Pollak/dp/1430219890
   */
  def using[A <: {def close(): Unit}, B](param: A)(f: A => B): B =
    try { f(param) } finally { param.close() }

  def writeToFile(fileName:String, data:String) =
    using (new FileWriter(fileName)) {
      fileWriter : FileWriter => fileWriter.write(data)
    }

  def parse(code : String, title : String): Unit = {

    println("--------------------------------------------------")
    println("----------------- Pre-processing -----------------")
    println("--------------------------------------------------")
    println()

    val wikiConfig = DefaultConfigEnWp.generate()
    val parserConfig = new SimpleParserConfig()

    val preprocessor = new WikitextPreprocessor(parserConfig)
    val preprocessVisitor = new PreprocessVisitor
    val preprocAST = preprocessor.parseArticle(code, title).asInstanceOf[WtPreproWikitextPage]
    preprocessVisitor.go(preprocAST)
    writeToFile("./preprocessedAst.dump", preprocessVisitor.getPreprocessedCode())
    val preprocessArticle = PreprocessorToParserTransformer.transform(preprocAST)

    println()
    println("--------------------------------------------------")
    println("--------------------- Parsing --------------------")
    println("--------------------------------------------------")
    println()

    val parser = new WikitextParser(parserConfig)
    val ast = parser.parseArticle(preprocessArticle, title)
    val parserVisitor = new PageVisitor(title)
    writeToFile("./parsedAst.dump", ast.toString)
    parserVisitor.go(ast)
    writeToFile("./pcms.csv", parserVisitor.pcm.toCSV())
    println(parserVisitor.pcm.toCSV())
    val pageTitle = PageTitle.make(wikiConfig, "title")
    val wom3Doc = AstToWomConverter.convert(wikiConfig, pageTitle, "author", DateTime.now(), ast)
    writeToFile("./wom.xml", wom3Doc.toString)


    println()
    println("--------------------------------------------------")
    println("----------------- RoundTripping ------------------")
    println("--------------------------------------------------")
    println()

    val roundTripCode = Wom3Toolbox.womToWmXPath(wom3Doc)

    println(roundTripCode)
    println(code.equals(roundTripCode))

    println()
    println("--------------------------------------------------")
    println("------------------- Cleaning ---------------------")
    println("--------------------------------------------------")
    println()

    val textNode = wom3Doc.getDocumentElement.getElementsByTagName("text").item(0)
    println("REMOVING : " + textNode.getTextContent)
    textNode.getParentNode.removeChild(textNode)

    println(Wom3Toolbox.womToWmXPath(wom3Doc))

  }

}
