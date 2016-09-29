package org.opencompare.api.scala.interpreter

import org.opencompare.api.scala.{MultipleValue, Value}

import scala.collection.mutable

/**
 * Created by gbecan on 7/20/15.
 */
class MultipleSeparatorPatternInterpreter (
                                   separator : String,
                                   parameters : List[String],
                                   confident : Boolean)
  extends SeparatorPatternInterpreter(separator, parameters, confident) {

  override def createValue(s: String, parts : List[String], parameters : List[String]) : Option[Value] = {
    var fullyInterpreted : Boolean = true

    val subValues = mutable.ListBuffer.empty[Value]

    for (part <- parts) {
      lastCall = Some(s)
      val subCInterpretation = cellContentInterpreter.interpretString(part)
      if (subCInterpretation.isDefined) {
        subValues += subCInterpretation.get
      } else {
        fullyInterpreted = false
      }
    }

    if (fullyInterpreted) {
      Some(MultipleValue(subValues.toList))
    } else {
      None
    }
  }

}