package org.diverse.pcm.formalizer.interpreters

import pcmmm.Constraint
import java.util.regex.Matcher
import pcmmm.PcmmmFactory
import pcmmm.Product
import pcmmm.Feature

class VariabilityConceptRefPatternInterpreter (
    validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends PatternInterpreter(validHeaders, regex, parameters, confident) {
 
  override def createConstraint(s : String, matcher : Matcher, parameters : List[String], products : List[Product], features : List[Feature]) : Option[Constraint] = {
		  val constraint = PcmmmFactory.eINSTANCE.createVariabilityConceptRef()
		  constraint.setName(s)
		  constraint.setVerbatim(s)
		  constraint.setConfident(confident)
		  Some(constraint)
  }
  
}