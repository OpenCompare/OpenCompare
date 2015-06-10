package org.opencompare.io.wikipedia.pcm

import org.opencompare.api.java.impl.PCMFactoryImpl


class Cell(
  val content : String,
  val rawContent : String,
  val isHeader : Boolean,
  val row : Int,
  val rowspan : Int,
  val column : Int,
  val colspan : Int
  ) {
  
  override def toString() : String = {
     content
  }

  def toPCM() : org.opencompare.api.java.Cell = {
    val factory = new PCMFactoryImpl()
    val cell = factory.createCell()
    cell.setContent(content)
    cell
  }
  
}