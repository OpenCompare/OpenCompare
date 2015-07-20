package org.opencompare.formalizer.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{Feature, Product, Value}

class PartialPatternInterpreter (
    validHeaders : List[String],
    regex : String,
    parameters : List[String],
    confident : Boolean)
    extends RegexPatternInterpreter(validHeaders, regex, parameters, confident) {

  override def createValue(s: String, matcher : Matcher, parameters : List[String], product : Product, feature : Feature) : Option[Value] = {

    // Interpret value
    val valueInterpretation = if (matcher.groupCount() >= 1) {
      val valueString = matcher.group(1)
      lastCall = Some(s, product, feature)
      cellContentInterpreter.findInterpretation(valueString, product, feature)
    } else {
      None
    }

    // Interpret condition
    val condInterpretation = if (matcher.groupCount() >= 2) {
      val conditionString = matcher.group(2)
      lastCall = Some(s, product, feature)
      cellContentInterpreter.findInterpretation(conditionString, product, feature)
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