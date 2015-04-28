package org.opencompare.io.wikipedia.parser

import java.util.regex.Pattern

import de.fau.cs.osr.ptk.common.AstVisitor
import de.fau.cs.osr.ptk.common.ast.{NodeList, Text}
import org.opencompare.io.wikipedia.pcm.{Matrix, Page}
import org.sweble.wikitext.`lazy`.parser.{DefinitionList, Enumeration, ExternalLink, HorizontalRule, InternalLink, Itemization, LazyParsedPage, Section, SemiPre, Table, Ticks, Url, Whitespace, XmlElementClose, XmlElementEmpty, XmlElementOpen}
import org.sweble.wikitext.`lazy`.postprocessor.{ImTagClose, ImTagOpen}
import org.sweble.wikitext.`lazy`.utils.{XmlCharRef, XmlEntityRef}

import scala.collection.mutable.ListBuffer

class PageVisitor(pageTitle : String) extends AstVisitor{


  var matrices : ListBuffer[Matrix] = ListBuffer()
  val pcm : Page = new Page
  pcm.title = pageTitle
  var section : StringBuilder = new StringBuilder
  var inTitle : Boolean = false

  
  
  	private val trimPattern : Pattern = Pattern.compile("\\s*([\\s\\S]*?)\\s*")
	/**
	 * Remove spaces before and after the string
	 */
	def trim(s : String) : String = {
	  val matcher = trimPattern.matcher(s)
	  var trimmedString = if (matcher.matches() && matcher.groupCount() == 1) {
		  matcher.group(1)
	  } else {
		  ""
	  }
    trimmedString = trimmedString.replaceAll("_", " ")
    trimmedString
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
      val sectionTitle = section.toString
      if (sectionTitle.isEmpty) {
        matrix.name = trim(pageTitle)
      } else {
        matrix.name = trim(pageTitle + " - " + sectionTitle)
      }

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