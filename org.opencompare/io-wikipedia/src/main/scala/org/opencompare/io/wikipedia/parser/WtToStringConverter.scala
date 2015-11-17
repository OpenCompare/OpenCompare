package org.opencompare.io.wikipedia.parser

import de.fau.cs.osr.utils.visitor.VisitingException
import org.joda.time.DateTime
import org.sweble.wikitext.engine.PageTitle
import org.sweble.wikitext.engine.config.WikiConfig
import org.sweble.wikitext.parser.nodes.{WtLctVarConv, WtNode}
import org.sweble.wom3.{Wom3ElementNode, Wom3Document}
import org.sweble.wom3.impl.DomImplementationImpl
import org.sweble.wom3.swcadapter.AstToWomConverter
import org.sweble.wom3.util.Wom3Toolbox

/**
 * Created by gbecan on 7/20/15.
 */
class WtToStringConverter(wikiConfig : WikiConfig) extends AstToWomConverter(wikiConfig) {

  def convert(wtNode : WtNode): Option[String] = {
    try {
      val pageTitle = PageTitle.make(wikiConfig, "title")
      val wom3Doc = DomImplementationImpl.get().createDocument("http://sweble.org/schema/wom30", "article", null).asInstanceOf[Wom3Document]
      convert(wom3Doc, pageTitle, "author", DateTime.now(), wtNode)
      Some(Wom3Toolbox.womToWmXPath(wom3Doc))
    } catch {
      case _ : VisitingException => None
      case _ : IllegalArgumentException => None
    }

  }


  def visit(wt : WtLctVarConv) : Wom3ElementNode = {
    null
  }

}
