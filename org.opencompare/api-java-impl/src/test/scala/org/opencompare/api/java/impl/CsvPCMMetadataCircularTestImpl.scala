package org.opencompare.api.java.impl

import org.opencompare.api.java.{PCMMetadataCircularTest, PCMCircularTest}
import org.opencompare.api.java.io.{CSVExporter, CSVLoader}

/**
 * Created by smangin on 01/06/15.
 */
class CsvPCMMetadataCircularTestImpl extends PCMMetadataCircularTest(
  resource = getClass.getClassLoader.getResource("csv/"),
  pcmFactory = new PCMFactoryImpl,
  initLoader = new CSVLoader(new PCMFactoryImpl(), ',', '"'),
  exporter = new CSVExporter,
  importer = new CSVLoader(new PCMFactoryImpl(), ',', '"')
)
