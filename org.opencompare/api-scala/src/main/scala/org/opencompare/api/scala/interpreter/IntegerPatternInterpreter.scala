package org.opencompare.api.scala.interpreter

import java.util.regex.Matcher

import org.opencompare.api.scala.{IntegerValue, Value}

import scala.collection.immutable.List

class IntegerPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends RegexPatternInterpreter(regex, parameters, confident) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		  Some(IntegerValue(try {
		    s.toInt
		  } catch {
		    case e : NumberFormatException => 0
		  }))
  }

}