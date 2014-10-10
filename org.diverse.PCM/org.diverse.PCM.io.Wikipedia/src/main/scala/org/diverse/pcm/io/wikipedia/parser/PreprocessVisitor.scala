package parser

import de.fau.cs.osr.ptk.common.AstVisitor
import org.sweble.wikitext.`lazy`.preprocessor.LazyPreprocessedPage
import de.fau.cs.osr.ptk.common.ast.NodeList
import org.sweble.wikitext.`lazy`.preprocessor.XmlComment
import de.fau.cs.osr.ptk.common.ast.Text
import org.sweble.wikitext.`lazy`.preprocessor.Template
import org.sweble.wikitext.`lazy`.preprocessor.Ignored
import scalaj.http.Http
import scalaj.http.HttpOptions
import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.xml.sax.InputSource
import java.io.StringReader
import org.sweble.wikitext.`lazy`.preprocessor.TemplateArgument
import org.sweble.wikitext.`lazy`.preprocessor.TagExtension
import scala.collection.immutable.Map
import scala.collection.immutable.HashMap
import org.sweble.wikitext.`lazy`.preprocessor.Redirect

class PreprocessVisitor extends AstVisitor {
  
  val code = new StringBuilder
  
  var isInTemplateName = false
  var templateName = new StringBuilder
  var isInTemplateArg = false
  var templateArg = new StringBuilder
  
  val templateCache : collection.mutable.Map[String, String] = collection.mutable.Map()
  
  def getPreprocessedCode() : String = {
    code.toString
  }
  
   /**
   * Expand template in WikiCode with the special page on English version of Wikipedia
   */
  def expandTemplate(template : String) : String = {
    val cachedTemplate = templateCache.get(template)
    if (cachedTemplate.isDefined) {
      cachedTemplate.get
    } else {
    	// Ask expanded template
	    val expandTemplatesPage = Http.post("https://en.wikipedia.org/wiki/Special:ExpandTemplates")
	     .params("wpInput" -> template, "wpRemoveComments" -> "1", "wpRemoveNowiki" -> "1")
	     .option(HttpOptions.connTimeout(1000))
		 .option(HttpOptions.readTimeout(30000))
	     .asString

	    // Filter the returned page
	    val xml = parseHTMLAsXML(expandTemplatesPage)
	    val textareas = (xml \\ "textarea")
	    var expandedTemplate = textareas.filter(_.attribute("id") exists (_.text == "output")).text

	    // Remove line breaks
	    if (expandedTemplate.size >= 2) {
	    	expandedTemplate = expandedTemplate.substring(1, expandedTemplate.size - 1 )  
	    }
	    
	    // Add template to cache
	    templateCache += template -> expandedTemplate
	    
	    expandedTemplate
    }
  }
  
   /**
   * Clean HTML to get strict XML
   */
  private def parseHTMLAsXML(htmlCode : String) : Node = {
    val adapter = new NoBindingFactoryAdapter
    val htmlParser = (new SAXFactoryImpl).newSAXParser()
    val xml = adapter.loadXML(new InputSource(new StringReader(htmlCode)), htmlParser)
    xml
  }

  def visit (e : LazyPreprocessedPage) {
    iterate(e)
  }
  
  def visit(e : NodeList) {
    iterate(e)
  }
  
  def visit(e : XmlComment) {
    
  }
  
  def visit(e : Text) {
	if (isInTemplateName) {
	  templateName ++= e.getContent()
	} else if (isInTemplateArg) {
	  templateArg ++= e.getContent()
	} else {
		code ++= e.getContent()  
	}
  }
  
  def visit(e : Template) {
    val template = new StringBuilder
    template ++= "{{"
    
    // Parse name of the template
    isInTemplateName = true
    dispatch(e.getName())
    template ++= templateName
    templateName = new StringBuilder
    isInTemplateName = false
    
    // Parse arguments of the template
    isInTemplateArg = true
    val argIterator = e.getArgs().iterator()
    while (argIterator.hasNext()) {
      template ++= "|" 

      val arg = argIterator.next()
      templateArg = new StringBuilder
      dispatch(arg)
      
      template ++= templateArg
    }
    isInTemplateArg = false
    
    template ++= "}}"
    
    // Call special page on wikipedia to expand the template
    val expandedTemplate = expandTemplate(template.toString)
//    println("-----")
//    println(e)
//    println(template.toString)
//    println(expandedTemplate)
//    println("-----")
    code ++= expandedTemplate
  }
  
  def visit(e : TemplateArgument) {
    if (!e.getName().isEmpty()) {
    	dispatch(e.getName())
    	templateArg ++= "="  
    }
    dispatch(e.getValue())
  }
  
  def visit(e : Ignored) {
    
  }
  
  def visit(e : TagExtension) {
    // a TagExtension is a reference which may contain usefull information
  }
  
  def visit(e : Redirect) {
    
  }
  
}