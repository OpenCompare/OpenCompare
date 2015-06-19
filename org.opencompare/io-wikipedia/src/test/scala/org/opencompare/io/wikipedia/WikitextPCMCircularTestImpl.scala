package org.opencompare.io.wikipedia

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.CSVLoader
import org.opencompare.api.java.PCMCircularTest
import org.opencompare.io.wikipedia.io.{MediaWikiAPI, WikiTextTemplateProcessor, WikiTextLoader, WikiTextExporter}

/**
 * Created by smangin on 01/06/15.
 */
class WikitextPCMCircularTestImpl extends PCMCircularTest(
  resource = getClass.getClassLoader.getResource("csv/"),
  pcmFactory = new PCMFactoryImpl,
  initLoader = new CSVLoader(new PCMFactoryImpl(), ',', '"'),
  exporter = new WikiTextExporter,
  importer = new WikiTextLoader(new WikiTextTemplateProcessor(new MediaWikiAPI("wikipedia.org")))
)