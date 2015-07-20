package org.opencompare.formalizer.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{Feature, Product, Value}

class BooleanPatternInterpreter (
    validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends RegexPatternInterpreter(validHeaders, regex, parameters, confident) {
  
 
  override def createValue(s: String, matcher : Matcher, parameters : List[String], product : Product, feature : Feature) : Option[Value] = {
		  val value = factory.createBooleanValue();
		  if (!parameters.isEmpty) {
			  value.setValue(parameters.head.toBoolean)
		  } else {
			  value.setValue(false)
		  }
		  Some(value)
  }
    
}