package org.opencompare.api.java.impl

import java.net.URL

import org.opencompare.api.java.io.{PCMLoader, PCMExporter, CSVExporter, CSVLoader}
import org.opencompare.api.java.{CircularImportTest, PCMFactory}
/**
 * Created by smangin on 01/06/15.
 */
class CSVCircularImportTestImpl extends CircularImportTest(
  getClass.getClassLoader.getResource("csv/"),
  new PCMFactoryImpl(),
  new CSVExporter,
  new CSVLoader(new PCMFactoryImpl(), ',', '"'))
