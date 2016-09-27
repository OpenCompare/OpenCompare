package org.opencompare.api.scala.interpreter

import org.opencompare.api.scala.Value

/**
 * Created by gbecan on 7/20/15.
 */
abstract class SeparatorPatternInterpreter(val separator : String,
                                  initParameters : List[String],
                                  initConfident : Boolean) extends PatternInterpreter(initParameters, initConfident) {

  override def matchAndCreateValue(s: String): Option[Value] = {
    val parts = s.split(separator).toList
    if (parts.size > 1) {
      createValue(s, parts, parameters)
    } else {
      None
    }

  }

  def createValue(s : String, parts : List[String], parameters : List[String]) : Option[Value]
}
