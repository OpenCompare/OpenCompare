package org.opencompare.api.java.interpreters

import java.util.regex.Matcher

import org.opencompare.api.java.{PCMFactory, Value}

import scala.collection.JavaConversions._

/**
  * Created by macher1 on 14/04/2017.
  */
class ImagePatternInterpreter(
  regex : String,
  parameters : List[String],
  confident : Boolean,
  initFactory: PCMFactory)
  extends RegexPatternInterpreter(regex, parameters, confident, initFactory) {

    override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
      /*val value = factory.createImageValue()
      value.setUrl(s)
      Some(value)*/
      val value = factory.createStringValue()
      value.setValue(s)
      Some(value)
    }

}
