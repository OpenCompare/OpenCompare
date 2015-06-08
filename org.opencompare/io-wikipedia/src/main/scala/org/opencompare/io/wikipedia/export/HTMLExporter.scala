package org.opencompare.io.wikipedia.export

import org.opencompare.io.wikipedia.pcm.Page

import scala.xml.PrettyPrinter

/**
 * Created by gbecan on 19/11/14.
 */
class HTMLExporter {

  def export(page : Page) : String = {
    (new PrettyPrinter(80,2)).format(page.toHTML)
  }

}
