package org.opencompare.io.wikipedia.io

import java.io.File
import java.util

import org.opencompare.api.java.PCMContainer
import org.opencompare.api.java.extractor.CellContentInterpreter
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{PCMDirection, _}
import org.opencompare.io.wikipedia.export.{CSVExporter, PCMModelExporter}
import org.opencompare.io.wikipedia.parser.{PageVisitor, PreprocessVisitor}
import org.opencompare.io.wikipedia.pcm.Page
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp
import org.sweble.wikitext.parser.utils.SimpleParserConfig
import org.sweble.wikitext.parser.{WikitextParser, WikitextPreprocessor}

import scala.collection.JavaConversions._
import scala.io.Source

/**
 * Created by  on 26/11/14.
 */
class WikiTextLoader(
                    val templateProcessor: WikiTextTemplateProcessor
                      )  extends PCMLoader {

  private val parserConfig = new SimpleParserConfig()
  val preprocessor = new WikitextPreprocessor(parserConfig)
  val parser = new WikitextParser(parserConfig)

  private val wikiConfig = DefaultConfigEnWp.generate()

  private val factory = new PCMFactoryImpl
  private val ioLoader = new ImportMatrixLoader(factory, new CellContentInterpreter(factory), PCMDirection.UNKNOWN)

  /**
   * Load PCM from wikitext code with default parameters (english language and empty page title)
   * @param code
   **/
  override def load(code: String): util.List[PCMContainer] = {
    mine("en", code, "")
  }

  override def load(file: File): util.List[PCMContainer] = {
    this.load(Source.fromFile(file).mkString)
  }


  def mineInternalRepresentation(language : String, code : String, title : String): Page = {
    // Format templates
    val preprocessorAST = preprocessor.parseArticle(code, title)
    val templatePreprocessor = new PreprocessVisitor(language, templateProcessor, expandTemplates = false)
    templatePreprocessor.go(preprocessorAST)
    val preprocessedCode = templatePreprocessor.getPreprocessedCode()

    // Parse wikitext code
    val ast = parser.parseArticle(preprocessedCode, title)
    val structuralVisitor = new PageVisitor(language, wikiConfig, preprocessor, templateProcessor, parser)
    val page = structuralVisitor.extractPage(ast, title)
    page
  }

  def mine(language : String, code : String, title : String) : util.List[PCMContainer] = {
    val importMatrices = mineImportMatrix(language, code, title)
    val pcmContainers = importMatrices.map(ioLoader.load)
    seqAsJavaList(pcmContainers)
  }

  def mineImportMatrix(language : String, code : String, title : String) : List[ImportMatrix] = {
    val page = mineInternalRepresentation(language, code, title)

    for (matrix <- page.getMatrices) yield {
      val ioMatrix = new ImportMatrix()
      ioMatrix.setName(matrix.name)

      for (r <- 0 until matrix.getNumberOfRows(); c <- 0 until matrix.getNumberOfColumns()) {
        val cellOpt = matrix.getCell(r, c)
        if (cellOpt.isDefined) {
          val cell = cellOpt.get
          val ioCell = new ImportCell(cell.content, cell.rawContent, cell.rowspan, cell.colspan)
          ioMatrix.setCell(ioCell, r, c)
        }
      }

      ioMatrix
    }
  }

}
