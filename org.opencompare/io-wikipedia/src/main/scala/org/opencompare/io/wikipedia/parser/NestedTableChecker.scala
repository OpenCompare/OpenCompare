package org.opencompare.io.wikipedia.parser

import de.fau.cs.osr.ptk.common.AstVisitor
import org.sweble.wikitext.parser.nodes.WtLctFlags.WtNoLctFlags
import org.sweble.wikitext.parser.nodes._

/**
 * Created by gbecan on 6/25/15.
 */
class NestedTableChecker extends AstVisitor[WtNode] with CompleteWikitextVisitorNoReturn {

  var nestedTableExists : Boolean = false

  def hasNestedTable(cellNode: WtNode): Boolean = {
    nestedTableExists = false
    go(cellNode)
    nestedTableExists
  }

  override def visit(wt: WtTable): Unit = {
    nestedTableExists = true
  }

  override def visit(wt: WtTableImplicitTableBody): Unit = iterate(wt)

  override def visit(wt: WtRedirect): Unit = iterate(wt)

  override def visit(wt: WtLinkOptionLinkTarget): Unit = iterate(wt)

  override def visit(wt: WtTableHeader): Unit = iterate(wt)

  override def visit(wt: WtTableRow): Unit = iterate(wt)

  override def visit(wt: WtTagExtension): Unit = iterate(wt)

  override def visit(wt: WtTemplate): Unit = iterate(wt)

  override def visit(wt: WtTemplateArguments): Unit = iterate(wt)

  override def visit(wt: WtUnorderedList): Unit = iterate(wt)

  override def visit(wt: WtValue): Unit = iterate(wt)

  override def visit(wt: WtWhitespace): Unit = iterate(wt)

  override def visit(wt: WtXmlAttributes): Unit = iterate(wt)

  override def visit(wt: WtText): Unit = iterate(wt)

  override def visit(wt: WtIgnored): Unit = iterate(wt)

  override def visit(wt: WtLinkOptionGarbage): Unit = iterate(wt)

  override def visit(wt: WtNewline): Unit = iterate(wt)

  override def visit(wt: WtPageName): Unit = iterate(wt)

  override def visit(wt: WtTemplateArgument): Unit = iterate(wt)

  override def visit(wt: WtXmlElement): Unit = iterate(wt)

  override def visit(wt: WtImageLink): Unit = iterate(wt)

  override def visit(wt: WtTemplateParameter): Unit = iterate(wt)

  override def visit(wt: WtHorizontalRule): Unit = iterate(wt)

  override def visit(wt: WtIllegalCodePoint): Unit = iterate(wt)

  override def visit(wt: WtLinkOptionKeyword): Unit = iterate(wt)

  override def visit(wt: WtLinkOptionResize): Unit = iterate(wt)

  override def visit(wt: WtXmlAttribute): Unit = iterate(wt)

  override def visit(wt: WtXmlEmptyTag): Unit = iterate(wt)

  override def visit(wt: WtXmlStartTag): Unit = iterate(wt)

  override def visit(wt: WtImStartTag): Unit = iterate(wt)

  override def visit(wt: WtImEndTag): Unit = iterate(wt)

  override def visit(wt: WtXmlEndTag): Unit = iterate(wt)

  override def visit(wt: WtXmlCharRef): Unit = iterate(wt)

  override def visit(wt: WtUrl): Unit = iterate(wt)

  override def visit(wt: WtTicks): Unit = iterate(wt)

  override def visit(wt: WtSignature): Unit = iterate(wt)

  override def visit(wt: WtPageSwitch): Unit = iterate(wt)

  override def visit(wt: WtTableCell): Unit = iterate(wt)

  override def visit(wt: WtTableCaption): Unit = iterate(wt)

  override def visit(wt: WtSection): Unit = iterate(wt)

  override def visit(wt: WtInternalLink): Unit = iterate(wt)

  override def visit(wt: WtExternalLink): Unit = iterate(wt)

  override def visit(wt: WtXmlEntityRef): Unit = iterate(wt)

  override def visit(wt: WtNodeList): Unit = iterate(wt)

  override def visit(wt: WtBody): Unit = iterate(wt)

  override def visit(wt: WtBold): Unit = iterate(wt)

  override def visit(wt: WtParsedWikitextPage): Unit = iterate(wt)

  override def visit(wt: WtOrderedList): Unit = iterate(wt)

  override def visit(wt: WtOnlyInclude): Unit = iterate(wt)

  override def visit(wt: WtName): Unit = iterate(wt)

  override def visit(wt: WtListItem): Unit = iterate(wt)

  override def visit(wt: WtLinkTitle): Unit = iterate(wt)

  override def visit(wt: WtLinkOptions): Unit = iterate(wt)

  override def visit(wt: WtLinkOptionAltText): Unit = iterate(wt)

  override def visit(wt: WtItalics): Unit = iterate(wt)

  override def visit(wt: WtHeading): Unit = iterate(wt)

  override def visit(wt: WtDefinitionListTerm): Unit = iterate(wt)

  override def visit(wt: WtDefinitionListDef): Unit = iterate(wt)

  override def visit(wt: WtDefinitionList): Unit = iterate(wt)

  override def visit(wt: WtPreproWikitextPage): Unit = iterate(wt)

  override def visit(wt: WtParagraph): Unit = iterate(wt)

  override def visit(wt: WtSemiPre): Unit = iterate(wt)

  override def visit(wt: WtSemiPreLine): Unit = iterate(wt)

  override def visit(wt: WtXmlComment): Unit = iterate(wt)

  override def visit(wt: WtXmlAttributeGarbage): Unit = iterate(wt)

  override def visit(wt: WtTagExtensionBody): Unit = iterate(wt)

  def visit(wt: WtLctVarConv): Unit = iterate(wt)

  def visit(wt: WtNoLctFlags): Unit = iterate(wt)

  def visit(wt : WtLinkTarget.WtNoLink) = {}
}
