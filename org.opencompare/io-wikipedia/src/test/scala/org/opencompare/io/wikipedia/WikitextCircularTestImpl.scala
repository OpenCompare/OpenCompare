package org.opencompare.io.wikipedia

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.CSVLoader
import org.opencompare.io.wikipedia.export.WikiTextExporter
import org.opencompare.api.java.CircularTest

/**
 * Created by smangin on 01/06/15.
 */
class WikitextCircularTestImpl extends CircularTest(
  resource = getClass.getClassLoader.getResource("CircularTest/"),
  pcmFactory = new PCMFactoryImpl,
  initLoader = new CSVLoader(new PCMFactoryImpl(), ',', '"'),
  exporter = new WikiTextExporter,
  importer = new WikipediaPageMiner
)
