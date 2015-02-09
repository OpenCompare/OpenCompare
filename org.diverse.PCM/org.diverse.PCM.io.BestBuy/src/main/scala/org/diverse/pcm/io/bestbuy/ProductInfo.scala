package org.diverse.pcm.io.bestbuy

/**
 * Created by gbecan on 15/01/15.
 */
class ProductInfo {

  var name = ""
  var longDescription = ""
  var features = List[String]()
  var details = Map[String, String]()


  def addFeature(feature: String) {
    features = feature :: features
  }

  def addDetail(name: String, value: String) {
    details += (name -> value)
  }

  override def toString: String = {
    return "ProductInfo{" + "longDescription='" + longDescription + "'\n" + "features=" + features + "\n" + "details=" + details + "\n" + '}'
  }

}