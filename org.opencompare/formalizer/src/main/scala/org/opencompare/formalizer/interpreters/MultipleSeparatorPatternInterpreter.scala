package org.opencompare.formalizer.interpreters

import org.opencompare.api.java.{Value, Feature, Product}

/**
 * Created by gbecan on 7/20/15.
 */
class MultipleSeparatorPatternInterpreter (
                                   validHeaders : List[String],
                                   separator : String,
                                   parameters : List[String],
                                   confident : Boolean)
  extends SeparatorPatternInterpreter(validHeaders, separator, parameters, confident) {

  override def createValue(s: String, parts : List[String], parameters : List[String], product : Product, feature : Feature) : Option[Value] = {
    val value = factory.createMultiple()

    var fullyInterpreted : Boolean = true

    for (part <- parts) {
      lastCall = Some(s, product, feature)
      val subCInterpretation = cellContentInterpreter.findInterpretation(part, product, feature)
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