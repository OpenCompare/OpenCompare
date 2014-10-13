package org.diverse.pcm.io.wikipedia.parser

import de.fau.cs.osr.ptk.common.AstVisitor
import org.diverse.pcm.io.wikipedia.pcm.{Page, Matrix}
import org.sweble.wikitext.`lazy`.parser.LazyParsedPage
import de.fau.cs.osr.ptk.common.ast.NodeList
import org.sweble.wikitext.`lazy`.postprocessor.ImTagOpen
import de.fau.cs.osr.ptk.common.ast.Text
import org.sweble.wikitext.`lazy`.parser.Whitespace
import org.sweble.wikitext.`lazy`.postprocessor.ImTagClose
import org.sweble.wikitext.`lazy`.parser.Table
import org.sweble.wikitext.`lazy`.parser.Section
import de.fau.cs.osr.ptk.common.ast.AstNode
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ListBuffer
import org.sweble.wikitext.`lazy`.parser.Itemization
import org.sweble.wikitext.`lazy`.parser.InternalLink
import org.sweble.wikitext.`lazy`.utils.XmlAttributeGarbage
import org.sweble.wikitext.`lazy`.preprocessor.LazyPreprocessedPage
import org.sweble.wikitext.`lazy`.parser.DefinitionList
import org.sweble.wikitext.`lazy`.parser.XmlElementClose
import org.sweble.wikitext.`lazy`.parser.XmlElementOpen
import org.sweble.wikitext.`lazy`.parser.SemiPre
import org.sweble.wikitext.`lazy`.parser.Ticks
import org.sweble.wikitext.`lazy`.parser.ExternalLink
import org.sweble.wikitext.`lazy`.parser.XmlElementEmpty
import org.sweble.wikitext.`lazy`.utils.XmlEntityRef
import org.sweble.wikitext.`lazy`.parser.Url
import org.sweble.wikitext.`lazy`.utils.XmlCharRef
import org.sweble.wikitext.`lazy`.parser.Enumeration
import org.sweble.wikitext.`lazy`.parser.HorizontalRule
import java.util.regex.Pattern

class PageVisitor extends AstVisitor{
  
  var matrices : ListBuffer[Matrix] = ListBuffer()
  val pcm : Page = new Page
  var section : StringBuilder = new StringBuilder
  var inTitle : Boolean = false
  
  
  	private val trimPattern : Pattern = Pattern.compile("\\s*([\\s\\S]*?)\\s*")
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
  
  def visit(e : LazyParsedPage) {
    iterate(e)
  }
  
  def visit(e : NodeList) {
    iterate(e)
  }
  
  def visit(e : Table) {
	  val tableVisitor = new TableVisitor
	  tableVisitor.go(e)
	  for (matrix <- tableVisitor.matrices) {
	    matrix.name = trim(section.toString)
	    pcm.addMatrix(matrix)
	  }
  }
  
  def visit(e : ImTagOpen) {
    
  }
  
  def visit(e : ImTagClose) {

  }
  
  
  def visit(e : Text) {
	  if (inTitle) {
	    section ++= e.getContent()
	  }
  }
  
  def visit(e : Whitespace) {

  }
  
  def visit(e : Section) {
	  inTitle = true
	  section = new StringBuilder
	  dispatch(e.getTitle())
	  inTitle = false
	  iterate(e)
  }
  
  def visit(e : Itemization) {
    
  }
  
  def visit(e : InternalLink) {
    if (inTitle) {
    	val nodeToText = new NodeToTextVisitor
    	nodeToText.go(e)
    	section ++= nodeToText.getText
    }
  }
  
  def visit(e : ExternalLink) {
	  
  }
  
  def visit(e : DefinitionList) {
    
  }
  
  def visit(e : XmlElementOpen) {
    
  }
  
  def visit(e : XmlElementClose) {
    
  }
  
  def visit(e : XmlElementEmpty) {
    
  }

  def visit(e : XmlEntityRef) {

  }
  
  def visit(e : XmlCharRef) {

  }
  	
  def visit(e : SemiPre) {

  }
  
  def visit(e : Ticks) {

  }

  def visit(e : Url) {

  }
  
  def visit(e : Enumeration) {

  }
  
  def visit (e : HorizontalRule) {
    
  }


    
}