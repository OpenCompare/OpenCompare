package org.opencompare.api.java.impl

import java.net.URL

import org.opencompare.api.java.io.{PCMLoader, PCMExporter, CSVExporter, CSVLoader}
import org.opencompare.api.java.{CircularImportExportTest, PCMFactory}
/**
 * Created by smangin on 01/06/15.
 */
class CSVCircularImportExportTestImpl extends CircularImportExportTest {

  override val resource : URL = getClass.getResource("csv/")
  override val pcmFactory : PCMFactory = new PCMFactoryImpl()
  override val exporter : PCMExporter = new CSVExporter
  override val loader : PCMLoader = new CSVLoader(pcmFactory, ',', '"')

}
