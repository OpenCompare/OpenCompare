package org.opencompare.api.java.impl.io

import org.opencompare.api.java.extractor.CellContentInterpreter
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.{CSVLoader, PCMCircularTest}


/**
  * Created by gbecan on 02/12/15.
  */
class KMFJsonCircularTest extends PCMCircularTest(
  datasetPath = "csv/",
  pcmFactory = new PCMFactoryImpl,
  initLoader = new CSVLoader(new PCMFactoryImpl, new CellContentInterpreter(new PCMFactoryImpl)),
  exporter = new KMFJSONExporter(),
  importer = new KMFJSONLoader()
) {

}
