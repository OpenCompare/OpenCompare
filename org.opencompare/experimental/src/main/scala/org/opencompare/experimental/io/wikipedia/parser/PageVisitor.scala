package org.opencompare.experimental.io.wikipedia.parser

import java.util.regex.Pattern

import de.fau.cs.osr.ptk.common.AstVisitor
import org.opencompare.io.wikipedia.pcm.{Page, Matrix}

import org.sweble.wikitext.parser.nodes._
import scala.collection.mutable.ListBuffer

class PageVisitor(pageTitle: String) extends AstVisitor[WtNode] with CompleteWikitextVisitorNoReturn {


  var matrices: ListBuffer[Matrix] = ListBuffer()
  val pcm: Page = new Page
  pcm.title = pageTitle
  var section: StringBuilder = new StringBuilder
  var inTitle: Boolean = false

  override def iterate(e: WtNode): Unit = {
    println("PageVisitor: " + e)
    super.iterate(e)
  }

  private val trimPattern: Pattern = Pattern.compile("\\s*([\\s\\S]*?)\\s*")

  /**
   * Remove spaces before and after the string
   */
  def trim(s: String): String = {
    val matcher = trimPattern.matcher(s)
    var trimmedString = if (matcher.matches() && matcher.groupCount() == 1) {
      matcher.group(1)
    } else {
      ""
    }
    trimmedString = trimmedString.replaceAll("_", " ")
    trimmedString
  }

  def visit(e: WtNodeList) {
    iterate(e)
  }

  def visit(e: WtTable) {
    val tableVisitor = new TableVisitor
    tableVisitor.visit(e)
    for (matrix <- tableVisitor.matrices) {
      val sectionTitle = section.toString
      if (sectionTitle.isEmpty) {
        matrix.name = trim(pageTitle)
      } else {
        matrix.name = trim(pageTitle + " - " + sectionTitle)
      }

      pcm.addMatrix(matrix)
    }
  }

  def visit(e: WtText) {
    if (inTitle) {
      section ++= e.getContent()
    }
  }

  def visit(e: WtSection) {
    inTitle = true
    section = new StringBuilder
    //dispatch(e) recursive iteration !!!
    inTitle = false
    iterate(e)
  }

  def visit(e: WtInternalLink) {
    if (inTitle) {
      val nodeToText = new NodeToTextVisitor
      nodeToText.go(e)
      section ++= nodeToText.getText
    }
  }

  override def visit(e: WtTableImplicitTableBody): Unit = iterate(e)

  override def visit(e: WtLinkOptionLinkTarget): Unit = iterate(e)

  override def visit(e: WtTableHeader): Unit = iterate(e)

  override def visit(e: WtTableRow): Unit = iterate(e)

  override def visit(e: WtTemplateArguments): Unit = iterate(e)

  override def visit(e: WtUnorderedList): Unit = iterate(e)

  override def visit(e: WtValue): Unit = iterate(e)

  override def visit(e: WtWhitespace): Unit = iterate(e)

  override def visit(e: WtXmlAttributes): Unit = iterate(e)

  override def visit(e: WtLinkOptionGarbage): Unit = iterate(e)

  override def visit(e: WtNewline): Unit = iterate(e)

  override def visit(e: WtPageName): Unit = iterate(e)

  override def visit(e: WtXmlElement): Unit = iterate(e)

  override def visit(e: WtImageLink): Unit = iterate(e)

  override def visit(e: WtTemplateParameter): Unit = iterate(e)

  override def visit(e: WtHorizontalRule): Unit = iterate(e)

  override def visit(e: WtIllegalCodePoint): Unit = iterate(e)

  override def visit(e: WtLinkOptionKeyword): Unit = iterate(e)

  override def visit(e: WtLinkOptionResize): Unit = iterate(e)

  override def visit(e: WtXmlAttribute): Unit = iterate(e)

  override def visit(e: WtXmlEmptyTag): Unit = iterate(e)

  override def visit(e: WtXmlStartTag): Unit = iterate(e)

  override def visit(e: WtImStartTag): Unit = iterate(e)

  override def visit(e: WtImEndTag): Unit = iterate(e)

  override def visit(e: WtXmlEndTag): Unit = iterate(e)

  override def visit(e: WtXmlCharRef): Unit = iterate(e)

  override def visit(e: WtUrl): Unit = iterate(e)

  override def visit(e: WtTicks): Unit = iterate(e)

  override def visit(e: WtSignature): Unit = iterate(e)

  override def visit(e: WtPageSwitch): Unit = iterate(e)

  override def visit(e: WtTableCell): Unit = iterate(e)

  override def visit(e: WtTableCaption): Unit = iterate(e)

  override def visit(e: WtExternalLink): Unit = iterate(e)

  override def visit(e: WtXmlEntityRef): Unit = iterate(e)

  override def visit(e: WtBody): Unit = iterate(e)

  override def visit(e: WtBold): Unit = iterate(e)

  override def visit(e: WtParsedWikitextPage): Unit = iterate(e)

  override def visit(e: WtOrderedList): Unit = iterate(e)

  override def visit(e: WtOnlyInclude): Unit = iterate(e)

  override def visit(e: WtName): Unit = iterate(e)

  override def visit(e: WtListItem): Unit = iterate(e)

  override def visit(e: WtLinkTitle): Unit = iterate(e)

  override def visit(e: WtLinkOptions): Unit = iterate(e)

  override def visit(e: WtLinkOptionAltText): Unit = iterate(e)

  override def visit(e: WtItalics): Unit = iterate(e)

  override def visit(e: WtHeading): Unit = iterate(e)

  override def visit(e: WtDefinitionListTerm): Unit = iterate(e)

  override def visit(e: WtDefinitionListDef): Unit = iterate(e)

  override def visit(e: WtDefinitionList): Unit = iterate(e)

  override def visit(e: WtParagraph): Unit = iterate(e)

  override def visit(e: WtSemiPre): Unit = iterate(e)

  override def visit(e: WtSemiPreLine): Unit = iterate(e)

  override def visit(e: WtXmlAttributeGarbage): Unit = iterate(e)

  override def visit(e: WtTagExtensionBody): Unit = iterate(e)

  override def visit(e: WtRedirect): Unit = iterate(e)

  override def visit(e: WtTagExtension): Unit = iterate(e)

  override def visit(e: WtTemplate): Unit = iterate(e)

  override def visit(e: WtIgnored): Unit = iterate(e)

  override def visit(e: WtXmlComment): Unit = iterate(e)

  override def visit(e: WtPreproWikitextPage): Unit = iterate(e)

  override def visit(e: WtTemplateArgument): Unit = iterate(e)
}