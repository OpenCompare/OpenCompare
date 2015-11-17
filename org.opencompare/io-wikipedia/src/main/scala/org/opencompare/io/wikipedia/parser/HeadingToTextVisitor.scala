package org.opencompare.io.wikipedia.parser

import de.fau.cs.osr.ptk.common.AstVisitor
import org.sweble.wikitext.parser.nodes._
import scala.collection.JavaConversions._

/**
 * Created by gbecan on 6/9/15.
 */
class HeadingToTextVisitor extends AstVisitor[WtNode] with CompleteWikitextVisitor[String] {

  def extractString(node : WtNode) : String = {
    go(node).toString
  }

  override def visit(wtHeading: WtHeading): String = {
    wtHeading.map(dispatch(_)).mkString("")
  }

  override def visit(wtText: WtText): String = {
    wtText.getContent
  }


  override def visit(wtTableImplicitTableBody: WtTableImplicitTableBody): String = {""}

  override def visit(wtRedirect: WtRedirect): String = {""}

  override def visit(wtLinkOptionLinkTarget: WtLinkOptionLinkTarget): String = {""}

  override def visit(wtTableHeader: WtTableHeader): String = {""}

  override def visit(wtTableRow: WtTableRow): String = {""}

  override def visit(wtTagExtension: WtTagExtension): String = {""}

  override def visit(wtTemplate: WtTemplate): String = {""}

  override def visit(wtTemplateArguments: WtTemplateArguments): String = {""}

  override def visit(wtUnorderedList: WtUnorderedList): String = {""}

  override def visit(wtValue: WtValue): String = {""}

  override def visit(wtWhitespace: WtWhitespace): String = {""}

  override def visit(wtXmlAttributes: WtXmlAttributes): String = {""}

  override def visit(wtIgnored: WtIgnored): String = {""}

  override def visit(wtLinkOptionGarbage: WtLinkOptionGarbage): String = {""}

  override def visit(wtNewline: WtNewline): String = {""}

  override def visit(wtPageName: WtPageName): String = {""}

  override def visit(wtTemplateArgument: WtTemplateArgument): String = {""}

  override def visit(wtXmlElement: WtXmlElement): String = {""}

  override def visit(wtImageLink: WtImageLink): String = {""}

  override def visit(wtTemplateParameter: WtTemplateParameter): String = {""}

  override def visit(wtHorizontalRule: WtHorizontalRule): String = {""}

  override def visit(wtIllegalCodePoint: WtIllegalCodePoint): String = {""}

  override def visit(wtLinkOptionKeyword: WtLinkOptionKeyword): String = {""}

  override def visit(wtLinkOptionResize: WtLinkOptionResize): String = {""}

  override def visit(wtXmlAttribute: WtXmlAttribute): String = {""}

  override def visit(wtXmlEmptyTag: WtXmlEmptyTag): String = {""}

  override def visit(wtXmlStartTag: WtXmlStartTag): String = {""}

  override def visit(wtImStartTag: WtImStartTag): String = {""}

  override def visit(wtImEndTag: WtImEndTag): String = {""}

  override def visit(wtXmlEndTag: WtXmlEndTag): String = {""}

  override def visit(wtXmlCharRef: WtXmlCharRef): String = {""}

  override def visit(wtUrl: WtUrl): String = {""}

  override def visit(wtTicks: WtTicks): String = {""}

  override def visit(wtSignature: WtSignature): String = {""}

  override def visit(wtPageSwitch: WtPageSwitch): String = {""}

  override def visit(wtTableCell: WtTableCell): String = {""}

  override def visit(wtTableCaption: WtTableCaption): String = {""}

  override def visit(wtTable: WtTable): String = {""}

  override def visit(wtSection: WtSection): String = {""}

  override def visit(wtInternalLink: WtInternalLink): String = {""}

  override def visit(wtExternalLink: WtExternalLink): String = {""}

  override def visit(wtXmlEntityRef: WtXmlEntityRef): String = {""}

  override def visit(wtNodeList: WtNodeList): String = {""}

  override def visit(wtBody: WtBody): String = {""}

  override def visit(wtBold: WtBold): String = {""}

  override def visit(wtParsedWikitextPage: WtParsedWikitextPage): String = {""}

  override def visit(wtOrderedList: WtOrderedList): String = {""}

  override def visit(wtOnlyInclude: WtOnlyInclude): String = {""}

  override def visit(wtName: WtName): String = {""}

  override def visit(wtListItem: WtListItem): String = {""}

  override def visit(wtLinkTitle: WtLinkTitle): String = {""}

  override def visit(wtLinkOptions: WtLinkOptions): String = {""}

  override def visit(wtLinkOptionAltText: WtLinkOptionAltText): String = {""}

  override def visit(wtItalics: WtItalics): String = {""}

  override def visit(wtDefinitionListTerm: WtDefinitionListTerm): String = {""}

  override def visit(wtDefinitionListDef: WtDefinitionListDef): String = {""}

  override def visit(wtDefinitionList: WtDefinitionList): String = {""}

  override def visit(wtPreproWikitextPage: WtPreproWikitextPage): String = {""}

  override def visit(wtParagraph: WtParagraph): String = {""}

  override def visit(wtSemiPre: WtSemiPre): String = {""}

  override def visit(wtSemiPreLine: WtSemiPreLine): String = {""}

  override def visit(wtXmlComment: WtXmlComment): String = {""}

  override def visit(wtXmlAttributeGarbage: WtXmlAttributeGarbage): String = {""}

  override def visit(wtTagExtensionBody: WtTagExtensionBody): String = {""}

  def visit(wtLctVarConv: WtLctVarConv): String = {""}
}
