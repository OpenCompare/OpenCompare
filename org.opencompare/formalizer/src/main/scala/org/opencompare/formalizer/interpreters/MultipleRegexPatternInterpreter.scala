package org.opencompare.formalizer.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{Feature, Product, Value}

class MultipleRegexPatternInterpreter (
    validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends RegexPatternInterpreter(validHeaders, regex, parameters, confident) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String], product : Product, feature : Feature) : Option[Value] = {
		  val value = parameters match { // TODO : support cardinality
//		    case "and" :: Nil => PcmmmFactory.eINSTANCE.createAnd()
//		    case "xor" :: Nil => PcmmmFactory.eINSTANCE.createXOr()
//		    case "or" :: Nil => PcmmmFactory.eINSTANCE.createOr()
		    case _ => factory.createMultiple()
		  }
		  var fullyInterpreted : Boolean = true

		  for (groupID <- 1 to matcher.groupCount()) {
			  val subConstraint = matcher.group(groupID)
			  if (subConstraint != null) {
				  lastCall = Some(s, product, feature)
				  val subCInterpretation = cellContentInterpreter.findInterpretation(subConstraint, product, feature)
				  if (subCInterpretation.isDefined) {
				    value.addSubValue(subCInterpretation.get)
				  } else {
				    fullyInterpreted = false
				  }
			  }

		  }
		  if (fullyInterpreted) {
			  Some(value)
		  } else {
			  None
		  }
  }

}