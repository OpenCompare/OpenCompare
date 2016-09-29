package org.opencompare.api.scala.io

import org.opencompare.api.scala.Value

class ImportCell(
                  initContent : String = "",
                  initRawContent : String = "",
                  initRowspan : Int = 1,
                  initColspan : Int = 1)
  extends IOCell(initContent, initRawContent, initRowspan, initColspan) {

  var interpretation : Option[Value] = None


}
