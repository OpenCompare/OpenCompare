package org.diverse.pcm.io.wikipedia.export

import org.diverse.pcm.api.java.PCM
import collection.JavaConversions._

/**
 * Created by gbecan on 26/11/14.
 */
class WikiTextExporter {

  def toWikiText(pcm : PCM) : String = {

    val builder = new StringBuilder

    builder ++= "{| class=\"wikitable\"\n" // new table

    val title = pcm.getName
    builder ++= "|+ " + title + "\n" // caption

    // Headers (features)
    builder ++= "|-\n" // new row
    builder ++= "|\n" // empty top left cell

    for (feature <- pcm.getFeatures.sortBy(_.getName)) {
      builder ++= "! " // new cell (we can use || also to separate cells horizontally)
      builder ++= feature.getName
      builder ++= "\n"
    }

    // Lines (products)
    for (product <- pcm.getProducts.sortBy(_.getName)) {
      builder ++= "|-\n" // new cell
      builder ++= "! "
      builder ++= product.getName
      builder ++= "\n"

      for (cell <- product.getCells.sortBy(_.getFeature.getName)) {
        builder ++= "| "
        builder ++= cell.getContent
        builder ++= "\n"
      }
    }

    builder ++= "|}" //  end table

    builder.toString()
  }

}



//"! scope=\"col\"" // column header (may be unnecessary with class="wikitable")
//"! scope=\"row\"" // row header