package org.opencompare.experimental.io.wikipedia.parser

import java.io.StringReader

import de.fau.cs.osr.ptk.common.AstVisitor
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.xml.sax.InputSource

import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter
import scalaj.http.{Http, HttpOptions}
import org.sweble.wikitext.parser.nodes._

class PreprocessVisitor extends AstVisitor[WtNode] with CompleteWikitextVisitorNoReturn {

  val code = new StringBuilder

  var isInTemplateName = false
  var templateName = new StringBuilder
  var isInTemplateArg = false
  var templateArg = new StringBuilder

  val templateCache: collection.mutable.Map[String, String] = collection.mutable.Map()

  def getPreprocessedCode(): String = {
    code.toString
  }

  /**
   * Expand template in WikiCode with the special page on English version of Wikipedia
   */
  def expandTemplate(template: String): String = {
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
      val textareas = xml \\ "textarea"
      var expandedTemplate = textareas.filter(_.attribute("id") exists (_.text == "output")).text

      // Remove line breaks
      if (expandedTemplate.length >= 2) {
        expandedTemplate = expandedTemplate.substring(1, expandedTemplate.length - 1)
      }

      // Add template to cache
      templateCache += template -> expandedTemplate

      expandedTemplate
    }
  }

  /**
   * Clean HTML to get strict XML
   */
  private def parseHTMLAsXML(htmlCode: String): Node = {
    val adapter = new NoBindingFactoryAdapter
    val htmlParser = (new SAXFactoryImpl).newSAXParser()
    val xml = adapter.loadXML(new InputSource(new StringReader(htmlCode)), htmlParser)
    xml
  }

  def visit(e: WtPreproWikitextPage) {
    iterate(e)
  }

  def visit(e: WtNodeList) {
    iterate(e)
  }

  def visit(e: WtXmlComment) {

  }

  def visit(e: WtText) {
    if (isInTemplateName) {
      templateName ++= e.getContent()
    } else if (isInTemplateArg) {
      templateArg ++= e.getContent()
    } else {
      code ++= e.getContent()
    }
  }

  def visit(e: WtTemplate) {
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
    val expandedTemplate = expandTemplate(template.toString())
    //    println("-----")
    //    println(e)
    //    println(template.toString)
    //    println(expandedTemplate)
    //    println("-----")
    code ++= expandedTemplate
  }

  def visit(e: WtTemplateArgument) {
    if (!e.getName().isEmpty()) {
      dispatch(e.getName())
      templateArg ++= "="
    }
    dispatch(e.getValue())
  }

  def visit(e: WtIgnored) {

  }

  def visit(e: WtTagExtension) {
    // a TagExtension is a reference which may contain usefull information
  }

  def visit(e: WtRedirect) {

  }

  override def visit(e: WtTableImplicitTableBody): Unit = iterate(e)

  override def visit(e: WtLinkOptionLinkTarget): Unit = iterate(e)

  override def visit(e: WtTableHeader): Unit = iterate(e)

  override def visit(e: WtTableRow): Unit = iterate(e)

  override def visit(e: WtTemplateArguments): Unit = iterate(e)

  override def visit(e: WtUnorderedList): Unit = iterate(e)

  override def visit(e: WtValue): Unit = iterate(e)

  override def visit(e: WtWhitespace): Unit = iterate(e)

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

  override def visit(e: WtInternalLink): Unit = iterate(e)

  override def visit(e: WtExternalLink): Unit = iterate(e)

  override def visit(e: WtXmlEntityRef): Unit = iterate(e)

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

  override def visit(e: WtDefinitionList): Unit = iterate(e)

  override def visit(e: WtParagraph): Unit = iterate(e)

  override def visit(e: WtSemiPre): Unit = iterate(e)

  override def visit(e: WtSemiPreLine): Unit = iterate(e)

  override def visit(e: WtXmlAttributeGarbage): Unit = iterate(e)

  override def visit(e: WtTagExtensionBody): Unit = iterate(e)
}