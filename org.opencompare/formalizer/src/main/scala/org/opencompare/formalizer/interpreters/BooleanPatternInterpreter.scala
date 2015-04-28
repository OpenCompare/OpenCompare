package org.opencompare.formalizer.interpreters

import java.util.regex.Matcher

import org.diverse.pcm.api.java.Value
import org.opencompare.api.java.{Value, Product, Feature}

class BooleanPatternInterpreter (
    validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends PatternInterpreter(validHeaders, regex, parameters, confident) {
  
 
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