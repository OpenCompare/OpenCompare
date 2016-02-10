package org.opencompare.api.java.interpreters

import _root_.java.util.regex.{Matcher, Pattern}

import org.opencompare.api.java._
import org.opencompare.api.java.extractor.CellContentInterpreter

abstract class PatternInterpreter(
    val parameters : List[String],
    val confident : Boolean,
		val factory: PCMFactory
    ) {

  var cellContentInterpreter : CellContentInterpreter = _

  protected var lastCall : Option[String] = None

  def setCellContentInterpreter(interpreter : CellContentInterpreter) {
    cellContentInterpreter = interpreter
  }

	def interpret(s : String) : Option[Value] = {

	  var result : Option[Value] = None
	  
	  if (!lastCall.isDefined || s != lastCall.get) {
				result = matchAndCreateValue(s)
	  }

	  lastCall = None
	  result
	}

	def matchAndCreateValue(s : String) : Option[Value]

	def format (s : String) : String = {
	  val words = for (word <- s.split("(?U:\\s)") if !word.isEmpty()) yield word
      val formattedContent = words.mkString("", " ", "").toLowerCase()
      formattedContent
	}
	
	

}

