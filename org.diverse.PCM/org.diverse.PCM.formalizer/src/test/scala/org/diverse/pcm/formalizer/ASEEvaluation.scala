package org.diverse.pcm.formalizer

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import java.io.File
import scala.io.Source
import org.diverse.pcm.formalizer.extractor.VariabilityExtractor
import org.eclipse.emf.ecore.util.Diagnostician
import org.eclipse.emf.common.util.Diagnostic
import com.github.tototoshi.csv.CSVWriter
import java.io.FileWriter
import scala.collection.JavaConversions._
import pcmmm.Header
import pcmmm.Extra
import pcmmm.ValuedCell
import pcmmm.Boolean
import pcmmm.Integer
import pcmmm.Double
import pcmmm.VariabilityConceptRef
import pcmmm.Partial
import pcmmm.Unknown
import pcmmm.Empty
import pcmmm.Inconsistent
import pcmmm.And
import pcmmm.Or
import pcmmm.XOr
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.StaticQuery
import scala.slick.jdbc.GetResult
import scala.slick.jdbc.GetResult
import pcmmm.PCM
import pcmmm.Cell
import scala.collection.mutable.ListBuffer
import pcmmm.Constraint
import test.Incoherent
import pcmmm.Multiple

class ASEEvaluation extends FlatSpec with Matchers {

  	val MODEL_EXT = ".pcm"
	val HTML_EXT = ".html"
	val CONFIG_EXT = ".config"
	  
	val INPUT_DIR = "../evaluation/input/models/"
	val CONFIG_DIR = "../evaluation/config_files/"
	val OUTPUT_DIR_MODELS = "../evaluation/output/models/"
	val OUTPUT_DIR_HTML = "../evaluation/output/html/"
  
	val TEST_SET_FILE = "../evaluation/1-pcm.txt"
	val EVALUATED_PCMS = "../evaluation/2-evaluated-pcms.txt"

	val STATS_FILE = "../evaluation/stats.csv"
	val EVAL_RESULTS_FILE = "../evaluation/eval_results.csv"
	val EVAL_NEW_CONCEPTS_FILE = "../evaluation/eval_new_concepts.csv"
	  
  
  "The evaluation" should "extract variability from every input PCM" in {
		val testSetFile = Source.fromFile(TEST_SET_FILE)
    	
    	val files = for (line <- testSetFile.getLines) yield {
    				new File(INPUT_DIR + line + MODEL_EXT)
    	}
		
	   	val variabilityExtractor = new VariabilityExtractor
		  
    	for (file <- files) {
		  // Load model
		  println(file.getName())
		  val pcm = VariabilityExtractor.loadPCMModel(file.getAbsolutePath())
		
		  // Load configuration
		  val configFile = CONFIG_DIR + file.getName.substring(0, file.getName.size - 4) + CONFIG_EXT  
		  variabilityExtractor.setConfiguration(VariabilityExtractor.parseConfigurationFile(configFile))
		
		  // Extract variability
		  variabilityExtractor.extractVariability(pcm)
		
		  // Validate model
		  val diagnostic = Diagnostician.INSTANCE.validate(pcm)
		  if (diagnostic.getSeverity() == Diagnostic.OK) {
			  println("... model is OK")
		  } else {
			  println("... model is NOT VALID")
		  }
	
		  // Save model
		  val name = file.getName().substring(0, file.getName().size - 4)
		  
		  val modelPath = OUTPUT_DIR_MODELS + name + MODEL_EXT
		  VariabilityExtractor.exportPCM2Model(pcm, modelPath)
		  
		  val htmlPath = OUTPUT_DIR_HTML + name + HTML_EXT
		  VariabilityExtractor.exportPCM2HTML(pcm, htmlPath)
		  
    	}
	 
  }
  
  it should "compute stats about the PCMs" in {
	  // Initialize CSV writer for stats
    	val csvWriter = new CSVWriter(new FileWriter(STATS_FILE))
    	val headers = Seq("Name", 
    	    "Matrices", 
    	    "Rows (av)",
    	    "Rows (min)",
    	    "Rows (max)",
    	    "Columns (av)",
    	    "Columns (min)",
    	    "Columns (max)",
    	    "Cells",
    	    "Header",
    	    "Extra",
    	    "Valued",
    	    "Boolean",
    	    "Integer",
    	    "Double",
    	    "VariabilityConceptRef",
    	    "Partial",
    	    "Unknown",
    	    "Empty",
    	    "Inconsistent",
    	    "And",
    	    "Or",
    	    "XOr")
    	    
		csvWriter.writeRow(headers)
		
		val testSetFile = Source.fromFile(EVALUATED_PCMS)
    	
    	val files = for (line <- testSetFile.getLines) yield {
    				new File(OUTPUT_DIR_MODELS + line + MODEL_EXT)
    	}
    	
		for (file <- files) {
		  // Load model
		  println(file.getName())
		  val pcm = VariabilityExtractor.loadPCMModel(file.getAbsolutePath())
		  	  
		  // Compute stats
		  val name = file.getName().substring(0, file.getName().size - 4)
		  
		  val nbMatrices = pcm.getMatrices().size()
		  
		  val cells = (for (matrix <- pcm.getMatrices()) yield {
			  matrix.getCells()
		  }).flatten.toList
		  val matrixWithLeastRows = pcm.getMatrices().sortBy(m =>
		    m.getCells().map(_.getRow).max).head
		  val matrixWithLeastColumns = pcm.getMatrices().sortBy(m =>
		    m.getCells().map(_.getColumn).max).head 
		  val rows = cells.map(_.getRow()) 
		  val columns = cells.map(_.getColumn())
		  val avRows = rows.sum.toDouble / rows.size.toDouble
		  val minRows = matrixWithLeastRows.getCells().map(_.getRow).max + 1
		  val maxRows = rows.max + 1
		  val avColumns = columns.sum.toDouble / columns.size.toDouble
		  val minColumns = matrixWithLeastColumns.getCells().map(_.getColumn).max + 1
		  val maxColumns = columns.max + 1
		  
		  val headers = cells.count(_.isInstanceOf[Header])
		  val extras = cells.count(_.isInstanceOf[Extra])
		  val valueds = cells.count(_.isInstanceOf[ValuedCell])
		  
		  val interpretations = cells.map( c =>
		  	if (c.isInstanceOf[ValuedCell]) {
		  		Some(c.asInstanceOf[ValuedCell].getInterpretation())
		  	} else {
		  		None
		  	}
		  ).flatten
		  
		  val booleans = interpretations.count(_.isInstanceOf[Boolean])
		  val integers = interpretations.count(_.isInstanceOf[Integer])
		  val doubles = interpretations.count(_.isInstanceOf[Double])
		  val vcRefs = interpretations.count(_.isInstanceOf[VariabilityConceptRef])
		  val partials = interpretations.count(_.isInstanceOf[Partial])
		  val unknowns = interpretations.count(_.isInstanceOf[Unknown])
		  val emptys = interpretations.count(_.isInstanceOf[Empty])
		  val inconsistents = interpretations.count(_.isInstanceOf[Inconsistent])
		  val ands = interpretations.count(_.isInstanceOf[And])
		  val ors = interpretations.count(_.isInstanceOf[Or])
		  val xors = interpretations.count(_.isInstanceOf[XOr])
		  
		  // Write stats to CSV file
		  val stats = Seq(name, nbMatrices, avRows, minRows, maxRows, avColumns, minColumns, maxColumns, cells.size, headers, extras, valueds,
		      booleans, integers, doubles, vcRefs, partials, unknowns, emptys, inconsistents, ands, ors, xors)
		  
		  csvWriter.writeRow(stats)
		  
		}
    	
    	csvWriter.close
	}
	
  
    // Define classes for gathering results
		case class ExperimentData(id : Int, date : String, firstName : String, 
				globalRemarks : String, lastName : String, mail : String, pcmName : String)
		case class ExperimentDataCell(id : Int, matriceId: String, mrow : Int, mcolumn : Int, 
				mvalidate : scala.Boolean, newType : String, noIdea : scala.Boolean, noInterpretation : scala.Boolean, remarks : String, experimentId : Int)
		case class ExperimentDataCellJoin(id : Int, date : String, firstName : String, 
				globalRemarks : String, lastName : String, mail : String, pcmName : String, id2 : Int, matriceId: String, mrow : Int, mcolumn : Int, 
				mvalidate : scala.Boolean, newType : String, noIdea : scala.Boolean, noInterpretation : scala.Boolean, remarks : String, experimentId : Int)
	
	
		implicit val getExperimentDataResult = GetResult(r => ExperimentData(r.nextInt, r.nextString, r.nextString,
				r.nextString, r.nextString, r.nextString, r.nextString))
		implicit val getExperimentDataCellResult = GetResult(r => ExperimentDataCell(r.nextInt, r.nextString, r.nextInt,
				r.nextInt, r.nextBoolean, r.nextString, r.nextBoolean, r.nextBoolean, r.nextString, r.nextInt))
		implicit val getExperimentDataCellJoinResult = GetResult(r => ExperimentDataCellJoin(r.nextInt, r.nextString, r.nextString,
				r.nextString, r.nextString, r.nextString, r.nextString, r.nextInt, r.nextString, r.nextInt,
				r.nextInt, r.nextBoolean, r.nextString, r.nextBoolean, r.nextBoolean, r.nextString, r.nextInt))
  
  
  
	it should "analyze the results of the user-study from the database" in {

	  // Get results from database
		var allResults : List[ExperimentDataCellJoin] = Nil
		Database.forURL("jdbc:mysql://localhost:3306/evalpcm",
		    user = "root",
		    password = "",
		    driver="com.mysql.jdbc.Driver") withSession {
		  implicit session =>
		    val query = "select * from ExperimentData inner join ExperimentDataCell on ExperimentData.id = ExperimentDataCell.experimentId"
//		    val query = "select * from ExperimentData"
		    allResults = StaticQuery.queryNA[ExperimentDataCellJoin](query).list//.foreach(r => println(r.mvalidate))
		    
		}
		
		// Analyze results
		val resultsByPCM = allResults.groupBy(_.pcmName)
		
		val testSetFile = Source.fromFile(TEST_SET_FILE)
    	
    	val files = for (line <- testSetFile.getLines) yield {
    				new File(OUTPUT_DIR_MODELS + line + MODEL_EXT)
    	}
		
		
		
		val csvWriter = new CSVWriter(new FileWriter(EVAL_RESULTS_FILE))
    	val headers = Seq("Name",
    	    "Valued cells",
    	    "Not evaluated",
    	    "Incoherent",
    	    "Valid", 
    	    "Don't know",
    	    "No interpretation",
    	    "Corrected in MM",
    	    "New concept",
    	    
    	    "Before",
    	    "Boolean",
    	    "Integer",
    	    "Double",
    	    "VariabilityConceptRef",
    	    "Partial",
    	    "Unknown",
    	    "Empty",
    	    "Inconsistent",
    	    "And",
    	    "Or",
    	    "XOr",
    	    "Multiple",
    	    
    	    "Errors",
    	    "Boolean",
    	    "Integer",
    	    "Double",
    	    "VariabilityConceptRef",
    	    "Partial",
    	    "Unknown",
    	    "Empty",
    	    "Inconsistent",
    	    "And",
    	    "Or",
    	    "XOr",
    	    "Multiple",
    	    
    	    "After",
    	    "Boolean",
    	    "Integer",
    	    "Double",
    	    "VariabilityConceptRef",
    	    "Partial",
    	    "Unknown",
    	    "Empty",
    	    "Inconsistent",
    	    "And",
    	    "Or",
    	    "XOr",
    	    "Multiple")
    	    
		csvWriter.writeRow(headers)
		
		val newConceptsComments : ListBuffer[(String, String, String, String)] = ListBuffer()
		
    	for (file <- files) {
		  // Load model
		  val pcm = VariabilityExtractor.loadPCMModel(file.getAbsolutePath())

		  val resultsForThisPCM = resultsByPCM.get(file.getName())
		  if (resultsForThisPCM.isDefined) {
			  println(file.getName())
			  val name = file.getName().substring(0, file.getName().size - 4)
			  val valuedCells = pcm.getMatrices().flatMap(_.getCells().filter(_.isInstanceOf[ValuedCell])).size
			  
			  // Count errors
			  val results = resultsForThisPCM.get
			  val extractedResults = extractResults(pcm, results)
			  
			  
			  val evaluatedCells = extractedResults.filter(c =>
			      !c.evaluation.isInstanceOf[NotEvaluated]
			      && !c.evaluation.isInstanceOf[Incoherent]
			      && !c.evaluation.isInstanceOf[DontKnow])
			  val correctedCells : ListBuffer[InterpretationEvaluation] = ListBuffer()
			  
			  var notEvaluated = 0
			  var incoherent = 0
			  var valids = 0
			  var noIdeas = 0
			  var noInterpretations = 0
			  var corrected = 0
			  var newConcepts = 0

			  
			  for (extractedResult <- extractedResults) {
				  extractedResult.evaluation match {
				    case NotEvaluated() => notEvaluated += 1
				    case Incoherent(_) => incoherent += 1
				    case Valid() => valids += 1
				    case DontKnow() => noIdeas += 1
				    case NoInterpretation() => 
				      noInterpretations += 1
				      correctedCells += extractedResult
				    case CorrectedInMM(_) => 
				      corrected += 1
				      correctedCells += extractedResult
				    case NewConcept(concept, firstName, lastName, email) => 
				      newConcepts += 1
				      correctedCells += extractedResult
				      newConceptsComments += ((concept, firstName, lastName, email)) 
				    case _ => 
				  }
			  }
			  
			  // Type of cells computed by our automated techniques
			  var booleansB = evaluatedCells.filter(_.constraint.isInstanceOf[Boolean]).size
			  var integersB = evaluatedCells.filter(_.constraint.isInstanceOf[Integer]).size
			  var doublesB = evaluatedCells.filter(_.constraint.isInstanceOf[Double]).size
			  var vcRefsB = evaluatedCells.filter(_.constraint.isInstanceOf[VariabilityConceptRef]).size
			  var partialsB = evaluatedCells.filter(_.constraint.isInstanceOf[Partial]).size
			  var unknownsB = evaluatedCells.filter(_.constraint.isInstanceOf[Unknown]).size
			  var emptysB = evaluatedCells.filter(_.constraint.isInstanceOf[Empty]).size
			  var inconsistentsB = evaluatedCells.filter(_.constraint.isInstanceOf[Inconsistent]).size
			  var andsB = evaluatedCells.filter(_.constraint.isInstanceOf[And]).size
			  var orsB = evaluatedCells.filter(_.constraint.isInstanceOf[Or]).size
			  var xorsB = evaluatedCells.filter(_.constraint.isInstanceOf[XOr]).size
			  var multiplesB = evaluatedCells.filter(c =>
			    (c.constraint.isInstanceOf[Multiple])
			    && (!c.constraint.isInstanceOf[XOr])
			    && (!c.constraint.isInstanceOf[Or])
			    && (!c.constraint.isInstanceOf[And])).size
			    
			  // Type of cells that were corrected
			  var booleansE = correctedCells.filter(_.constraint.isInstanceOf[Boolean]).size
			  var integersE = correctedCells.filter(_.constraint.isInstanceOf[Integer]).size
			  var doublesE = correctedCells.filter(_.constraint.isInstanceOf[Double]).size
			  var vcRefsE = correctedCells.filter(_.constraint.isInstanceOf[VariabilityConceptRef]).size
			  var partialsE = correctedCells.filter(_.constraint.isInstanceOf[Partial]).size
			  var unknownsE = correctedCells.filter(_.constraint.isInstanceOf[Unknown]).size
			  var emptysE = correctedCells.filter(_.constraint.isInstanceOf[Empty]).size
			  var inconsistentsE = correctedCells.filter(_.constraint.isInstanceOf[Inconsistent]).size
			  var andsE = correctedCells.filter(_.constraint.isInstanceOf[And]).size
			  var orsE = correctedCells.filter(_.constraint.isInstanceOf[Or]).size
			  var xorsE = correctedCells.filter(_.constraint.isInstanceOf[XOr]).size
			  var multiplesE = correctedCells.filter(c =>
			    (c.constraint.isInstanceOf[Multiple])
			    && (!c.constraint.isInstanceOf[XOr])
			    && (!c.constraint.isInstanceOf[Or])
			    && (!c.constraint.isInstanceOf[And])).size
			  		
			  // Type of cells after correction		  
			  var booleansA = booleansB - booleansE
			  var integersA = integersB - integersE
			  var doublesA = doublesB - doublesE
			  var vcRefsA = vcRefsB - vcRefsE
			  var partialsA = partialsB - partialsE
			  var unknownsA = unknownsB - unknownsE
			  var emptysA = emptysB - emptysE
			  var inconsistentsA = inconsistentsB - inconsistentsE
			  var andsA = andsB - andsE
			  var orsA = orsB - orsE
			  var xorsA = xorsB - xorsE
			  var multiplesA = multiplesB - multiplesE
			  
			  for (correctedCell <- correctedCells) {
				  correctedCell.evaluation match {
				    case CorrectedInMM(concept) =>
				      concept match {
				        case "Boolean" => booleansA += 1
				        case "Integer" => integersA += 1
				        case "Double" => doublesA += 1
				        case "VariabilityConceptRef" => vcRefsA += 1
				        case "Partial" => partialsA += 1
				        case "Unknown" => unknownsA += 1
				        case "Empty" => emptysA += 1
				        case "Inconsistent" => inconsistentsA += 1
				        case "And" => andsA += 1
				        case "Or" => orsA += 1
				        case "Xor" => xorsA += 1
				        case "Multiple" => multiplesA += 1
				        case _ => println("Error! Concept unknown : " + concept)
				      }
				    case _ =>
				  }
			  }
			  
			  
			  
			  csvWriter.writeRow(Seq(name, valuedCells, notEvaluated, incoherent, valids, noIdeas, noInterpretations, corrected, newConcepts,
			      "", booleansB, integersB, doublesB, vcRefsB, partialsB, unknownsB, emptysB, inconsistentsB, andsB, orsB, xorsB, multiplesB,
			      "", booleansE, integersE, doublesE, vcRefsE, partialsE, unknownsE, emptysE, inconsistentsE, andsE, orsE, xorsE, multiplesE,
			      "", booleansA, integersA, doublesA, vcRefsA, partialsA, unknownsA, emptysA, inconsistentsA, andsA, orsA, xorsA, multiplesA))
			  
		  }
		  
    	}
		
		csvWriter.close
		
		val distinctNewConceptsComments = newConceptsComments.sortBy(_._4).groupBy(_._1)
		val conceptsWriter = new CSVWriter(new FileWriter(EVAL_NEW_CONCEPTS_FILE))
		distinctNewConceptsComments.foreach(c => conceptsWriter.writeRow(Seq(c._1, c._2.size)))
		conceptsWriter.close
			
		
	}  
	
	def extractResults(pcm : PCM, results : List[ExperimentDataCellJoin]) : List[InterpretationEvaluation] = {
		val extractedResults : ListBuffer[InterpretationEvaluation] = ListBuffer()
		
		for (matrix <- pcm.getMatrices(); cell <- matrix.getCells() if cell.isInstanceOf[ValuedCell]) {
//		  println(matrix.getId())
//		  println(results.map(r => r.matriceId).distinct)
			val resultsForThisCell = results.filter(r => 
			  (r.matriceId == matrix.getId()
			  && r.mrow == cell.getRow()
			  && r.mcolumn == cell.getColumn())
			  )
			  
			val evaluations = resultsForThisCell.map(r => convertToEvaluation(r))

			val evaluation = if (evaluations.isEmpty) {
//				println(matrix.getId() + " : " + cell.getRow() + ", " + cell.getColumn())
				NotEvaluated()
			} else if (evaluations.size == 1) {
				evaluations.head
			} else {
				if (evaluations.groupBy(_.getClass()).size == 1) {
					evaluations.head
				} else {
					Incoherent(evaluations) 
				}
				  
				
			}
			val interpretation = cell.asInstanceOf[ValuedCell].getInterpretation()
			extractedResults += InterpretationEvaluation(cell, interpretation, evaluation)
		}
		
		extractedResults.toList
	}
	
	def convertToEvaluation(result : ExperimentDataCellJoin) : Evaluation = {
		if (result.mvalidate) {
			Valid()
		} else if (result.noIdea) {
			DontKnow()
		} else if (result.noInterpretation) {
			NoInterpretation()
		} else if (Option(result.newType).isDefined) {
			CorrectedInMM(result.newType)
		} else if (Option(result.remarks).isDefined) {
			NewConcept(result.remarks, result.firstName, result.lastName, result.mail)
		} else { // Should not happen
			NotEvaluated()
		}
	}
}