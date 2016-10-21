package org.opencompare.api.scala.interpreter

import org.opencompare.api.scala.Value

abstract class PatternInterpreter(
    val parameters : List[String],
    val confident : Boolean
    ) {

  var cellContentInterpreter : CellContentInterpreter = _

  protected var lastCall : Option[String] = None

  def setCellContentInterpreter(interpreter : CellContentInterpreter) {
    cellContentInterpreter = interpreter
  }

	def interpret(s : String) : Option[Value] = {

	  var result : Option[Value] = None
	  
	  if (lastCall.isEmpty || s != lastCall.get) {
				result = matchAndCreateValue(s)
	  }

	  lastCall = None
	  result
	}

	def matchAndCreateValue(s : String) : Option[Value]

	def format (s : String) : String = {
	  val words = for (word <- s.split("(?U:\\s)") if !word.isEmpty) yield word
      val formattedContent = words.mkString("", " ", "").toLowerCase()
      formattedContent
	}
	
	

}

