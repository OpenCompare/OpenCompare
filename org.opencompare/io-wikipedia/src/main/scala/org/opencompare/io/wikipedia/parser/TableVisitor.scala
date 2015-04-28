package org.opencompare.io.wikipedia.parser

import java.util.regex.Pattern

import de.fau.cs.osr.ptk.common.AstVisitor
import de.fau.cs.osr.ptk.common.ast.{AstNode, NodeList, Text}
import org.opencompare.io.wikipedia.pcm.{Cell, Matrix}
import org.sweble.wikitext.`lazy`.parser._
import org.sweble.wikitext.`lazy`.postprocessor.{ImTagClose, ImTagOpen}
import org.sweble.wikitext.`lazy`.utils.{XmlAttribute, XmlAttributeGarbage, XmlCharRef, XmlEntityRef}

import scala.collection.mutable.{ListBuffer, Stack}

class TableVisitor extends AstVisitor {

	val matrices : ListBuffer[Matrix] = ListBuffer()
  
	private val matrixStack : Stack[Matrix] = new Stack()
	private def currentMatrix = matrixStack.top
	
	private val rowStack : Stack[Int] = new Stack()
	private val columnStack : Stack[Int] = new Stack()
	
	private var row : Int = 0
	private var column : Int = 0
	
	private var rowspan : Int = 0
	private var colspan : Int = 0
	
	private var cellContent : StringBuilder = new StringBuilder
		
	private val trimPattern : Pattern = Pattern.compile("\\s*([\\s\\S]*?)\\s*")
	
	private val ignoredXMLStack : Stack[Boolean] = new Stack()
	
	private def ignoredXMLElement : Boolean = {
		if (!ignoredXMLStack.isEmpty) { 
			ignoredXMLStack.top 
		} else {
			false
		}
	}
	
	/**
	 * Remove spaces before and after the string
	 */
	def trim(s : String) : String = {
	  val matcher = trimPattern.matcher(s)
	  if (matcher.matches() && matcher.groupCount() == 1) {
		  matcher.group(1)
	  } else {
		  ""
	  }
	}
	
	def visit(e : Table) = {
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
	  if (!rowStack.isEmpty && !columnStack.isEmpty) {
		  row = rowStack.top
		  column = columnStack.top  
	  }
	}

	def visit(e : NodeList) = {
		iterate(e)
	}

	def visit(e : XmlAttribute) = {
	  	val name = e.getName()

	  	if (!e.getValue().isEmpty()) {
	  		val value = e.getValue().get(0) match {
	  			case t:Text => t.getContent() 
	  			case _ => ""
	  		}
	  	
	  		name match {
	  		case "rowspan" => rowspan = getNumberFromString(value)
	  		case "colspan" => colspan = getNumberFromString(value)
	  		case _ => 
	  		}
	  	}

	}
	
	def getNumberFromString(s: String) : Int = {
	  val numberRegex = "(\\d)*".r
	  (numberRegex findFirstIn s).getOrElse("0").toInt
	}
		
	def visit(e : TableRow) = {
	  if (row == 0 && !currentMatrix.cells.isEmpty) {
	    row += 1
	    column = 0
	  }
	  if (!e.getBody().isEmpty()) {
		  iterate(e)
		  row += 1
		  column = 0  
	  }
	}

	def visit(e : TableHeader) = {
		handleCell(e, true)
	}

	def visit(e : TableCell) = {
		handleCell(e, false)
	}
	
	def handleCell(e : AstNode, isHeader : Boolean) {
		rowspan = 1
		colspan = 1

		if (!ignoredXMLElement) {
			// Skip cells defined by rowspan
			while (currentMatrix.getCell(row, column).isDefined) {
				column += 1
			}
		}

		cellContent = new StringBuilder()
		iterate(e)
		
		if(!ignoredXMLElement) {
		  if (cellContent.toString.startsWith("||")) {
			  cellContent.delete(0, 2)
			  currentMatrix.setCell(new Cell("", false, row, 1, column ,1), row, column)
			  column +=1
		  }
		  
		  val cell = new Cell(trim(cellContent.toString), isHeader, row, rowspan, column, colspan)
			
		  // Handle rowspan and colspan
		  for (rowShift <- 0 until rowspan; colShift <- 0 until colspan) {
			  currentMatrix.setCell(cell, row + rowShift, column + colShift)
		  }
		  
		  column += colspan
		}
	} 

	
	def visit(e : ImTagOpen) = {

	}

	def visit(e : ImTagClose) = {

	}

	def visit(e : Text) = {
	  if (!ignoredXMLElement) {
	    cellContent ++= e.getContent()
	  }
	  iterate(e) 
	}

	def visit(e : InternalLink) = {
	  if (!ignoredXMLElement) {
		  if (e.getTitle().getContent().isEmpty()) {
		    cellContent ++= e.getTarget()
		  } else if (!e.getTarget().endsWith(".png")){
		    dispatch(e.getTitle())
		  }
	  }
	}
	
	def visit(e : ExternalLink) = {
	  if (!ignoredXMLElement) {
		  if (e.getTitle().isEmpty()) {
//		    val target = e.getTarget()
//		    cellContent ++= target.getProtocol() + ":" + target.getPath()
		  } else {
		    dispatch(e.getTitle())
		  }
	  }
	}
	
	def visit(e : LinkTitle) = {
	  iterate(e)
	}

	def visit(e : Whitespace) = {
//	  if (e.getHasNewline()) {
//	    cellContent += '\n'
//	  }
	}

	def visit(e : XmlElementOpen) = {
	  val emptyElement = e.getName() match {
	    case "br" => cellContent += '\n'; true
	    case "p" => cellContent += '\n'; true
	    case _ => false
	  }
	  
	  if (!emptyElement) {
		  val ignored = e.getName() match {
		    case "small" if isSignificantXMLElement(e)=> false
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
	def isSignificantXMLElement(e : XmlElementOpen) : Boolean = {
		val attributes = e.getXmlAttributes()
		var significant = true;
		
		val it = attributes.iterator()
		
		while (it.hasNext() && significant) {
			val attribute = it.next().asInstanceOf[XmlAttribute]
			val name = attribute.getName()
			
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

	def visit(e : XmlElementClose) = {
	  if (!ignoredXMLStack.isEmpty) {
		  ignoredXMLStack.pop
	  }

	}

	def visit(e : XmlElementEmpty) = {
		e.getName() match {
		case "br" => cellContent += '\n'
		case _ =>
		}
	}

	def visit(e : XmlEntityRef) = {
	  if (!ignoredXMLElement) {
	    val value = e.getName() match {
	      case "nbsp" => 160.toChar.toString
	      case "times" => 215.toChar.toString
	      case _ => ""
	    }
	    
	    cellContent ++= value
	  }
	}

	def visit(e : XmlCharRef) = {
	  if (!ignoredXMLElement) {
		  cellContent += e.getCodePoint().toChar
	  }
	} 
	
	def visit(e : XmlAttributeGarbage) = {
		cellContent ++= e.getContent() + "|"
	}

	def visit(e : Url) = {
//		println(e.getPath())
	}
	
	def visit(e : Ticks) = {
	  
    }
	
	def visit(e : SemiPre) = {

	}
	
	def visit(e : TableCaption) {
	  
	}
	
	def visit(e : DefinitionList) {
	  val it = e.getContent().iterator()
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
	
	def visit(e : DefinitionDefinition) {
	  iterate(e)
	}
	
	def visit(e : Section) {
	  
	}
	
	def visit(e : Itemization) {
		val it = e.getContent().iterator()
		while(it.hasNext()) {
		  val item = it.next()
		  dispatch(item)
		  cellContent += '\n'
		}
	}

	def visit(e : ItemizationItem) {
		iterate(e)
	}

  def visit(e : Enumeration) {
    iterate(e)
  }
  
  def visit(e : EnumerationItem) {
    iterate(e)
  }
	
}