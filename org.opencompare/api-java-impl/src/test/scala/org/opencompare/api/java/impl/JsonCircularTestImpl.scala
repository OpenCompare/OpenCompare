package org.opencompare.api.java.impl

import org.opencompare.api.java.CircularTest
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import org.opencompare.api.java.io.CSVLoader

/**
 * Created by smangin on 01/06/15.
 */
class JsonCircularTestImpl extends CircularTest(
  resource = getClass.getClassLoader.getResource("csv/"),
  pcmFactory = new PCMFactoryImpl,
  initLoader = new CSVLoader(new PCMFactoryImpl(), ',', '"'),
  exporter = new KMFJSONExporter,
  importer = new KMFJSONLoader
)
