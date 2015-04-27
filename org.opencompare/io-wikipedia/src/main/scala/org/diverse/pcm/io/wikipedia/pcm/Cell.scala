package org.diverse.pcm.io.wikipedia.pcm

import org.diverse.pcm.api.java.impl.PCMFactoryImpl


class Cell(
  val content : String,
  val isHeader : Boolean,
  val row : Int,
  val rowspan : Int,
  val column : Int,
  val colspan : Int
  ) {
  
  override def toString() : String = {
     content
  }

  def toPCM() : org.diverse.pcm.api.java.Cell = {
    val factory = new PCMFactoryImpl()
    val cell = factory.createCell()
    cell.setContent(content)
    cell
  }
  
}