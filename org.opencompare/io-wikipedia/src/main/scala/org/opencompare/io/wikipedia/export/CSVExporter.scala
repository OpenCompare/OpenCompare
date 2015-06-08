package org.opencompare.io.wikipedia.export

import org.opencompare.io.wikipedia.pcm.Page

/**
 * Created by gbecan on 19/11/14.
 */
class CSVExporter {

  def export(page : Page) : String = {
    page.toCSV()
  }

}
