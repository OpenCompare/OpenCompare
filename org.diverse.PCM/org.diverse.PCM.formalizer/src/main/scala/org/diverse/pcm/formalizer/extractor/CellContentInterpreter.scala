package org.diverse.pcm.formalizer.extractor

import org.diverse.pcm.api.java
import org.diverse.pcm.api.java.{Feature, Value, PCM}

import scala.collection.JavaConversions._
import org.diverse.pcm.formalizer.interpreters.BooleanPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.UnknownPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.EmptyPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.MultiplePatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PartialPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.VariabilityConceptRefPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import org.diverse.pcm.formalizer.interpreters.DoublePatternInterpreter
import org.diverse.pcm.formalizer.interpreters.IntegerPatternInterpreter

class CellContentInterpreter() {

  private var patternInterpreters: List[PatternInterpreter] = CellContentInterpreter.defaultInterpreters ::: CellContentInterpreter.defaultGreedyInterpreters
  patternInterpreters.foreach(_.setCellContentInterpreter(this))

  def setInterpreters(interpreters: List[PatternInterpreter]) {
    patternInterpreters = CellContentInterpreter.defaultInterpreters :::
      interpreters :::
      CellContentInterpreter.defaultGreedyInterpreters
    patternInterpreters.foreach(_.setCellContentInterpreter(this))
  }

  /**
   * Interpret each cell and specify its product and feature headers
   * @param pcm : model of PCM
   */
  def interpretCells(pcm: PCM) {

    // Interpret every uninterpreted cells
    for (
      product <- pcm.getProducts;
      cell <- product.getCells
      if !Option(cell.getInterpretation).isDefined // FIXME : do we need to filter out interpreted cells?
    ) {

      // Find interpretation
      val interpretation = findInterpretation(cell.getContent, product, cell.getFeature)

      // Set interpretation
      if (interpretation.isDefined) {
        cell.setInterpretation(interpretation.get)
      }

    }
  }

  /**
   * Find interpretation of a string according to the given pattern interpreters and lists of valid products and features
   * @param verbatim : string to analyze
   * @param product : product associated with this string
   * @param feature : feature associated with this string
   */
  def findInterpretation(verbatim: String, product: java.Product, feature: Feature): Option[Value] = {
    var interpretation: Option[Value] = None
    for (interpreter <- patternInterpreters if !interpretation.isDefined) {
      interpretation = interpreter.interpret(verbatim, product, feature)
    }
    interpretation
  }
}
  
object CellContentInterpreter {
  val defaultInterpreters : List[PatternInterpreter] = List(
		new EmptyPatternInterpreter(Nil,"",Nil, true),
    	new BooleanPatternInterpreter(Nil,"yes|true|✓",List("true"), true),
    	new BooleanPatternInterpreter(Nil,"no|false",List("false"), true),
    	new UnknownPatternInterpreter(Nil,"\\?+",Nil, true),
    	new UnknownPatternInterpreter(Nil,"n/a",Nil, true),
    	new UnknownPatternInterpreter(Nil,"unknown",Nil, true),
    	new UnknownPatternInterpreter(Nil,"(-)+",Nil, true),
    	new UnknownPatternInterpreter(Nil,"(—)+",Nil, true),
    	new PartialPatternInterpreter(Nil,"(partial)",Nil, true)
		
    )
  val defaultGreedyInterpreters : List[PatternInterpreter] = List(
      // int
      new IntegerPatternInterpreter(Nil,"\\d+",Nil, true),
      // double
      new DoublePatternInterpreter(Nil,"\\d+(\\.\\d+)?",Nil, true),
      // dimensions
      new MultiplePatternInterpreter(Nil, "(\\d+(?:\\.\\d+)?) (?:×|x) (\\d+(?:\\.\\d+)?) (?:×|x) (\\d+(?:\\.\\d+)?)", List("and"), true),
      // date XX/XX/XXXX
      new VariabilityConceptRefPatternInterpreter(Nil, "\\d{2}/\\d{2}/\\d{4}", Nil, true),
      // foo (bar)
      new PartialPatternInterpreter(Nil,"([^,/]+?)\\s*\\((.+)\\)",Nil,false),
      // yes, with some condition
      new PartialPatternInterpreter(Nil,"(yes),?\\s*(.*)",Nil, false)
    ) ::: 
    // values separated by comas
    (for (n <- 1 to 50) yield {
    	  new MultiplePatternInterpreter(Nil,
    	      (for (i <- 1 to n+1) yield {"([^,]+?)"}).mkString("\\s*,\\s+"),
    	      List("and"), true)
    }).toList ::: 
    // values separated by semi colons
    (for (n <- 1 to 50) yield {
    	  new MultiplePatternInterpreter(Nil,
    	      (for (i <- 1 to n+1) yield {"([^;]+?)"}).mkString("\\s*;\\s+"),
    	      List("and"), true)
    }).toList ::: 
    // values separated by slashes 
    (for (n <- 1 to 10) yield {
    	  new MultiplePatternInterpreter(Nil,
    	      (for (i <- 1 to n+1) yield {"([^/]+?)"}).mkString("\\s*/\\s*"),
    	      List("and"), true)
    }).toList ::: List(
        // foo OR bar
        new MultiplePatternInterpreter(Nil,"(.+)\\sor\\s(.+)", List("or"), false),
        // foo AND bar
        new MultiplePatternInterpreter(Nil,"(.+)\\sand\\s(.+)", List("and"), false),
        // everything
        new VariabilityConceptRefPatternInterpreter(Nil, ".*", Nil, false)
    ) 
    
    
}