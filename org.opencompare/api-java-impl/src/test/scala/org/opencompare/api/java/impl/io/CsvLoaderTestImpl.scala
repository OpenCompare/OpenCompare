package org.opencompare.api.java.impl.io

import org.opencompare.api.java.extractor.CellContentInterpreter
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.CsvScalaLoaderTest

/**
  * Created by gbecan on 20/11/15.
  */
class CsvLoaderTestImpl extends CsvScalaLoaderTest(new PCMFactoryImpl, new CellContentInterpreter(new PCMFactoryImpl))
