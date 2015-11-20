package org.opencompare.api.java.io

import org.opencompare.api.java.PCMFactory

/**
  * Created by gbecan on 20/11/15.
  */
abstract class HtmlPCMCircularTest(val factory : PCMFactory) extends PCMCircularTest(
  datasetPath = "csv/",
  pcmFactory = factory,
  initLoader = new CSVLoader(factory),
  exporter = new HTMLExporter,
  importer = new HTMLLoader(factory)
)