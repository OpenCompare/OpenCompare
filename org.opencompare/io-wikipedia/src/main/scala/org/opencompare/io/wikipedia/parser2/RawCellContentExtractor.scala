package org.opencompare.io.wikipedia.parser2

import org.joda.time.DateTime
import org.sweble.wikitext.engine.PageTitle
import org.sweble.wikitext.engine.config.WikiConfig
import org.sweble.wikitext.parser.nodes._
import org.sweble.wom3.swcadapter.AstToWomConverter
import org.sweble.wom3.util.Wom3Toolbox

/**
 * Created by gbecan on 6/10/15.
 */
class RawCellContentExtractor(val wikiConfig : WikiConfig) {

  def extract(cell : WtNode) : String = {
    val pageTitle = PageTitle.make(wikiConfig, "title")
    val wom3Doc = AstToWomConverter.convert(wikiConfig, pageTitle, "author", DateTime.now(), cell)
    Wom3Toolbox.womToWmXPath(wom3Doc)
  }

}
