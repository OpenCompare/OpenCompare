package org.opencompare.api.scala.io

import org.opencompare.api.scala.interpreter.DefaultCellContentInterpreter


class CSVCircularTest() extends CircularTest(
  datasetPath = "csv/",
  initLoader = new CSVLoader(new DefaultCellContentInterpreter, ',', '"'),
  exporter = new CSVExporter,
  importer = new CSVLoader(new DefaultCellContentInterpreter, ',', '"')
)
