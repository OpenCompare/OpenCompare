package org.opencompare.experimental.io.wikipedia.parser

import java.util.regex.Pattern

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text
import java.util.regex.Pattern

import de.fau.cs.osr.ptk.common.AstVisitor
import de.fau.cs.osr.ptk.common.ast.AstNode
import org.opencompare.io.wikipedia.pcm.{Cell, Matrix}
import org.sweble.wikitext.parser.nodes._

import scala.collection.mutable.{ListBuffer, Stack}

class TableVisitor extends AstVisitor[WtNode] with CompleteWikitextVisitorNoReturn {

  val matrices: ListBuffer[Matrix] = ListBuffer()

  private val matrixStack: Stack[Matrix] = new Stack()

  private def currentMatrix = matrixStack.top

  private val rowStack: Stack[Int] = new Stack()
  private val columnStack: Stack[Int] = new Stack()

  private var row: Int = 0
  private var column: Int = 0

  private var rowspan: Int = 0
  private var colspan: Int = 0

  private var cellContent: StringBuilder = new StringBuilder

  private val trimPattern: Pattern = Pattern.compile("\\s*([\\s\\S]*?)\\s*")

  private val ignoredXMLStack: Stack[Boolean] = new Stack()

  override def iterate(e: WtNode): Unit = {
    println("TableVisitor: " + e)
    super.iterate(e)
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
    val matrix = new Matrix
    matrixStack.push(matrix)
    matrices += matrix

    // Save old values of row and column
    rowStack.push(row)
    columnStack.push(column)
    row = 0
    column = 0

    // Iterate over each row
    iterate(e)

    matrixStack.pop

    // Clear previous cell
    cellContent = new StringBuilder()

    // Restore old values of row and column
    rowStack.pop
    columnStack.pop
    if (rowStack.nonEmpty && columnStack.nonEmpty) {
      row = rowStack.top
      column = columnStack.top
    }
  }

  def visit(e: WtNodeList) = {
    iterate(e)
  }

  def visit(e: WtXmlAttribute) = {
    val name = e.getName().getAsString()

    if (!e.getValue().isEmpty()) {
      val value = e.getValue().get(0) match {
        case t: Text => t.toString()
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
    val numberRegex = "(\\d)*".r
    var num = 0
    if (s.nonEmpty) {
      num = (numberRegex findFirstIn s).getOrElse("0").toInt
    }
    num
  }

  def visit(e: WtTableRow) = {
    if (row == 0 && currentMatrix.cells.nonEmpty) {
      row += 1
      column = 0
    }
    if (!e.getBody().isEmpty()) {
      iterate(e)
      row += 1
      column = 0
    }
  }

  def visit(e: WtTableHeader) = {
    handleCell(e, true)
  }

  def visit(e: WtTableCell) = {
    handleCell(e, false)
  }

  def handleCell(e: WtNode, isHeader: Boolean) {
    rowspan = 1
    colspan = 1

    if (!ignoredXMLElement) {
      // Skip cells defined by rowspan
      while (currentMatrix.getCell(row, column).isDefined) {
        column += 1
      }
    }

    cellContent = new StringBuilder()
    iterate(e) // TODO : why ?

    if (!ignoredXMLElement) {
      if (cellContent.toString().startsWith("||")) {
        cellContent.delete(0, 2)
        currentMatrix.setCell(new Cell("", false, row, 1, column, 1), row, column)
        column += 1
      }

      val cell = new Cell(trim(cellContent.toString()), isHeader, row, rowspan, column, colspan)

      // Handle rowspan and colspan
      for (rowShift <- 0 until rowspan; colShift <- 0 until colspan) {
        currentMatrix.setCell(cell, row + rowShift, column + colShift)
      }

      column += colspan
    }
  }

  def visit(e: WtText) = {
    if (!ignoredXMLElement) {
      cellContent ++= e.getContent()
    }
    iterate(e)
  }

  def visit(e: WtInternalLink) = {
    if (!ignoredXMLElement) {
      if (e.getTitle().isEmpty) {
        cellContent ++= e.getTarget().getAsString()
      } else if (!e.getTarget().getAsString.endsWith(".png")) {
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
      val attribute = it.next().asInstanceOf[WtXmlAttribute]
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
    cellContent ++= e + "|"
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

  override def visit(e: WtImStartTag): Unit = iterate(e)

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

  override def visit(e: WtSemiPre): Unit = iterate(e)

  override def visit(e: WtSemiPreLine): Unit = iterate(e)

  override def visit(e: WtTagExtensionBody): Unit = iterate(e)

  override def visit(e: WtRedirect): Unit = iterate(e)

  override def visit(e: WtTagExtension): Unit = iterate(e)

  override def visit(e: WtTemplate): Unit = iterate(e)

  override def visit(e: WtIgnored): Unit = iterate(e)

  override def visit(e: WtXmlComment): Unit = iterate(e)

  override def visit(e: WtPreproWikitextPage): Unit = iterate(e)

  override def visit(e: WtTemplateArgument): Unit = iterate(e)
}