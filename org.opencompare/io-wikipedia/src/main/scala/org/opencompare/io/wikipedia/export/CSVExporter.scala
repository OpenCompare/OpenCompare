package org.opencompare.io.wikipedia.export

import org.opencompare.api.java.PCM
import org.opencompare.api.java.io.PCMExporter
import org.opencompare.io.wikipedia.pcm.Page

/**
 * Created by gbecan on 19/11/14.
 */
class CSVExporter extends PCMExporter {

  def export(page : Page) : String = {
    page.toCSV()
  }

  override def export(pcm: PCM): String = ""
}
