package org.diverse.pcm.io.wikipedia.export

import org.diverse.pcm.io.wikipedia.pcm.Page

import scala.xml.PrettyPrinter

/**
 * Created by gbecan on 19/11/14.
 */
class HTMLExporter {

  def export(page : Page) : String = {
    (new PrettyPrinter(80,2)).format(page.toHTML)
  }

}
