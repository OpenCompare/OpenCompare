package org.opencompare.api.scala.io

import org.opencompare.api.scala.interpreter.DefaultCellContentInterpreter

class JSONCircularTest extends CircularTest(
  datasetPath = "csv/",
  initLoader = new CSVLoader(new DefaultCellContentInterpreter, ',', '"'),
  exporter = new JSONExporter,
  importer = new JSONLoader){

}
