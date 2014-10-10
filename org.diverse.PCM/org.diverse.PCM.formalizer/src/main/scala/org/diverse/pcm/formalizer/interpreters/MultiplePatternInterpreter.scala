package org.diverse.pcm.formalizer.interpreters

import java.util.regex.Matcher
import pcmmm.Constraint
import pcmmm.PcmmmFactory
import org.diverse.pcm.formalizer.extractor.CellContentInterpreter
import pcmmm.Product
import pcmmm.Feature

class MultiplePatternInterpreter (
    validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends PatternInterpreter(validHeaders, regex, parameters, confident) {

  override def createConstraint(s : String, matcher : Matcher, parameters : List[String], products : List[Product], features : List[Feature]) : Option[Constraint] = {
		  val constraint = parameters match {
		    case "and" :: Nil => PcmmmFactory.eINSTANCE.createAnd()
		    case "xor" :: Nil => PcmmmFactory.eINSTANCE.createXOr()
		    case "or" :: Nil => PcmmmFactory.eINSTANCE.createOr()
		    case _ => PcmmmFactory.eINSTANCE.createMultiple()
		  }
		  var fullyInterpreted : Boolean = true
		  var subConstraintsConfidence = true
		  for (groupID <- 1 to matcher.groupCount()) {
			  val subConstraint = matcher.group(groupID)
			  if (subConstraint != null) {
				  lastCall = Some(s, products, features) 
				  val subCInterpretation = cellContentInterpreter.findInterpretation(subConstraint, products, features)
				  if (subCInterpretation.isDefined) {
				    constraint.getContraints().add(subCInterpretation.get)
				    subConstraintsConfidence = subConstraintsConfidence && subCInterpretation.get.isConfident()
				  } else {
				    fullyInterpreted = false
				  }
			  }
			  
		  }
		  if (fullyInterpreted) {
			  constraint.setConfident(confident && subConstraintsConfidence)
			  Some(constraint)
		  } else {
			  None
		  }
  }

}