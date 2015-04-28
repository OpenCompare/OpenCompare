package org.opencompare.io.bestbuy

/**
 * Created by gbecan on 15/01/15.
 */
class ProductInfo {

  var sku = ""
  var name = ""
  var longDescription = ""
  var features = collection.mutable.ListBuffer[String]()
  var details = Map[String, String]()
  var completeXMLDescription = <NoDescription></NoDescription>

  def addFeature(feature: String) {
    features += feature
  }

  def addDetail(name: String, value: String) {
    details += (name -> value)
  }

  override def toString: String = {
    return "ProductInfo{" + "longDescription='" + longDescription + "'\n" + "features=" + features + "\n" + "details=" + details + "\n" + '}'
  }

}