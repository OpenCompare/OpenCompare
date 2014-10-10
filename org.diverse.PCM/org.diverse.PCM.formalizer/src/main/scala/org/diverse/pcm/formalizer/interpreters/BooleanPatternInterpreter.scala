package org.diverse.pcm.formalizer.interpreters

import java.util.regex.Matcher
import pcmmm.Constraint
import pcmmm.PcmmmFactory
import pcmmm.Feature
import pcmmm.Product

class BooleanPatternInterpreter (
    validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends PatternInterpreter(validHeaders, regex, parameters, confident) {
  
 
  override def createConstraint(s: String, matcher : Matcher, parameters : List[String], products : List[Product], features : List[Feature]) : Option[Constraint] = {
		  val constraint = PcmmmFactory.eINSTANCE.createBoolean()
		  if (!parameters.isEmpty) {
			  constraint.setValue(parameters.head.toBoolean)
		  } else {
			  constraint.setValue(false)
		  }
		  constraint.setName(s)
		  constraint.setVerbatim(s)
		  constraint.setConfident(confident)
		  Some(constraint)
  }
    
}