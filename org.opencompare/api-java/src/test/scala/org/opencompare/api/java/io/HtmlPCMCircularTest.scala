package org.opencompare.api.java.io

import org.opencompare.api.java.PCMFactory
import org.opencompare.api.java.interpreter.CellContentInterpreter

/**
  * Created by gbecan on 20/11/15.
  */
abstract class HtmlPCMCircularTest(val factory : PCMFactory, val cellContentInterpreter: CellContentInterpreter) extends PCMCircularTest(
  datasetPath = "csv/",
  pcmFactory = factory,
  initLoader = new CSVLoader(factory, cellContentInterpreter),
  exporter = new HTMLExporter,
  importer = new HTMLLoader(factory, cellContentInterpreter)
)