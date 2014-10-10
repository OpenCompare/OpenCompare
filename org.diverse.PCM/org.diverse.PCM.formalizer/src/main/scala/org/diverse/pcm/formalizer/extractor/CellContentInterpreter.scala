package org.diverse.pcm.formalizer.extractor

import pcmmm.PCM
import scala.collection.JavaConversions._
import pcmmm.Extra
import pcmmm.Cell
import java.util.ListIterator
import pcmmm.PcmmmFactory
import pcmmm.Constraint
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import pcmmm.Matrix
import pcmmm.Header
import pcmmm.Feature
import pcmmm.Product
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import org.diverse.pcm.formalizer.interpreters.BooleanPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.UnknownPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.EmptyPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.VariabilityConceptRefPatternInterpreter
import pcmmm.ValuedCell
import org.diverse.pcm.formalizer.interpreters.MultiplePatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PartialPatternInterpreter
import org.diverse.pcm.formalizer.configuration.PCMConfiguration
import org.diverse.pcm.formalizer.interpreters.VariabilityConceptRefPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import org.diverse.pcm.formalizer.interpreters.DoublePatternInterpreter
import org.diverse.pcm.formalizer.interpreters.IntegerPatternInterpreter

class CellContentInterpreter() {

  private var patternInterpreters : List[PatternInterpreter] = CellContentInterpreter.defaultInterpreters ::: CellContentInterpreter.defaultGreedyInterpreters
  patternInterpreters.foreach(_.setCellContentInterpreter(this))
  
  def setInterpreters(interpreters : List[PatternInterpreter]) {
      patternInterpreters = CellContentInterpreter.defaultInterpreters :::
    	  					interpreters :::
    	  					CellContentInterpreter.defaultGreedyInterpreters
      patternInterpreters.foreach(_.setCellContentInterpreter(this))
  }
  
  /**
   * Interpret each cell and specify its product and feature headers
   * @param pcm : model of PCM
   * @param patternInterpreters : variability pattern interpreters
   */
  def interpretCells(pcm : PCM, config : PCMConfiguration) {
    
    for (matrix <- pcm.getMatrices) {
    
      // Configure pattern interpreters to this matrix
      val matrixConfig = config.getConfig(matrix)
      setInterpreters(matrixConfig.getPatterns)
      for (patternInterpreter <- patternInterpreters) {
		patternInterpreter.config(pcm)
	  }
      
      // Interpret every uninterpreted cells 
      val it = matrix.getCells().listIterator()
      while (it.hasNext()) {
        val cell = it.next()
        cell match {
          case valuedCell : ValuedCell if !Option(valuedCell.getInterpretation()).isDefined =>
	           // Find concepts in headers of this cell
	           val (products, features) = findConceptsFor(valuedCell, matrix)
	       
	           // Find interpretation
	           val interpretation = findInterpretation(valuedCell.getVerbatim(), products, features)
		       
	           // Set interpretation
		       if (interpretation.isDefined) {
			         valuedCell.setInterpretation(interpretation.get)
		       }
	           
	           // Set product and feature headers
        	   valuedCell.getMyHeaderProducts.addAll(products)
        	   valuedCell.getMyHeaderFeatures.addAll(features)
          case _ => 
        }
      }
    }
    
  }
  
  /**
   * Find interpretation of a string according to the given pattern interpreters and lists of valid products and features
   * @param verbatim : string to analyze
   * @param products : header products associated with this string
   * @param features : header features associated with this string
   */
  def findInterpretation(verbatim : String, products : List[Product], features : List[Feature]) : Option[Constraint] = {
	   var interpretation : Option[Constraint] = None
	   for (interpreter <- patternInterpreters if !interpretation.isDefined) {
		 interpretation = interpreter.interpret(verbatim, products, features)
	   }
	   interpretation
  }
  
  /**
   * Find concepts for a cell
   * @return products and features related to the cell
   */
  def findConceptsFor(cell : Cell, matrix : Matrix) : (List[Product], List[Feature]) = {
    val cellRows = cell.getRow() until cell.getRow() + cell.getRowspan()
    val cellColumns = cell.getColumn() until cell.getColumn() + cell.getColspan()

    var products : List[Product] = Nil
    var features : List[Feature] = Nil
    
    for (header <- matrix.getCells().filter(_.isInstanceOf[Header]))  {
      val headerRows = header.getRow() until header.getRow() + header.getRowspan()
      val headerColumns = header.getColumn() until header.getColumn() + header.getColspan()
      

      val concept = header.asInstanceOf[Header].getConcept()
      
      if ((!headerRows.intersect(cellRows).isEmpty) || (!headerColumns.intersect(cellColumns).isEmpty)) {
        concept match {
          case p : Product => products = p :: products
          case f : Feature => features = f :: features
        }
      }
    }
    
    (products, features)
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