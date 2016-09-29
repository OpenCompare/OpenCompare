package org.opencompare.api.scala

abstract class AbstractFeature {

  var name : String = ""
  var parent : Option[FeatureGroup] = None

}
