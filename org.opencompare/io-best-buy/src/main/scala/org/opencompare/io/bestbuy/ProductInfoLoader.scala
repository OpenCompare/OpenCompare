package org.opencompare.io.bestbuy

import java.io.{FileReader, File}

import com.github.tototoshi.csv.CSVReader

import scala.io.Source
import scala.xml
import scala.xml.XML

/**
 * Created by gbecan on 4/7/15.
 */
class ProductInfoLoader {

  def load(overview : File, specification : File, xmlDescription : File) : ProductInfo = {

    val productInfo = new ProductInfo

    // TODO : productInfo.sku
    // TODO : productInfo.name

    // FIXME : not really deserializing the product info but it should be close enough for the experiment

    // Read overview
//    for (feature <- Source.fromFile(overview).getLines()) {
//      productInfo.addFeature(feature)
//    }

    // Read specification

    val csvReader = CSVReader.open(specification)
    val detailNames = csvReader.readNext().get
    val detailValues = csvReader.readNext().get
    val details = detailNames.zip(detailValues)
    for ((name, value) <- details) {
      productInfo.addDetail(name, value)
    }

    // Read XML description
    productInfo.completeXMLDescription = XML.loadFile(xmlDescription)

    productInfo
  }


}
