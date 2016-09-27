package org.opencompare.api.scala.interpreter

import org.opencompare.api.scala.{PCM, Value}

class DefaultCellContentInterpreter() extends CellContentInterpreter {

  val defaultInterpreters : List[PatternInterpreter] = List(
    new EmptyPatternInterpreter("",Nil, true),
    new BooleanPatternInterpreter("yes|true|✓",List("true"), true),
    new BooleanPatternInterpreter("no|false",List("false"), true),
    new UnknownPatternInterpreter("\\?+",Nil, true),
    new UnknownPatternInterpreter("n/a",Nil, true),
    new UnknownPatternInterpreter("unknown",Nil, true),
    new UnknownPatternInterpreter("(-)+",Nil, true),
    new UnknownPatternInterpreter("(—)+",Nil, true),
    new PartialPatternInterpreter("(partial)",Nil, true)

  )
  val defaultGreedyInterpreters : List[PatternInterpreter] =  List(
    // int
    new IntegerPatternInterpreter("\\d+",Nil, true),
    // double
    new RealPatternInterpreter("\\d+(\\.\\d+)?",Nil, true),
    // dimensions
    new MultipleRegexPatternInterpreter( "(\\d+(?:\\.\\d+)?) (?:×|x) (\\d+(?:\\.\\d+)?) (?:×|x) (\\d+(?:\\.\\d+)?)", List("and"), true),
    // date XX/XX/XXXX
    new StringPatternInterpreter("\\d{2}/\\d{2}/\\d{4}", Nil, true),
    // foo (bar)
    new PartialPatternInterpreter("([^,/]+?)\\s*\\((.+)\\)",Nil,false),
    // yes, with some condition
    new PartialPatternInterpreter("(yes),?\\s*(.*)",Nil, false),
    // values separated by comas
    new MultipleSeparatorPatternInterpreter(",", List("and"), true),
    // values separated by semi colons
    new MultipleSeparatorPatternInterpreter(";", List("and"), true),
    // values separated by slashes
    new MultipleSeparatorPatternInterpreter("/", List("and"), true),
    // foo OR bar
    new MultipleRegexPatternInterpreter("(.+)\\sor\\s(.+)", List("or"), false),
    // foo AND bar
    new MultipleRegexPatternInterpreter("(.+)\\sand\\s(.+)", List("and"), false),
    // everything
    new StringPatternInterpreter(".*", Nil, false)
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
      product <- pcm.products;
      cell <- product.cells
      if cell.interpretation.isEmpty
    ) {
      cell.interpretation = interpretStringOption(cell.content)
    }
  }

  /**
   * Find interpretation of a string according to the given pattern interpreters and lists of valid products and features
    *
    * @param verbatim : string to analyze
   */
  def interpretStringOption(verbatim: String): Option[Value] = {
    var interpretation: Option[Value] = None
    for (interpreter <- patternInterpreters if interpretation.isEmpty) {
      interpretation = interpreter.interpret(verbatim)
    }
    interpretation
  }


    
    
}