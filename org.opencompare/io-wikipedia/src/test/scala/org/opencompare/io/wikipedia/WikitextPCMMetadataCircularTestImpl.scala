package org.opencompare.io.wikipedia

import org.opencompare.api.java.PCMMetadataCircularTest
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.CSVLoader
import org.opencompare.io.wikipedia.io.{MediaWikiAPI, WikiTextTemplateProcessor, WikiTextExporter, WikiTextLoader}

/**
 * Created by smangin on 01/06/15.
 */
class WikitextPCMMetadataCircularTestImpl extends PCMMetadataCircularTest(
  resource = getClass.getClassLoader.getResource("csv/"),
  pcmFactory = new PCMFactoryImpl,
  initLoader = new CSVLoader(new PCMFactoryImpl(), ',', '"'),
  exporter = new WikiTextExporter,
  importer = new WikiTextLoader(new WikiTextTemplateProcessor(new MediaWikiAPI("wikipedia.org")))
)
