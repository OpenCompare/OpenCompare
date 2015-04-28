//package org.opencompare.formalizer.configuration
//
//import scala.collection.mutable.ListBuffer
//import org.opencompare.formalizer.interpreters.PatternInterpreter
//
//class MatrixConfiguration {
//
//  var ignored : Boolean = false
//  var inverted : Boolean = false
//
//  var headerRows : Int = 1
//  var headerColumns : Int = 1
//
//  var ignoreRows : List[Int] = Nil
//  var ignoreColumns : List[Int] = Nil
//
//  private var patterns : ListBuffer[PatternInterpreter] = ListBuffer()
//
//  def addPattern(pattern : PatternInterpreter) {
//    	patterns += pattern
//  }
//
//  def getPatterns() : List[PatternInterpreter] = {
//		  patterns.toList
//  }
//}