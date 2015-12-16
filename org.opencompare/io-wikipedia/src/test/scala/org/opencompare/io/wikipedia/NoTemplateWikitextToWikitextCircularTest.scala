package org.opencompare.io.wikipedia

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.PCMCircularTest
import org.opencompare.io.wikipedia.io.{WikiTextExporter, MediaWikiAPI, WikiTextTemplateProcessor, WikiTextLoader}

/**
  * Created by gbecan on 15/12/15.
  */
class NoTemplateWikitextToWikitextCircularTest extends PCMCircularTest(
  datasetPath = "wikitext/",
  pcmFactory = new PCMFactoryImpl,
  initLoader = new WikiTextLoader(new WikiTextTemplateProcessor(new MediaWikiAPI("wikipedia.org") {
    override def expandTemplate(language: String, template: String): String = {
      val result = template
        .replaceAll("\\{", "")
        .replaceAll("\\}", "")
        .replaceAll("\\|", "")

      result
    }
  })),
  exporter = new WikiTextExporter,
  importer = new WikiTextLoader(new WikiTextTemplateProcessor(new MediaWikiAPI("wikipedia.org")) {
    override def expandTemplate(language: String, template: String): String = {
      template
        .replaceAll("\\{", "")
        .replaceAll("\\}", "")
        .replaceAll("\\|", "")
    }

  })
)