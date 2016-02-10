package org.opencompare.api.java.impl.io

import org.opencompare.api.java.{extractor, PCMFactory}
import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.interpreter.CellContentInterpreter


/**
 * Created by gbecan on 10/2/15.
 */
class ImportMatrixLoaderTest extends org.opencompare.api.java.io.ImportMatrixLoaderTest{
  override val factory: PCMFactory = new PCMFactoryImpl
  override val cellContentInterpreter: CellContentInterpreter = new extractor.CellContentInterpreter(factory)
}
