package org.opencompare.api.scala.interpreter

import java.util.regex.Matcher

import org.opencompare.api.scala.{BooleanValue, Value}

class BooleanPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends RegexPatternInterpreter(regex, parameters, confident) {
  
 
  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		  if (parameters.nonEmpty) {
				Some(BooleanValue(parameters.head.toBoolean))
		  } else {
				Some(BooleanValue(false))
		  }
  }
    
}