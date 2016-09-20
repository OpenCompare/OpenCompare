package org.opencompare.api.scala

class Product {

  var pcm : PCM = _
  def key : Feature = pcm.productsKey
  def keyCell : Option[Cell] = findCell(key)
  def keyContent : Option[String] = keyCell.map(_.content)

  var cells : List[Cell] = Nil

  def findCell(feature : Feature) : Option[Cell] = cells.find(_.feature == feature)




}
