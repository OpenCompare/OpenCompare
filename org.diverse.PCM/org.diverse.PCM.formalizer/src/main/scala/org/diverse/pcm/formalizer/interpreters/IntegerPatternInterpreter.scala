package org.diverse.pcm.formalizer.interpreters

import java.util.regex.Matcher
import scala.collection.immutable.List
import pcmmm.PcmmmFactory
import pcmmm.Product
import pcmmm.Feature
import pcmmm.Constraint

class IntegerPatternInterpreter (
     validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends PatternInterpreter(validHeaders, regex, parameters, confident) {
  
  override def createConstraint(s: String, matcher : Matcher, parameters : List[String], products : List[Product], features : List[Feature]) : Option[Constraint] = {
		  val constraint = PcmmmFactory.eINSTANCE.createInteger()
		
		  constraint.setValue(try {
		    s.toInt
		  } catch {
		    case e : NumberFormatException => 0
		  })
		  
		  constraint.setName(s)
		  constraint.setVerbatim(s)
		  constraint.setConfident(confident)
		  Some(constraint)
    
  }

}