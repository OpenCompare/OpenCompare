package org.opencompare.api.java.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{PCMFactory, Feature, Product, Value}

class MultipleRegexPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean,
		initFactory: PCMFactory)
    extends RegexPatternInterpreter(regex, parameters, confident, initFactory) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
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
				  lastCall = Some(s)
				  val subCInterpretation = cellContentInterpreter.interpretStringOption(subConstraint)
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