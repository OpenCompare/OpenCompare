package org.opencompare.api.java.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{PCMFactory, Feature, Product, Value}

class PartialPatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean,
    initFactory: PCMFactory)
    extends RegexPatternInterpreter(regex, parameters, confident, initFactory) {

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


    if (valueInterpretation.isDefined && !condInterpretation.isDefined) { // Partial

      val value = factory.createPartial()
      value.setValue(valueInterpretation.get)
      Some(value)

    } else if (valueInterpretation.isDefined && condInterpretation.isDefined) { // Conditional

      val value = factory.createConditional()

      if (!parameters.contains("inverted")) {
        value.setValue(valueInterpretation.get)
        value.setCondition(condInterpretation.get)
      } else {
        // Invert argument and condition if specified by parameter
        value.setValue(condInterpretation.get)
        value.setCondition(valueInterpretation.get)
      }

      Some(value)

    } else {
      None
    }





  }

}