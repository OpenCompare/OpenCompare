package org.opencompare.api.java.interpreters

import java.util.regex.{Matcher, Pattern}

import org.opencompare.api.java.{PCMFactory, Value, Feature, Product}

/**
 * Created by gbecan on 7/20/15.
 */
abstract class RegexPatternInterpreter(
                              regex : String,
                              initParameters : List[String],
                              initConfident : Boolean,
                              initFactory: PCMFactory) extends PatternInterpreter(initParameters, initConfident, initFactory) {

  private val pattern : Pattern =  Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS |
    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL)

  override def matchAndCreateValue(s: String): Option[Value] = {
    val matcher = pattern.matcher(s)
    if (matcher.matches()) {
      createValue(s, matcher, parameters)
    } else {
      None
    }
  }

  def createValue(s : String, matcher : Matcher, parameters : List[String]) : Option[Value]
}
