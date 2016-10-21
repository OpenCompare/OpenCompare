package org.opencompare.api.scala.io

class ExportCell(
                  initContent : String = "",
                  initRawContent : String = "",
                  initRowspan : Int = 1,
                  initColspan : Int = 1)
  extends IOCell(initContent, initRawContent, initRowspan, initColspan) {

  var feature : Boolean = false
  var inProductsKeyColumn : Boolean = false
}
