package org.opencompare.io.wikipedia

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{CSVLoader, PCMCircularTest}
import org.opencompare.io.wikipedia.io.{MediaWikiAPI, WikiTextExporter, WikiTextLoader, WikiTextTemplateProcessor}

/**
 * Created by smangin on 01/06/15.
 */
class WikitextToWikitextCircularTest extends PCMCircularTest(
  datasetPath = "wikitext/",
  pcmFactory = new PCMFactoryImpl,
  initLoader = new WikiTextLoader(new WikiTextTemplateProcessor(new MediaWikiAPI("wikipedia.org"))),
  exporter = new WikiTextExporter,
  importer = new WikiTextLoader(new WikiTextTemplateProcessor(new MediaWikiAPI("wikipedia.org")))
)