package org.opencompare.formalizer.interpreters

import org.diverse.pcm.api.java.Value
import java.util.regex.Matcher

import org.opencompare.api.java.{Value, Product, Feature}

class VariabilityConceptRefPatternInterpreter (
    validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends PatternInterpreter(validHeaders, regex, parameters, confident) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String], product : Product, feature : Feature) : Option[Value] = {
		val value = factory.createStringValue()
    value.setValue(s)
		Some(value)
  }
  
}