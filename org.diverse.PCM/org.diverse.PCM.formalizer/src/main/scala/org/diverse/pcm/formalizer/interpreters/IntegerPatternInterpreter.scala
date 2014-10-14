package org.diverse.pcm.formalizer.interpreters

import java.util.regex.Matcher
import org.diverse.pcm.api.java.{Value, Feature, Product}

import scala.collection.immutable.List

class IntegerPatternInterpreter (
     validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends PatternInterpreter(validHeaders, regex, parameters, confident) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String], product : Product, feature : Feature) : Option[Value] = {
		  val value = factory.createIntegerValue()
		
		  value.setValue(try {
		    s.toInt
		  } catch {
		    case e : NumberFormatException => 0
		  })
		  
		  Some(value)
    
  }

}