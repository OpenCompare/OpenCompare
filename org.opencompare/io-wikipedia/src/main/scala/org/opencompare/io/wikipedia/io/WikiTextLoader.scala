package org.opencompare.io.wikipedia.io

import java.io.File

import org.opencompare.api.java.PCM
import org.opencompare.api.java.io.PCMLoader
import org.opencompare.io.wikipedia.WikipediaPageMiner
import org.opencompare.io.wikipedia.export.PCMModelExporter

/**
 * Created by gbecan on 26/11/14.
 */
class WikiTextLoader  extends PCMLoader {

  val loader = new WikipediaPageMiner

  override def load(code: String): PCM = {
    loader.load(code)
  }

  override def load(file: File): PCM = {
    loader.load(file)
  }

}
