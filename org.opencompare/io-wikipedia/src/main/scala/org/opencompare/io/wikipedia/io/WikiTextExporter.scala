package org.opencompare.io.wikipedia.io

import java.io.{InputStream, FileInputStream, BufferedInputStream}

import org.opencompare.api.java.PCM
import org.opencompare.api.java.io.PCMExporter

import scala.collection.JavaConversions._

/**
 * Created by gbecan on 26/11/14.
 */
class WikiTextExporter  extends PCMExporter {

  override def export(pcm: PCM): String = {
    new org.opencompare.io.wikipedia.export.WikiTextExporter().toWikiText(pcm)
  }

}
