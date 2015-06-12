package org.opencompare.api.java.impl

import org.opencompare.api.java.PCMCircularTest
import org.opencompare.api.java.impl.io.{KMFJSONExporter, KMFJSONLoader}
import org.opencompare.api.java.io.CSVLoader

/**
 * Created by smangin on 01/06/15.
 */
class JsonPCMCircularTestImpl extends PCMCircularTest(
  resource = getClass.getClassLoader.getResource("csv/"),
  pcmFactory = new PCMFactoryImpl,
  initLoader = new CSVLoader(new PCMFactoryImpl(), ',', '"'),
  exporter = new KMFJSONExporter,
  importer = new KMFJSONLoader
)
