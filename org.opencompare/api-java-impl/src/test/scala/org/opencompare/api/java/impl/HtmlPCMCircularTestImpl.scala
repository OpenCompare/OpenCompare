package org.opencompare.api.java.impl

import org.opencompare.api.java.PCMCircularTest
import org.opencompare.api.java.io.{HTMLExporter, HTMLLoader, CSVExporter, CSVLoader}

/**
 * Created by smangin on 01/06/15.
 */
class HtmlPCMCircularTestImpl extends PCMCircularTest(
  resource = getClass.getClassLoader.getResource("html/"),
  pcmFactory = new PCMFactoryImpl,
  initLoader = new HTMLLoader(new PCMFactoryImpl()),
  exporter = new HTMLExporter,
  importer = new HTMLLoader(new PCMFactoryImpl())
)
