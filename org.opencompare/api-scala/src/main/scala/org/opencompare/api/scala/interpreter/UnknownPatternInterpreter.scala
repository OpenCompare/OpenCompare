package org.opencompare.api.scala.interpreter

import java.util.regex.Matcher

import org.opencompare.api.scala.{NotAvailableValue, Value}

import scala.collection.immutable.List

class UnknownPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends RegexPatternInterpreter(regex, parameters, confident) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		 Some(NotAvailableValue())
  }

}