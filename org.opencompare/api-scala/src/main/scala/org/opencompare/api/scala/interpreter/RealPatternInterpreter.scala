package org.opencompare.api.scala.interpreter

import java.util.regex.Matcher

import org.opencompare.api.scala.{RealValue, Value}

class RealPatternInterpreter(
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends RegexPatternInterpreter(regex, parameters, confident) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		  Some(RealValue(try {
		    s.toDouble
		  } catch {
		    case e : NumberFormatException => Double.NaN
		    case e : NullPointerException => Double.NaN
		  }))
  }

}