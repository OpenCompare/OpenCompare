package org.opencompare.api.java.impl

import java.net.URL

import org.opencompare.api.java.io.{CSVExporter, CSVLoader}
import org.opencompare.api.java.{ImportTest, PCMFactory}
/**
 * Created by smangin on 01/06/15.
 */
class CSVImportTestImpl extends ImportTest {

  override val pcmFactory = new PCMFactoryImpl()
  override val exporter = new CSVExporter
  override val loader = new CSVLoader(pcmFactory, ',', '"')
  override val resource = getClass.getResource("csv/")

}
