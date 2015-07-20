package org.opencompare.formalizer.interpreters

import java.util.regex.{Matcher, Pattern}

import org.opencompare.api.java.{Value, Feature, Product}

/**
 * Created by gbecan on 7/20/15.
 */
abstract class RegexPatternInterpreter(initValidHeaders : List[String],
                              regex : String,
                              initParameters : List[String],
                              initConfident : Boolean) extends PatternInterpreter(initValidHeaders, initParameters, initConfident) {

  private val pattern : Pattern =  Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS |
    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL)

  override def matchAndCreateValue(s: String, product: Product, feature: Feature): Option[Value] = {
    val matcher = pattern.matcher(s)
    if (matcher.matches()) {
      createValue(s, matcher, parameters, product, feature)
    } else {
      None
    }
  }

  def createValue(s : String, matcher : Matcher, parameters : List[String], product : Product, feature : Feature) : Option[Value]
}
