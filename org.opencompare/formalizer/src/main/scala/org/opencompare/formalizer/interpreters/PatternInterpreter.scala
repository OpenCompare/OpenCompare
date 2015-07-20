package org.opencompare.formalizer.interpreters

import _root_.java.util.regex.{Matcher, Pattern}

import org.opencompare.api.java.impl.PCMFactoryImpl
import org.opencompare.api.java.{PCMFactory, Feature, Product, Value}
import org.opencompare.formalizer.extractor.CellContentInterpreter

abstract class PatternInterpreter(
	val validHeaders : List[String],
    val parameters : List[String],
    val confident : Boolean
    ) {

	val factory : PCMFactory = new PCMFactoryImpl

	var validProducts : List[Product] = Nil
  var validFeatures : List[Feature] = Nil

  var cellContentInterpreter : CellContentInterpreter = _

  protected var lastCall : Option[(String, Product, Feature)] = None

  def setCellContentInterpreter(interpreter : CellContentInterpreter) {
    cellContentInterpreter = interpreter
  }

	def interpret(s : String, product : Product, feature : Feature) : Option[Value] = {

	  var result : Option[Value] = None
	  
	  if (!lastCall.isDefined || (s, product, feature) != lastCall.get) {
		  if ((validProducts.isEmpty || validProducts.contains(product))
				  && (validFeatures.isEmpty || validFeatures.contains(feature))) {
			  	result = matchAndCreateValue(s, product, feature)
		  }
	  } 

	  lastCall = None
	  result
	}

	def matchAndCreateValue(s : String, product : Product, feature : Feature) : Option[Value]

	def format (s : String) : String = {
	  val words = for (word <- s.split("(?U:\\s)") if !word.isEmpty()) yield word
      val formattedContent = words.mkString("", " ", "").toLowerCase()
      formattedContent
	}
	
	

}

