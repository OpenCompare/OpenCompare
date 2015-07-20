package org.opencompare.io.wikipedia.parser

import de.fau.cs.osr.ptk.common.AstVisitor
import org.opencompare.io.wikipedia.io.WikiTextTemplateProcessor
import org.opencompare.io.wikipedia.pcm.Page
import org.sweble.wikitext.engine.config.WikiConfig
import org.sweble.wikitext.parser.{WikitextPreprocessor, WikitextParser}
import org.sweble.wikitext.parser.nodes._

import scala.collection.mutable

/**
 * Created by gbecan on 6/9/15.
 */
class PageVisitor(
                  val language : String,
                   val wikiConfig : WikiConfig,
                   val preprocessor : WikitextPreprocessor,
                   val templateProcessor : WikiTextTemplateProcessor,
                   val parser : WikitextParser
                   ) extends AstVisitor[WtNode] with CompleteWikitextVisitorNoReturn {

  var page = new Page

  private val nodeToText = new HeadingToTextVisitor

  private var sectionStack : mutable.Stack[String] = _


  def extractPage(ast : WtNode, title : String): Page = {
    sectionStack = new mutable.Stack[String]()

    page = new Page
    page.title = title

    go(ast)

    page
  }

  override def visit(wtTableImplicitTableBody: WtTableImplicitTableBody): Unit = {}

  override def visit(wtRedirect: WtRedirect): Unit = {}

  override def visit(wtLinkOptionLinkTarget: WtLinkOptionLinkTarget): Unit = {}

  override def visit(wtTableHeader: WtTableHeader): Unit = {}

  override def visit(wtTableRow: WtTableRow): Unit = {}

  override def visit(wtTagExtension: WtTagExtension): Unit = {}

  override def visit(wtTemplate: WtTemplate): Unit = {}

  override def visit(wtTemplateArguments: WtTemplateArguments): Unit = {}

  override def visit(wtUnorderedList: WtUnorderedList): Unit = {

  }

  override def visit(wtValue: WtValue): Unit = {}

  override def visit(wtWhitespace: WtWhitespace): Unit = {}

  override def visit(wtXmlAttributes: WtXmlAttributes): Unit = {}

  override def visit(wtText: WtText): Unit = {

  }

  override def visit(wtIgnored: WtIgnored): Unit = {}

  override def visit(wtLinkOptionGarbage: WtLinkOptionGarbage): Unit = {}

  override def visit(wtNewline: WtNewline): Unit = {

  }

  override def visit(wtPageName: WtPageName): Unit = {}

  override def visit(wtTemplateArgument: WtTemplateArgument): Unit = {}

  override def visit(wtXmlElement: WtXmlElement): Unit = {

  }

  override def visit(wtImageLink: WtImageLink): Unit = {

  }

  override def visit(wtTemplateParameter: WtTemplateParameter): Unit = {}

  override def visit(wtHorizontalRule: WtHorizontalRule): Unit = {}

  override def visit(wtIllegalCodePoint: WtIllegalCodePoint): Unit = {}

  override def visit(wtLinkOptionKeyword: WtLinkOptionKeyword): Unit = {}

  override def visit(wtLinkOptionResize: WtLinkOptionResize): Unit = {}

  override def visit(wtXmlAttribute: WtXmlAttribute): Unit = {}

  override def visit(wtXmlEmptyTag: WtXmlEmptyTag): Unit = {

  }

  override def visit(wtXmlStartTag: WtXmlStartTag): Unit = {

  }

  override def visit(wtImStartTag: WtImStartTag): Unit = {

  }

  override def visit(wtImEndTag: WtImEndTag): Unit = {

  }

  override def visit(wtXmlEndTag: WtXmlEndTag): Unit = {

  }

  override def visit(wtXmlCharRef: WtXmlCharRef): Unit = {

  }

  override def visit(wtUrl: WtUrl): Unit = {}

  override def visit(wtTicks: WtTicks): Unit = {

  }

  override def visit(wtSignature: WtSignature): Unit = {}

  override def visit(wtPageSwitch: WtPageSwitch): Unit = {}

  override def visit(wtTableCell: WtTableCell): Unit = {}

  override def visit(wtTableCaption: WtTableCaption): Unit = {}

  override def visit(wtTable: WtTable): Unit = {
    val name = if (sectionStack.isEmpty) {
      page.title
    } else {
      page.title + " - " + sectionStack.top
    }

    val tableVisitor = new TableVisitor(language, wikiConfig, preprocessor, templateProcessor, parser)
    val matrices = tableVisitor.extract(wtTable, name)
    matrices.foreach(matrix => page.addMatrix(matrix))
  }

  override def visit(wtSection: WtSection): Unit = {
    val heading = nodeToText.extractString(wtSection.getHeading)
    sectionStack.push(heading)

    dispatch(wtSection.getBody)

    sectionStack.pop()
  }

  override def visit(wtInternalLink: WtInternalLink): Unit = {

  }

  override def visit(wtExternalLink: WtExternalLink): Unit = {

  }

  override def visit(wtXmlEntityRef: WtXmlEntityRef): Unit = {

  }

  override def visit(wtNodeList: WtNodeList): Unit = {

  }

  override def visit(wtBody: WtBody): Unit = {
    iterate(wtBody)
  }

  override def visit(wtBold: WtBold): Unit = {}

  override def visit(wtParsedWikitextPage: WtParsedWikitextPage): Unit = {
    iterate(wtParsedWikitextPage)
  }

  override def visit(wtOrderedList: WtOrderedList): Unit = {

  }

  override def visit(wtOnlyInclude: WtOnlyInclude): Unit = {}

  override def visit(wtName: WtName): Unit = {}

  override def visit(wtListItem: WtListItem): Unit = {}

  override def visit(wtLinkTitle: WtLinkTitle): Unit = {}

  override def visit(wtLinkOptions: WtLinkOptions): Unit = {}

  override def visit(wtLinkOptionAltText: WtLinkOptionAltText): Unit = {}

  override def visit(wtItalics: WtItalics): Unit = {}

  override def visit(wtHeading: WtHeading): Unit = {

  }

  override def visit(wtDefinitionListTerm: WtDefinitionListTerm): Unit = {}

  override def visit(wtDefinitionListDef: WtDefinitionListDef): Unit = {}

  override def visit(wtDefinitionList: WtDefinitionList): Unit = {}

  override def visit(wtPreproWikitextPage: WtPreproWikitextPage): Unit = {}

  override def visit(wtParagraph: WtParagraph): Unit = {}

  override def visit(wtSemiPre: WtSemiPre): Unit = {}

  override def visit(wtSemiPreLine: WtSemiPreLine): Unit = {}

  override def visit(wtXmlComment: WtXmlComment): Unit = {
//    
  }

  override def visit(wtXmlAttributeGarbage: WtXmlAttributeGarbage): Unit = {}

  override def visit(wtTagExtensionBody: WtTagExtensionBody): Unit = {}

  def visit(wtLctVarConv: WtLctVarConv): Unit = {}
}
