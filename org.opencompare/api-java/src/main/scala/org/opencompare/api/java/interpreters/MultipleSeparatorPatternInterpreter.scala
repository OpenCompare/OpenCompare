package org.opencompare.api.java.interpreters

import org.opencompare.api.java.{PCMFactory, Value, Feature, Product}

/**
 * Created by gbecan on 7/20/15.
 */
class MultipleSeparatorPatternInterpreter (
                                   separator : String,
                                   parameters : List[String],
                                   confident : Boolean,
                                   initFactory: PCMFactory)
  extends SeparatorPatternInterpreter(separator, parameters, confident, initFactory) {

  override def createValue(s: String, parts : List[String], parameters : List[String]) : Option[Value] = {
    val value = factory.createMultiple()

    var fullyInterpreted : Boolean = true

    for (part <- parts) {
      lastCall = Some(s)
      val subCInterpretation = cellContentInterpreter.interpretStringOption(part)
      if (subCInterpretation.isDefined) {
        value.addSubValue(subCInterpretation.get)
      } else {
        fullyInterpreted = false
      }
    }

    if (fullyInterpreted) {
      Some(value)
    } else {
      None
    }
  }

}