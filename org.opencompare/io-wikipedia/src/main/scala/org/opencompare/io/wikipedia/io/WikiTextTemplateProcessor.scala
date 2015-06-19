package org.opencompare.io.wikipedia.io

import java.io.StringReader

import org.xml.sax.InputSource

import scala.xml.Node
import scala.xml.parsing.NoBindingFactoryAdapter

/**
 * Created by gbecan on 6/11/15.
 */
class WikiTextTemplateProcessor(val mediaWikiAPI : MediaWikiAPI, initialCache : Map[String, String] = Map.empty[String, String]) {

  // Constructor for Java compatibility with default parameters
  def this(initMediaWikiAPI : MediaWikiAPI) {
    this(initMediaWikiAPI, Map.empty[String, String])
  }

  val templateCache: collection.mutable.Map[String, String] = collection.mutable.Map()
  initialCache.foreach(templateCache += _)

  def expandTemplate(language : String, template: String): String = {

    val cachedTemplate = templateCache.get(template)
    if (cachedTemplate.isDefined) {
      cachedTemplate.get
    } else {
      // Expand template
      val expandedTemplate = mediaWikiAPI.expandTemplate(language, template)

      // Add template to cache
      templateCache += template -> expandedTemplate

      expandedTemplate
    }
  }

}
