package org.opencompare.api.scala.interpreter

import java.util.regex.Matcher

import org.opencompare.api.scala.{ConditionalValue, PartialValue, Value}

class PartialPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends RegexPatternInterpreter(regex, parameters, confident) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {

    // Interpret value
    val valueInterpretation = if (matcher.groupCount() >= 1) {
      val valueString = matcher.group(1)
      lastCall = Some(s)
      cellContentInterpreter.interpretStringOption(valueString)
    } else {
      None
    }

    // Interpret condition
    val condInterpretation = if (matcher.groupCount() >= 2) {
      val conditionString = matcher.group(2)
      lastCall = Some(s)
      cellContentInterpreter.interpretStringOption(conditionString)
    } else {
      None
    }


    if (valueInterpretation.isDefined && condInterpretation.isEmpty) { // Partial
      Some(PartialValue(valueInterpretation.get))
    } else if (valueInterpretation.isDefined && condInterpretation.isDefined) { // Conditional
      if (!parameters.contains("inverted")) {
        Some(ConditionalValue(valueInterpretation.get, condInterpretation.get))
      } else {
        // Invert argument and condition if specified by parameter
        Some(ConditionalValue(condInterpretation.get, valueInterpretation.get))
      }
    } else {
      None
    }





  }

}