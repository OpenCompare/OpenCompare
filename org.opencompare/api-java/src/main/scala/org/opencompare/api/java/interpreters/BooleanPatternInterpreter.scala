package org.opencompare.api.java.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{PCMFactory, Feature, Product, Value}

class BooleanPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean,
		initFactory: PCMFactory)
    extends RegexPatternInterpreter(regex, parameters, confident, initFactory) {
  
 
  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		  val value = factory.createBooleanValue()
		  if (parameters.nonEmpty) {
			  value.setValue(parameters.head.toBoolean)
		  } else {
			  value.setValue(false)
		  }
		  Some(value)
  }
    
}