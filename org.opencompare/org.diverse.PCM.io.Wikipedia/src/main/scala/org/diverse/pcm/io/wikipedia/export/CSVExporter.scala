package org.diverse.pcm.io.wikipedia.export

import org.diverse.pcm.io.wikipedia.pcm.Page

/**
 * Created by gbecan on 19/11/14.
 */
class CSVExporter {

  def export(page : Page) : String = {
    page.toCSV()
  }

}
