package org.opencompare.api.java.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{PCMFactory, Value, Feature, Product}

/**
 * Created by gbecan on 7/20/15.
 */
abstract class SeparatorPatternInterpreter(val separator : String,
                                  initParameters : List[String],
                                  initConfident : Boolean,
                                   initFactory: PCMFactory) extends PatternInterpreter(initParameters, initConfident, initFactory) {

  override def matchAndCreateValue(s: String): Option[Value] = {
    val parts = s.split(separator).toList
    if (parts.size > 1) {
      createValue(s, parts, parameters)
    } else {
      None
    }

  }

  def createValue(s : String, parts : List[String], parameters : List[String]) : Option[Value]
}
