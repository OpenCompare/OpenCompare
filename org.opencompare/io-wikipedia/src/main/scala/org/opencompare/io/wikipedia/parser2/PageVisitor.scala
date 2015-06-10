package org.opencompare.io.wikipedia.parser2

import de.fau.cs.osr.ptk.common.AstVisitor
import org.opencompare.io.wikipedia.pcm.Page
import org.sweble.wikitext.parser.nodes._

import scala.collection.mutable

/**
 * Created by gbecan on 6/9/15.
 */
class PageVisitor extends AstVisitor[WtNode] with CompleteWikitextVisitorNoReturn {

  var page = new Page

  private val nodeToText = new NodeToTextVisitor
  private val tableVisitor = new TableVisitor

  private var sectionStack : mutable.Stack[String] = _

  private def init(): Unit = {
    page = new Page
    sectionStack = new mutable.Stack[String]()
  }

  override def visit(wtTableImplicitTableBody: WtTableImplicitTableBody): Unit = {println("not implemented yet")}

  override def visit(wtRedirect: WtRedirect): Unit = {println("not implemented yet")}

  override def visit(wtLinkOptionLinkTarget: WtLinkOptionLinkTarget): Unit = {println("not implemented yet")}

  override def visit(wtTableHeader: WtTableHeader): Unit = {println("not implemented yet")}

  override def visit(wtTableRow: WtTableRow): Unit = {println("not implemented yet")}

  override def visit(wtTagExtension: WtTagExtension): Unit = {println("not implemented yet")}

  override def visit(wtTemplate: WtTemplate): Unit = {println("not implemented yet")}

  override def visit(wtTemplateArguments: WtTemplateArguments): Unit = {println("not implemented yet")}

  override def visit(wtUnorderedList: WtUnorderedList): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtValue: WtValue): Unit = {println("not implemented yet")}

  override def visit(wtWhitespace: WtWhitespace): Unit = {println("not implemented yet")}

  override def visit(wtXmlAttributes: WtXmlAttributes): Unit = {println("not implemented yet")}

  override def visit(wtText: WtText): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtIgnored: WtIgnored): Unit = {println("not implemented yet")}

  override def visit(wtLinkOptionGarbage: WtLinkOptionGarbage): Unit = {println("not implemented yet")}

  override def visit(wtNewline: WtNewline): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtPageName: WtPageName): Unit = {println("not implemented yet")}

  override def visit(wtTemplateArgument: WtTemplateArgument): Unit = {println("not implemented yet")}

  override def visit(wtXmlElement: WtXmlElement): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtImageLink: WtImageLink): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtTemplateParameter: WtTemplateParameter): Unit = {println("not implemented yet")}

  override def visit(wtHorizontalRule: WtHorizontalRule): Unit = {println("not implemented yet")}

  override def visit(wtIllegalCodePoint: WtIllegalCodePoint): Unit = {println("not implemented yet")}

  override def visit(wtLinkOptionKeyword: WtLinkOptionKeyword): Unit = {println("not implemented yet")}

  override def visit(wtLinkOptionResize: WtLinkOptionResize): Unit = {println("not implemented yet")}

  override def visit(wtXmlAttribute: WtXmlAttribute): Unit = {println("not implemented yet")}

  override def visit(wtXmlEmptyTag: WtXmlEmptyTag): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtXmlStartTag: WtXmlStartTag): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtImStartTag: WtImStartTag): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtImEndTag: WtImEndTag): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtXmlEndTag: WtXmlEndTag): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtXmlCharRef: WtXmlCharRef): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtUrl: WtUrl): Unit = {println("not implemented yet")}

  override def visit(wtTicks: WtTicks): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtSignature: WtSignature): Unit = {println("not implemented yet")}

  override def visit(wtPageSwitch: WtPageSwitch): Unit = {println("not implemented yet")}

  override def visit(wtTableCell: WtTableCell): Unit = {println("not implemented yet")}

  override def visit(wtTableCaption: WtTableCaption): Unit = {println("not implemented yet")}

  override def visit(wtTable: WtTable): Unit = {
    println("table")
    val name = sectionStack.top
    val matrices = tableVisitor.extract(wtTable, name)
    matrices.foreach(matrix => page.addMatrix(matrix))
  }

  override def visit(wtSection: WtSection): Unit = {
    println("section")
    val heading = nodeToText.extractString(wtSection.getHeading)
    println("\theading : " + heading)
    sectionStack.push(heading)

    dispatch(wtSection.getBody)

    sectionStack.pop()
  }

  override def visit(wtInternalLink: WtInternalLink): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtExternalLink: WtExternalLink): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtXmlEntityRef: WtXmlEntityRef): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtNodeList: WtNodeList): Unit = {
    println("node list")
  }

  override def visit(wtBody: WtBody): Unit = {
    iterate(wtBody)
  }

  override def visit(wtBold: WtBold): Unit = {println("not implemented yet")}

  override def visit(wtParsedWikitextPage: WtParsedWikitextPage): Unit = {
    init()
    iterate(wtParsedWikitextPage)
  }

  override def visit(wtOrderedList: WtOrderedList): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtOnlyInclude: WtOnlyInclude): Unit = {println("not implemented yet")}

  override def visit(wtName: WtName): Unit = {println("not implemented yet")}

  override def visit(wtListItem: WtListItem): Unit = {println("not implemented yet")}

  override def visit(wtLinkTitle: WtLinkTitle): Unit = {println("not implemented yet")}

  override def visit(wtLinkOptions: WtLinkOptions): Unit = {println("not implemented yet")}

  override def visit(wtLinkOptionAltText: WtLinkOptionAltText): Unit = {println("not implemented yet")}

  override def visit(wtItalics: WtItalics): Unit = {println("not implemented yet")}

  override def visit(wtHeading: WtHeading): Unit = {
    println("heading")
  }

  override def visit(wtDefinitionListTerm: WtDefinitionListTerm): Unit = {println("not implemented yet")}

  override def visit(wtDefinitionListDef: WtDefinitionListDef): Unit = {println("not implemented yet")}

  override def visit(wtDefinitionList: WtDefinitionList): Unit = {println("not implemented yet")}

  override def visit(wtPreproWikitextPage: WtPreproWikitextPage): Unit = {println("not implemented yet")}

  override def visit(wtParagraph: WtParagraph): Unit = {println("not implemented yet")}

  override def visit(wtSemiPre: WtSemiPre): Unit = {println("not implemented yet")}

  override def visit(wtSemiPreLine: WtSemiPreLine): Unit = {println("not implemented yet")}

  override def visit(wtXmlComment: WtXmlComment): Unit = {
//    println("not implemented yet")
  }

  override def visit(wtXmlAttributeGarbage: WtXmlAttributeGarbage): Unit = {println("not implemented yet")}

  override def visit(wtTagExtensionBody: WtTagExtensionBody): Unit = {println("not implemented yet")}
}
