package org.opencompare.api.java.extractor

import org.opencompare.api.java._
import org.opencompare.api.java.interpreters._

import scala.collection.JavaConversions._


class CellContentInterpreter(factory: PCMFactory) extends org.opencompare.api.java.interpreter.CellContentInterpreter {

  val defaultInterpreters : List[PatternInterpreter] = List(
    new EmptyPatternInterpreter("",Nil, true, factory),
    new BooleanPatternInterpreter("yes|true|✓",List("true"), true, factory),
    new BooleanPatternInterpreter("no|false",List("false"), true, factory),
    new UnknownPatternInterpreter("\\?+",Nil, true, factory),
    new UnknownPatternInterpreter("n/a",Nil, true, factory),
    new UnknownPatternInterpreter("unknown",Nil, true, factory),
    new UnknownPatternInterpreter("(-)+",Nil, true, factory),
    new UnknownPatternInterpreter("(—)+",Nil, true, factory),
    new PartialPatternInterpreter("(partial)",Nil, true, factory)

  )
  val defaultGreedyInterpreters : List[PatternInterpreter] =  List(
    // int
    new IntegerPatternInterpreter("\\d+",Nil, true, factory),
    // double
    new DoublePatternInterpreter("\\d+(\\.\\d+)?",Nil, true, factory),
    // dimensions
    new MultipleRegexPatternInterpreter( "(\\d+(?:\\.\\d+)?) (?:×|x) (\\d+(?:\\.\\d+)?) (?:×|x) (\\d+(?:\\.\\d+)?)", List("and"), true, factory),
    // date XX/XX/XXXX
    new VariabilityConceptRefPatternInterpreter("\\d{2}/\\d{2}/\\d{4}", Nil, true, factory),
    // foo (bar)
    new PartialPatternInterpreter("([^,/]+?)\\s*\\((.+)\\)",Nil,false, factory),
    // yes, with some condition
    new PartialPatternInterpreter("(yes),?\\s*(.*)",Nil, false, factory),
    // values separated by comas
    new MultipleSeparatorPatternInterpreter(",", List("and"), true, factory),
    // values separated by semi colons
    new MultipleSeparatorPatternInterpreter(";", List("and"), true, factory),
    // values separated by slashes
    new MultipleSeparatorPatternInterpreter("/", List("and"), true, factory),
    // foo OR bar
    new MultipleRegexPatternInterpreter("(.+)\\sor\\s(.+)", List("or"), false, factory),
    // foo AND bar
    new MultipleRegexPatternInterpreter("(.+)\\sand\\s(.+)", List("and"), false, factory),
    // everything
    new VariabilityConceptRefPatternInterpreter(".*", Nil, false, factory)
  )

  private var patternInterpreters: List[PatternInterpreter] = defaultInterpreters ::: defaultGreedyInterpreters
  patternInterpreters.foreach(_.setCellContentInterpreter(this))

  def setInterpreters(interpreters: List[PatternInterpreter]) {
    patternInterpreters = defaultInterpreters ::: interpreters ::: defaultGreedyInterpreters
    patternInterpreters.foreach(_.setCellContentInterpreter(this))
  }

  /**
   * Interpret each cell and specify its product and feature headers
    *
    * @param pcm : model of PCM
   */
  override def interpretCells(pcm: PCM) {

    // Interpret every uninterpreted cells
    for (
      product <- pcm.getProducts;
      cell <- product.getCells
      if Option(cell.getInterpretation).isEmpty
    ) {

      // Find interpretation
      val interpretation = interpretStringOption(cell.getContent)

      // Set interpretation
      if (interpretation.isDefined) {
        cell.setInterpretation(interpretation.get)
      }

    }
  }

  override def interpretString(verbatim: String): Value = {
    val interpretation = interpretStringOption(verbatim)
    interpretation match {
      case Some(e) => e
      case None => null
    }
  }

  /**
   * Find interpretation of a string according to the given pattern interpreters and lists of valid products and features
    *
    * @param verbatim : string to analyze
   */
  def interpretStringOption(verbatim: String): Option[Value] = {
    var interpretation: Option[Value] = None
    for (interpreter <- patternInterpreters if !interpretation.isDefined) {
      interpretation = interpreter.interpret(verbatim)
    }
    interpretation
  }


    
    
}