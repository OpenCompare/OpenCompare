package org.opencompare.api.java.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{PCMFactory, Feature, Product, Value}

class DoublePatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean,
		initFactory: PCMFactory)
    extends RegexPatternInterpreter(regex, parameters, confident, initFactory) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		  val value = factory.createRealValue()
		
		  value.setValue(try {
		    s.toDouble
		  } catch {
		    case e : NumberFormatException => Double.NaN
		    case e : NullPointerException => Double.NaN
		  })
		  
		  Some(value)
    
  }

}