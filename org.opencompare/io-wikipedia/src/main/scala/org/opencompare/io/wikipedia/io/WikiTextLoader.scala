package org.opencompare.io.wikipedia.io

import java.io.File

import org.opencompare.api.java.PCM
import org.opencompare.api.java.io.PCMLoader
import org.opencompare.io.wikipedia.WikipediaPageMiner
import org.opencompare.io.wikipedia.export.PCMModelExporter

import scala.io.Source

/**
 * Created by  on 26/11/14.
 */
class WikiTextLoader  extends PCMLoader {

  val loader = new WikipediaPageMiner
  val exporter = new PCMModelExporter

  override def load(code: String): PCM = {
    exporter.export(loader.parse(loader.preprocess(code), "")).head
  }

  override def load(file: File): PCM = {
    this.load(Source.fromFile(file).mkString)
  }

}
