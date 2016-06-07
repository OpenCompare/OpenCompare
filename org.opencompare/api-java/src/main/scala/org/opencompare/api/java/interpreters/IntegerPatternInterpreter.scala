package org.opencompare.api.java.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{PCMFactory, Feature, Product, Value}

import scala.collection.immutable.List

class IntegerPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean,
		initFactory: PCMFactory)
    extends RegexPatternInterpreter(regex, parameters, confident, initFactory) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		  val value = factory.createIntegerValue()
		
		  value.setValue(try {
		    s.toInt
		  } catch {
		    case e : NumberFormatException => 0
		  })
		  
		  Some(value)
    
  }

}