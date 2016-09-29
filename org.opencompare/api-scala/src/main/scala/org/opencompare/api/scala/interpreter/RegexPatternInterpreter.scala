package org.opencompare.api.scala.interpreter

import java.util.regex.{Matcher, Pattern}

import org.opencompare.api.scala.Value

abstract class RegexPatternInterpreter(
                              regex : String,
                              initParameters : List[String],
                              initConfident : Boolean) extends PatternInterpreter(initParameters, initConfident) {

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
