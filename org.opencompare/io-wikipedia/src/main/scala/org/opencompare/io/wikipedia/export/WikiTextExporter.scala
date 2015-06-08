package org.opencompare.io.wikipedia.export

import org.opencompare.api.java.PCM
import org.opencompare.api.java.io.PCMExporter

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

    for (feature <- pcm.getConcreteFeatures.sortBy(_.getName)) {
      builder ++= "! " // new header
      builder ++= feature.getName
      builder ++= "\n"
    }

    // Lines (products)
    for (product <- pcm.getProducts.sortBy(_.getName)) {

      // Product name header
      builder ++= "|-\n"
      builder ++= "! "
      builder ++= product.getName
      builder ++= "\n"

      // Cells
      for (cell <- product.getCells.sortBy(_.getFeature.getName)) {
        builder ++= "| " // new cell (we can also use || to separate cells horizontally)
        builder ++= cell.getContent
        builder ++= "\n"
      }
    }

    builder ++= "|}" //  end table

    builder.toString()
  }

}
