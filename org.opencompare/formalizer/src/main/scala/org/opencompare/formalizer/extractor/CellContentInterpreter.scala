package org.opencompare.formalizer.extractor

import org.opencompare.api.java.{Feature, PCM, Product, Value}
import org.opencompare.formalizer.interpreters._

import scala.collection.JavaConversions._

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
  def findInterpretation(verbatim: String, product: Product, feature: Feature): Option[Value] = {
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
  val defaultGreedyInterpreters : List[PatternInterpreter] =  List(
      // int
      new IntegerPatternInterpreter(Nil,"\\d+",Nil, true),
      // double
      new DoublePatternInterpreter(Nil,"\\d+(\\.\\d+)?",Nil, true),
      // dimensions
      new MultipleRegexPatternInterpreter(Nil, "(\\d+(?:\\.\\d+)?) (?:×|x) (\\d+(?:\\.\\d+)?) (?:×|x) (\\d+(?:\\.\\d+)?)", List("and"), true),
      // date XX/XX/XXXX
      new VariabilityConceptRefPatternInterpreter(Nil, "\\d{2}/\\d{2}/\\d{4}", Nil, true),
      // foo (bar)
      new PartialPatternInterpreter(Nil,"([^,/]+?)\\s*\\((.+)\\)",Nil,false),
      // yes, with some condition
      new PartialPatternInterpreter(Nil,"(yes),?\\s*(.*)",Nil, false),
      // values separated by comas
      new MultipleSeparatorPatternInterpreter(Nil, ",", List("and"), true),
      // values separated by semi colons
      new MultipleSeparatorPatternInterpreter(Nil, ";", List("and"), true),
      // values separated by slashes
      new MultipleSeparatorPatternInterpreter(Nil, "/", List("and"), true),
      // foo OR bar
      new MultipleRegexPatternInterpreter(Nil,"(.+)\\sor\\s(.+)", List("or"), false),
      // foo AND bar
      new MultipleRegexPatternInterpreter(Nil,"(.+)\\sand\\s(.+)", List("and"), false),
      // everything
      new VariabilityConceptRefPatternInterpreter(Nil, ".*", Nil, false)
    )
    
    
}