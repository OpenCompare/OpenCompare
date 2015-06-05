package org.opencompare.experimental.io.wikipedia.parser

import de.fau.cs.osr.ptk.common.AstVisitor
import org.sweble.wikitext.parser.nodes._

class NodeToTextVisitor extends AstVisitor[WtNode] with CompleteWikitextVisitorNoReturn {

  private val builder = new StringBuilder

  override def iterate(e: WtNode): Unit = {
    println("NodeToTextVisitor: " + e)
    super.iterate(e)
  }
  def getText(): String = {
    builder.toString
  }

  def visit(e: WtNodeList) {
    iterate(e)
  }

  def visit(e: WtText) {
    builder ++= e.getContent()
  }

  def visit(e: WtInternalLink) {
    if (!e.getTarget.isEmpty) {
      builder ++= e.getTarget.getAsString
    } else if (!e.getTarget().getAsString.endsWith(".png")) {
      dispatch(e.getTitle())
    }
  }

  def visit(e: WtLinkTitle) {
    iterate(e)
  }

  override def visit(e: WtTableImplicitTableBody): Unit = iterate(e)

  override def visit(e: WtRedirect): Unit = iterate(e)

  override def visit(e: WtLinkOptionLinkTarget): Unit = iterate(e)

  override def visit(e: WtTableHeader): Unit = iterate(e)

  override def visit(e: WtTableRow): Unit = iterate(e)

  override def visit(e: WtTagExtension): Unit = iterate(e)

  override def visit(e: WtTemplate): Unit = iterate(e)

  override def visit(e: WtTemplateArguments): Unit = iterate(e)

  override def visit(e: WtUnorderedList): Unit = iterate(e)

  override def visit(e: WtValue): Unit = iterate(e)

  override def visit(e: WtWhitespace): Unit = iterate(e)

  override def visit(e: WtXmlAttributes): Unit = iterate(e)

  override def visit(e: WtIgnored): Unit = iterate(e)

  override def visit(e: WtLinkOptionGarbage): Unit = iterate(e)

  override def visit(e: WtNewline): Unit = iterate(e)

  override def visit(e: WtPageName): Unit = iterate(e)

  override def visit(e: WtTemplateArgument): Unit = iterate(e)

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

  override def visit(e: WtTable): Unit = iterate(e)

  override def visit(e: WtSection): Unit = iterate(e)

  override def visit(e: WtExternalLink): Unit = iterate(e)

  override def visit(e: WtXmlEntityRef): Unit = iterate(e)

  override def visit(e: WtBody): Unit = iterate(e)

  override def visit(e: WtBold): Unit = iterate(e)

  override def visit(e: WtParsedWikitextPage): Unit = iterate(e)

  override def visit(e: WtOrderedList): Unit = iterate(e)

  override def visit(e: WtOnlyInclude): Unit = iterate(e)

  override def visit(e: WtName): Unit = iterate(e)

  override def visit(e: WtListItem): Unit = iterate(e)

  override def visit(e: WtLinkOptions): Unit = iterate(e)

  override def visit(e: WtLinkOptionAltText): Unit = iterate(e)

  override def visit(e: WtItalics): Unit = iterate(e)

  override def visit(e: WtHeading): Unit = iterate(e)

  override def visit(e: WtDefinitionListTerm): Unit = iterate(e)

  override def visit(e: WtDefinitionListDef): Unit = iterate(e)

  override def visit(e: WtDefinitionList): Unit = iterate(e)

  override def visit(e: WtPreproWikitextPage): Unit = iterate(e)

  override def visit(e: WtParagraph): Unit = iterate(e)

  override def visit(e: WtSemiPre): Unit = iterate(e)

  override def visit(e: WtSemiPreLine): Unit = iterate(e)

  override def visit(e: WtXmlComment): Unit = iterate(e)

  override def visit(e: WtXmlAttributeGarbage): Unit = iterate(e)

  override def visit(e: WtTagExtensionBody): Unit = iterate(e)
}