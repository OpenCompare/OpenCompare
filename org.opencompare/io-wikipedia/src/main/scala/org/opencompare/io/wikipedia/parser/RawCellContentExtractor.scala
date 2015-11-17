package org.opencompare.io.wikipedia.parser

import java.util.regex.Pattern

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

  private val trimPattern : Pattern = Pattern.compile("[\\s|!]*([\\s\\S]*?)\\s*")
  private val nestedTableChecker : NestedTableChecker = new NestedTableChecker
  private val wtToStringConverter : WtToStringConverter = new WtToStringConverter(wikiConfig)

  def extract(cell : WtNode) : String = {

    if (nestedTableChecker.hasNestedTable(cell)) {
      "" // FIXME : we do not support nested tables for now
    } else {
      val code = wtToStringConverter.convert(cell)

      if (code.isDefined) {
        trim(code.get)
      } else {
        ""
      }
    }

  }

  def trim(s: String): String = {
    val matcher = trimPattern.matcher(s)
    if (matcher.matches() && matcher.groupCount() == 1) {
      matcher.group(1)
    } else {
      ""
    }
  }

}
