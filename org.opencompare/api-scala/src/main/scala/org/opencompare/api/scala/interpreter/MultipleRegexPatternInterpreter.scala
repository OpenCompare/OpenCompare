package org.opencompare.api.scala.interpreter

import java.util.regex.Matcher

import org.opencompare.api.scala.{MultipleValue, Value}

import scala.collection.mutable

class MultipleRegexPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends RegexPatternInterpreter(regex, parameters, confident) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		  var fullyInterpreted : Boolean = true

			val subValues = mutable.ListBuffer.empty[Value]

		  for (groupID <- 1 to matcher.groupCount()) {
			  val subConstraint = matcher.group(groupID)
			  if (subConstraint != null) {
				  lastCall = Some(s)
				  val subCInterpretation = cellContentInterpreter.interpretString(subConstraint)
				  if (subCInterpretation.isDefined) {
				    subValues += subCInterpretation.get
				  } else {
				    fullyInterpreted = false
				  }
			  }

		  }
		  if (fullyInterpreted) {
			  Some(MultipleValue(subValues.toList))
		  } else {
			  None
		  }
  }

}