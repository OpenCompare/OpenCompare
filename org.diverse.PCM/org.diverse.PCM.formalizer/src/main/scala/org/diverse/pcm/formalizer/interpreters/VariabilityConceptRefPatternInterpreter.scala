package org.diverse.pcm.formalizer.interpreters

import org.diverse.pcm.api.java.{Value, Feature, Product}
import java.util.regex.Matcher

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