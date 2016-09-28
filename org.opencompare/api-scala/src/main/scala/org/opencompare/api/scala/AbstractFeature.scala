package org.opencompare.api.scala

trait AbstractFeature {

  var name : String = ""
  var parent : Option[FeatureGroup] = None

}
