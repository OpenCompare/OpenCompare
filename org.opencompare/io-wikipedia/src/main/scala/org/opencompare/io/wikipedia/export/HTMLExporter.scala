package org.opencompare.io.wikipedia.export

import org.opencompare.api.java.PCM
import org.opencompare.api.java.io.PCMExporter
import org.opencompare.io.wikipedia.pcm.Page

import scala.xml.PrettyPrinter

/**
 * Created by gbecan on 19/11/14.
 */
class HTMLExporter extends PCMExporter {

  def export(page : Page) : String = {
    (new PrettyPrinter(80,2)).format(page.toHTML)
  }

  override def export(pcm: PCM): String = ""
}
