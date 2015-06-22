package org.opencompare.io.wikipedia.parser

import de.fau.cs.osr.ptk.common.AstVisitor
import org.opencompare.io.wikipedia.io.WikiTextTemplateProcessor
import org.opencompare.io.wikipedia.pcm.{Cell, Matrix}
import org.sweble.wikitext.engine.config.WikiConfig
import org.sweble.wikitext.parser.nodes._
import org.sweble.wikitext.parser.{WikitextParser, WikitextPreprocessor}

import scala.collection.JavaConversions._

import scala.collection.mutable

/**
 * Created by gbecan on 6/9/15.
 */
class TableVisitor(
                    val language : String,
                    val wikiConfig: WikiConfig,
                    val preprocessor : WikitextPreprocessor,
                    val templateProcessor : WikiTextTemplateProcessor,
                    val parser : WikitextParser
                    ) extends AstVisitor[WtNode] with CompleteWikitextVisitorNoReturn {

  private var matrices : mutable.ListBuffer[Matrix] = _
  private var matrix : Matrix = _

  private var row : Int = 0
  private var column : Int = 0
  private var rowspan : Int = 1
  private var colspan : Int = 1

  private val rawContentExtractor = new RawCellContentExtractor(wikiConfig)
  private val contentExtractor = new CellContentExtractor(language, preprocessor, templateProcessor, parser)

  def extract(wtTable: WtTable, name : String) : List[Matrix] = {
    matrices = mutable.ListBuffer.empty[Matrix]

    matrix = new Matrix
    matrix.name = name
    matrices += matrix

    go(wtTable.getBody)

    matrices.toList
  }

  override def visit(wtTable: WtTable): Unit = {
    val recursiveTableVisitor = new TableVisitor(language, wikiConfig, preprocessor, templateProcessor, parser)
    val recursiveMatrices = recursiveTableVisitor.extract(wtTable, "") // TODO : name of the matrix
    matrices ++= recursiveMatrices
  }

  override def visit(wtBody: WtBody): Unit = {
    iterate(wtBody)
  }

  override def visit(wtTableRow: WtTableRow): Unit = {
    // TODO
    // TODO : wtTableRow.getXmlAttributes

    if (row == 0 && matrix.cells.nonEmpty) {
      row += 1
      column = 0
    }

    if (wtTableRow.getBody().nonEmpty) {
      iterate(wtTableRow)
      row += 1
      column = 0
    }

  }

  override def visit(wtTableHeader: WtTableHeader): Unit = {
    dispatch(wtTableHeader.getXmlAttributes)

    processCell(wtTableHeader, true)
  }

  override def visit(wtTableCell: WtTableCell): Unit = {
    dispatch(wtTableCell.getXmlAttributes)

    processCell(wtTableCell, false)
  }

  private def processCell(cellNode : WtNode, isHeader : Boolean) {

    // Skip cells defined by rowspan
    while (matrix.getCell(row, column).isDefined) {
      column += 1
    }

    // Extract raw cell content
    val rawContent = rawContentExtractor.extract(cellNode)

    // Extract cell content
    val content = contentExtractor.extractCellContent(rawContent)

    // Create cell
    val cell = new Cell(content, rawContent, isHeader, row, rowspan, column, colspan)

    // Handle rowspan and colspan
    for (rowShift <- 0 until rowspan; colShift <- 0 until colspan) {
      matrix.setCell(cell, row + rowShift, column + colShift)
    }

    // Update positions
    column += colspan
    rowspan = 1
    colspan = 1
  }


  override def visit(wtTableImplicitTableBody: WtTableImplicitTableBody): Unit = {}

  override def visit(wtRedirect: WtRedirect): Unit = {}

  override def visit(wtLinkOptionLinkTarget: WtLinkOptionLinkTarget): Unit = {}

  override def visit(wtTagExtension: WtTagExtension): Unit = {}

  override def visit(wtTemplate: WtTemplate): Unit = {}

  override def visit(wtTemplateArguments: WtTemplateArguments): Unit = {}

  override def visit(wtUnorderedList: WtUnorderedList): Unit = {}

  override def visit(wtValue: WtValue): Unit = {}

  override def visit(wtWhitespace: WtWhitespace): Unit = {}

  override def visit(wtXmlAttributes: WtXmlAttributes): Unit = {
    iterate(wtXmlAttributes)
  }

  override def visit(wtText: WtText): Unit = {}

  override def visit(wtIgnored: WtIgnored): Unit = {}

  override def visit(wtLinkOptionGarbage: WtLinkOptionGarbage): Unit = {}

  override def visit(wtNewline: WtNewline): Unit = {}

  override def visit(wtPageName: WtPageName): Unit = {}

  override def visit(wtTemplateArgument: WtTemplateArgument): Unit = {}

  override def visit(wtXmlElement: WtXmlElement): Unit = {}

  override def visit(wtImageLink: WtImageLink): Unit = {}

  override def visit(wtTemplateParameter: WtTemplateParameter): Unit = {}

  override def visit(wtHorizontalRule: WtHorizontalRule): Unit = {}

  override def visit(wtIllegalCodePoint: WtIllegalCodePoint): Unit = {}

  override def visit(wtLinkOptionKeyword: WtLinkOptionKeyword): Unit = {}

  override def visit(wtLinkOptionResize: WtLinkOptionResize): Unit = {}

  override def visit(wtXmlAttribute: WtXmlAttribute): Unit = {
    val name = wtXmlAttribute.getName.getAsString

    if (wtXmlAttribute.getValue.nonEmpty) {
      val value = wtXmlAttribute.getValue.head match {
        case t : WtText => t.toString()
        case _ => ""
      }

      name match {
        case "rowspan" => rowspan = getNumberFromString(value)
        case "colspan" => colspan = getNumberFromString(value)
        case _ =>
      }
    }
  }

  def getNumberFromString(s: String): Int = {
    val numberRegex = "(\\d)+".r
    (numberRegex findFirstIn s).getOrElse("1").toInt
  }

  override def visit(wtXmlEmptyTag: WtXmlEmptyTag): Unit = {}

  override def visit(wtXmlStartTag: WtXmlStartTag): Unit = {}

  override def visit(wtImStartTag: WtImStartTag): Unit = {}

  override def visit(wtImEndTag: WtImEndTag): Unit = {}

  override def visit(wtXmlEndTag: WtXmlEndTag): Unit = {}

  override def visit(wtXmlCharRef: WtXmlCharRef): Unit = {}

  override def visit(wtUrl: WtUrl): Unit = {}

  override def visit(wtTicks: WtTicks): Unit = {}

  override def visit(wtSignature: WtSignature): Unit = {}

  override def visit(wtPageSwitch: WtPageSwitch): Unit = {}

  override def visit(wtTableCaption: WtTableCaption): Unit = {

  }


  override def visit(wtSection: WtSection): Unit = {}

  override def visit(wtInternalLink: WtInternalLink): Unit = {}

  override def visit(wtExternalLink: WtExternalLink): Unit = {}

  override def visit(wtXmlEntityRef: WtXmlEntityRef): Unit = {}

  override def visit(wtNodeList: WtNodeList): Unit = {}

  override def visit(wtBold: WtBold): Unit = {}

  override def visit(wtParsedWikitextPage: WtParsedWikitextPage): Unit = {}

  override def visit(wtOrderedList: WtOrderedList): Unit = {}

  override def visit(wtOnlyInclude: WtOnlyInclude): Unit = {}

  override def visit(wtName: WtName): Unit = {}

  override def visit(wtListItem: WtListItem): Unit = {}

  override def visit(wtLinkTitle: WtLinkTitle): Unit = {}

  override def visit(wtLinkOptions: WtLinkOptions): Unit = {}

  override def visit(wtLinkOptionAltText: WtLinkOptionAltText): Unit = {}

  override def visit(wtItalics: WtItalics): Unit = {}

  override def visit(wtHeading: WtHeading): Unit = {}

  override def visit(wtDefinitionListTerm: WtDefinitionListTerm): Unit = {}

  override def visit(wtDefinitionListDef: WtDefinitionListDef): Unit = {}

  override def visit(wtDefinitionList: WtDefinitionList): Unit = {}

  override def visit(wtPreproWikitextPage: WtPreproWikitextPage): Unit = {}

  override def visit(wtParagraph: WtParagraph): Unit = {}

  override def visit(wtSemiPre: WtSemiPre): Unit = {}

  override def visit(wtSemiPreLine: WtSemiPreLine): Unit = {}

  override def visit(wtXmlComment: WtXmlComment): Unit = {}

  override def visit(wtXmlAttributeGarbage: WtXmlAttributeGarbage): Unit = {}

  override def visit(wtTagExtensionBody: WtTagExtensionBody): Unit = {}
}
