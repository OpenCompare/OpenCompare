package org.opencompare.api.java.impl

import java.net.URL

import org.opencompare.api.java.io.{PCMLoader, PCMExporter, CSVExporter, CSVLoader}
import org.opencompare.api.java.{PCMCircularTest, PCMFactory}
/**
 * Created by smangin on 01/06/15.
 */
class CsvPCMCircularTestImpl extends PCMCircularTest(
  resource = getClass.getClassLoader.getResource("csv/"),
  pcmFactory = new PCMFactoryImpl,
  initLoader = new CSVLoader(new PCMFactoryImpl(), ',', '"'),
  exporter = new CSVExporter,
  importer = new CSVLoader(new PCMFactoryImpl(), ',', '"')
)
