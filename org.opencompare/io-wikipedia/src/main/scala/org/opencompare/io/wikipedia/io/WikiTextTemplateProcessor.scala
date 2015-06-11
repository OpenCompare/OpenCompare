package org.opencompare.io.wikipedia.io

import java.io.StringReader

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import org.xml.sax.InputSource

import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter
import scalaj.http.{HttpOptions, Http}

/**
 * Created by gbecan on 6/11/15.
 */
class WikiTextTemplateProcessor(initialCache : Map[String, String] = Map.empty[String, String]) {

  // Constructor for Java compatibility with default parameters
  def this() {
    this(Map.empty[String, String])
  }

  val templateCache: collection.mutable.Map[String, String] = collection.mutable.Map()
  initialCache.foreach(templateCache += _)

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

}
