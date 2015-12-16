package org.opencompare.io.wikipedia.parser

import java.util.regex.Pattern

import de.fau.cs.osr.ptk.common.AstVisitor
import org.opencompare.io.wikipedia.io.WikiTextTemplateProcessor
import org.sweble.wikitext.parser.nodes._
import org.sweble.wikitext.parser.{WikitextParser, WikitextPreprocessor}

import scala.collection.mutable.Stack

class CellContentExtractor(
                            val language : String,
                            val preprocessor : WikitextPreprocessor,
                            val templateProcessor : WikiTextTemplateProcessor,
                            val parser : WikitextParser
                            ) extends AstVisitor[WtNode] with CompleteWikitextVisitorNoReturn {

  private var cellContent: StringBuilder = new StringBuilder
  private val trimPattern: Pattern = Pattern.compile("\\s*(.*?)\\s*", Pattern.DOTALL)
  private var ignoredXMLStack: Stack[Boolean] = new Stack()


  def extractCellContent(rawContent : String): String = {
    val code = "{|\n" +
      "|-\n" +
      "| " +
      rawContent + "\n" +
      "|}"

    val title = ""

    // Expand template with preprocessor + remove nowiki tags
    val preprocessorAST = preprocessor.parseArticle(code, title)
    val templatePreprocessor = new PreprocessVisitor(language, templateProcessor)
    templatePreprocessor.go(preprocessorAST)
    val preprocessedCode = templatePreprocessor
      .getPreprocessedCode()
      .replaceAll("<nowiki>", "")
      .replaceAll("</nowiki>", "")

    // Parse content of cell
    val ast = parser.parseArticle(preprocessedCode, title)

    ignoredXMLStack = new Stack()
    cellContent = new StringBuilder()

    go(ast)

    // Treat special cases for cell content
    val content = if (!ignoredXMLElement) {
      if (cellContent.toString().startsWith("||")) {
        cellContent.delete(0, 2)
      }

      trim(cellContent.toString())
    } else {
      ""
    }

    content
  }


  private def ignoredXMLElement: Boolean = {
    if (ignoredXMLStack.nonEmpty) {
      ignoredXMLStack.top
    } else {
      false
    }
  }

  /**
   * Remove spaces before and after the string
   */
  def trim(s: String): String = {
    val matcher = trimPattern.matcher(s)
    if (matcher.matches() && matcher.groupCount() == 1) {
      matcher.group(1)
    } else {
      ""
    }
  }

  def visit(e: WtTable) = {
    iterate(e)
  }

  def visit(e: WtNodeList) = {
    iterate(e)
  }

  override def visit(e: WtXmlAttributes): Unit = {}

  def visit(e: WtXmlAttribute) = {

  }

  def getNumberFromString(s: String): Int = {
    val numberRegex = "(\\d)+".r
    (numberRegex findFirstIn s).getOrElse("1").toInt
  }

  def visit(e: WtTableRow) = {
    iterate(e)
  }

  def visit(e: WtTableHeader) = {

  }

  def visit(e: WtTableCell) = {
    iterate(e)
  }


  def visit(e: WtText) = {
    if (!ignoredXMLElement) {
      cellContent ++= e.getContent()
    }
    iterate(e)
  }

  def visit(e: WtInternalLink) = {
    if (!ignoredXMLElement) {
      val target = e.getTarget().getAsString()

      if (e.getTitle().isEmpty) {
        cellContent ++= target
      } else if (!target.endsWith(".png")) {
        dispatch(e.getTitle())
      }
    }
  }

  def visit(e: WtExternalLink) = {
    if (!ignoredXMLElement) {

      if (e.getTitle().isEmpty()) {
        //		    val target = e.getTarget()
        //		    cellContent ++= target.getProtocol() + ":" + target.getPath()
      } else {
        dispatch(e.getTitle())
      }
    }
  }

  def visit(e: WtWhitespace) = {
    //	  if (e.getHasNewline()) {
    //	    cellContent += '\n'
    //	  }
  }

  def visit(e: WtXmlStartTag) = {
    val emptyElement = e.getName() match {
      case "br" => cellContent += '\n'; true
      case "p" => cellContent += '\n'; true
      case _ => false
    }

    if (!emptyElement) {
      val ignored = e.getName() match {
        case "small" if isSignificantXMLElement(e) => false
        case "big" => false
        case "abbr" => false
        case "center" => false
        case "span" if isSignificantXMLElement(e) => false
        case "div" => false
        case "noinclude" => false
        case "onlyinclude" => false
        case "includeonly" => true
        case "code" => false
        case _ => true
      }

      ignoredXMLStack.push(ignoredXMLElement || ignored)
    }
  }

  /**
   * Determine if an XML element should be included in the output
   */
  def isSignificantXMLElement(e: WtXmlStartTag): Boolean = {
    val attributes = e.getXmlAttributes()
    var significant = true

    val it = attributes.iterator()

    while (it.hasNext() && significant) {
      val attributeNode = it.next()
      if (attributeNode.isInstanceOf[WtXmlAttribute]) {
        val attribute = attributeNode.asInstanceOf[WtXmlAttribute]
        val name = attribute.getName().getAsString()

        val nodeToTextVisitor = new NodeToTextVisitor
        nodeToTextVisitor.go(attribute.getValue())
        val value = nodeToTextVisitor.getText

        significant = name match {
          case "class" if value.contains("plainlinks") => false
          case "class" if value.contains("flagicon") => false
          case "style" if value.contains("display:none") => false
          case _ => true
        }
      }

    }

    significant
  }

  def visit(e: WtXmlEndTag) = {
    if (ignoredXMLStack.nonEmpty) {
      ignoredXMLStack.pop
    }

  }

  def visit(e: WtXmlEmptyTag) = {
    e.getName() match {
      case "br" => cellContent += '\n'
      case _ =>
    }
  }

  def visit(e: WtXmlEntityRef) = {
    if (!ignoredXMLElement) {
      val value = e.getName() match {
        case "nbsp" => 160.toChar.toString
        case "times" => 215.toChar.toString
        case _ => ""
      }

      cellContent ++= value
    }
  }

  def visit(e: WtXmlCharRef) = {
    if (!ignoredXMLElement) {
      cellContent += e.getCodePoint().toChar
    }
  }

  def visit(e: WtXmlAttributeGarbage) = {
//    cellContent ++= e + "|" // FIXME : this line has been commented without thorough testing
  }

  def visit(e: WtDefinitionList) {
    val it = e.iterator()
    var first = true

    while (it.hasNext()) {
      val definition = it.next()

      // Each element of a definition list is separated by a line break
      if (first) {
        first = false
      } else {
        cellContent += '\n'
      }

      dispatch(definition)
    }
  }

  def visit(e: WtUnorderedList) {
    val it = e.iterator()
    while (it.hasNext()) {
      val item = it.next()
      dispatch(item)
      cellContent += '\n'
    }
  }

  override def visit(e: WtTableImplicitTableBody): Unit = iterate(e)

  override def visit(e: WtLinkOptionLinkTarget): Unit = iterate(e)

  override def visit(e: WtTemplateArguments): Unit = iterate(e)

  override def visit(e: WtValue): Unit = iterate(e)

  override def visit(e: WtLinkOptionGarbage): Unit = iterate(e)

  override def visit(e: WtNewline): Unit = {
    cellContent += '\n'
  }

  override def visit(e: WtPageName): Unit = iterate(e)

  override def visit(e: WtXmlElement): Unit = iterate(e)

  override def visit(e: WtImageLink): Unit = {

  }

  override def visit(e: WtTemplateParameter): Unit = iterate(e)

  override def visit(e: WtHorizontalRule): Unit = iterate(e)

  override def visit(e: WtIllegalCodePoint): Unit = iterate(e)

  override def visit(e: WtLinkOptionKeyword): Unit = iterate(e)

  override def visit(e: WtLinkOptionResize): Unit = iterate(e)

  override def visit(e: WtImStartTag): Unit = {
    iterate(e)
  }

  override def visit(e: WtImEndTag): Unit = iterate(e)

  override def visit(e: WtUrl): Unit = iterate(e)

  override def visit(e: WtTicks): Unit = iterate(e)

  override def visit(e: WtSignature): Unit = iterate(e)

  override def visit(e: WtPageSwitch): Unit = iterate(e)

  override def visit(e: WtTableCaption): Unit = iterate(e)

  override def visit(e: WtSection): Unit = iterate(e)

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

  override def visit(e: WtParagraph): Unit = iterate(e)

  override def visit(e: WtSemiPre): Unit = {
    iterate(e)
  }

  override def visit(e: WtSemiPreLine): Unit = {
    cellContent += ' '
    iterate(e)
  }

  override def visit(e: WtTagExtensionBody): Unit = iterate(e)

  override def visit(e: WtRedirect): Unit = iterate(e)

  override def visit(e: WtTagExtension): Unit = iterate(e)

  override def visit(e: WtTemplate): Unit = iterate(e)

  override def visit(e: WtIgnored): Unit = iterate(e)

  override def visit(e: WtXmlComment): Unit = iterate(e)

  override def visit(e: WtPreproWikitextPage): Unit = iterate(e)

  override def visit(e: WtTemplateArgument): Unit = iterate(e)

  def visit(e: WtLinkTarget.WtNoLink): Unit = {}
}