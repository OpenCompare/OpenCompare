package org.opencompare.api.java.impl.io

import org.opencompare.api.java.extractor.CellContentInterpreter
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.io.CsvPCMCircularTest
/**
 * Created by smangin on 01/06/15.
 */
class CsvPCMCircularTestImpl extends CsvPCMCircularTest(new PCMFactoryImpl, new CellContentInterpreter(new PCMFactoryImpl))
