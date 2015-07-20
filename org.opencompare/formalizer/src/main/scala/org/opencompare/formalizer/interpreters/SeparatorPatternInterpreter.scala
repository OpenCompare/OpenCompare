package org.opencompare.formalizer.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{Value, Feature, Product}

/**
 * Created by gbecan on 7/20/15.
 */
abstract class SeparatorPatternInterpreter(initValidHeaders : List[String],
                                  val separator : String,
                                  initParameters : List[String],
                                  initConfident : Boolean) extends PatternInterpreter(initValidHeaders, initParameters, initConfident) {

  override def matchAndCreateValue(s: String, product: Product, feature: Feature): Option[Value] = {
    val parts = s.split(separator).toList
    if (parts.size > 1) {
      createValue(s, parts, parameters, product, feature)
    } else {
      None
    }

  }

  def createValue(s : String, parts : List[String], parameters : List[String], product : Product, feature : Feature) : Option[Value]
}
