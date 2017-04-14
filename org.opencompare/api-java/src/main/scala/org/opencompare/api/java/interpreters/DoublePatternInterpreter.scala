package org.opencompare.api.java.interpreters

import java.text.{DecimalFormat, DecimalFormatSymbols}
import java.util.regex.Matcher

import org.opencompare.api.java.{Feature, PCMFactory, Product, Value}

class DoublePatternInterpreter (
    regex : String,
    parameters : List[String],
    confident : Boolean,
		initFactory: PCMFactory)
    extends RegexPatternInterpreter(regex, parameters, confident, initFactory) {

	def mkStrValue(s: String): Option[Value] = {
		val value = factory.createStringValue()
		value.setValue(s)
		Some(value)
	}

	override def createValue(s: String, matcher : Matcher, parameters : List[String]) : Option[Value] = {
		  val value = factory.createRealValue()

			if (s.contains(",")) { // trying to parse with ","
				val df = new DecimalFormat()
				val symbols = new DecimalFormatSymbols()
				symbols.setDecimalSeparator(',')
				symbols.setGroupingSeparator(' ')
				df.setDecimalFormatSymbols(symbols)
				try {
					val d = df.parse(s).doubleValue()
					value.setValue(d)
					Some(value)
				} catch {
					case e : NumberFormatException => mkStrValue(s)
					case e : NullPointerException => mkStrValue(s)
				}
			}

			else {

				try {
					value.setValue(s.toDouble)
					Some(value)
				} catch {
					case e: NumberFormatException => mkStrValue(s)
					case e: NullPointerException => mkStrValue(s)
				}

			}
    
  }

}