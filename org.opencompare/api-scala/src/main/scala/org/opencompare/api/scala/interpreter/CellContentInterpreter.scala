package org.opencompare.api.scala.interpreter

import org.opencompare.api.scala.{PCM, Value}

trait CellContentInterpreter {

  def interpretCells(pcm : PCM)
  def interpretString(content : String) : Option[Value]

}
