package org.opencompare.io.wikipedia.io

import java.io.{InputStream, FileInputStream, BufferedInputStream}

import org.opencompare.api.java.{PCMMetadata, PCMContainer, PCM}
import org.opencompare.api.java.io.PCMExporter

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 26/11/14.
 */
class WikiTextExporter  extends PCMExporter {

  override def export(container: PCMContainer): String = {
    val builder = new StringBuilder
    val pcm = container.getPcm

    builder ++= "{| class=\"wikitable\"\n" // new table
    val title = pcm.getName
    builder ++= "|+ " + title + "\n" // caption

    // Headers (features)
    builder ++= "|-\n" // new row
    builder ++= "|\n" // empty top left cell

//    for (feature <- pcm.getConcreteFeatures.sortBy(_.getName)) {
    for (feature <- container.getMetadata.getSortedFeatures) {
      builder ++= "! " // new header
      builder ++= feature.getName
      builder ++= "\n"
    }

    // Lines (products)
//    for (product <- pcm.getProducts.sortBy(_.getName)) {
    for (product <- container.getMetadata.getSortedProducts) {

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
