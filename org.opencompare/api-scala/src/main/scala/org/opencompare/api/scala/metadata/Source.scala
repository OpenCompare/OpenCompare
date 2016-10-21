package org.opencompare.api.scala.metadata

import org.opencompare.api.scala.PCM

trait Source extends PCM {

  var source : String = ""
  var license : String = ""
  var creator : String = ""

}
