package org.diverse.pcm.formalizer

import java.io.File
import java.io.FileWriter
import java.util.Collections
import scala.collection.JavaConversions.asScalaBuffer
import scala.io.Source
import scala.xml.PrettyPrinter
import org.eclipse.emf.common.util.Diagnostic
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.util.Diagnostician
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.diverse.pcm.formalizer.clustering.HierarchicalClusterer
import org.diverse.pcm.formalizer.export.PCM2HTML
import org.diverse.pcm.formalizer.extractor.PCMNormalizer
import org.diverse.pcm.formalizer.extractor.VariabilityExtractor
import pcmmm.Cell
import pcmmm.Extra
import pcmmm.Feature
import pcmmm.Header
import pcmmm.PCM
import pcmmm.PcmmmPackage
import pcmmm.ValuedCell
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein
import org.diverse.pcm.formalizer.extractor.DomainExtractor
import pcmmm.Enum
import pcmmm.PcmmmFactory
import org.eclipse.emf.ecore.util.EcoreUtil

class VariabilityExtractorTest extends FlatSpec with Matchers {

  def loadPCMModel(file : File) : PCM = {
    // Initialize the model
    PcmmmPackage.eINSTANCE.eClass();
    
    // Register the XMI resource factory for the .website extension

    val reg = Resource.Factory.Registry.INSTANCE
    val m = reg.getExtensionToFactoryMap()
    m.put("pcm", new XMIResourceFactoryImpl())

    // Obtain a new resource set
    val resSet = new ResourceSetImpl()

    // Get the resource
    val resource = resSet.getResource(URI.createURI(file.getAbsolutePath()), true)
    // Get the first model element and cast it to the right type, in my
    // example everything is hierarchical included in this first node
    val pcm = resource.getContents().get(0).asInstanceOf[PCM]
    pcm
  }
  
  def savePCMModel(pcm : PCM, name : String) {
     val path = "output/models/" + name
     
     // Save model in file
     val reg = Resource.Factory.Registry.INSTANCE;
     val m = reg.getExtensionToFactoryMap();
     m.put("pcm", new XMIResourceFactoryImpl());
     val resSet = new ResourceSetImpl();
     val resource = resSet.createResource(URI.createURI(path));
     resource.getContents().add(pcm);
     resource.save(Collections.EMPTY_MAP);
     
     // Save model in HTML file
     val htmlExport = new PCM2HTML
     val htmlCode = htmlExport.pcm2HTML(pcm)
     val title = name.substring(0, name.size - 4)
     
     val writer = new FileWriter("output/html/" + title.replaceAll(" ", "_") + ".html")
	 writer.write((new PrettyPrinter(80,2)).format(htmlCode))
	 writer.close()
	    
  }
  
  def computeWarnings(pcm : PCM) : List[(ValuedCell, String)] = {
	var warnings : List[(ValuedCell, String)] = Nil 
      for (matrix <- pcm.getMatrices();
    		  cell <- matrix.getCells() if cell.isInstanceOf[ValuedCell]) {
    	  val valuedCell = cell.asInstanceOf[ValuedCell]
    	  val headerFeature = valuedCell.getMyHeaderFeatures().head.asInstanceOf[Feature]
    	  val domain = headerFeature.getDomain().asInstanceOf[Enum]
    	  
    	  val domainExtractor = new DomainExtractor
    	  val values = domainExtractor.listValues(valuedCell.getInterpretation())
    	  
    	  for (value <- values if !domain.getValues().contains(value)) {
    		warnings ::= (valuedCell, value.getVerbatim())
    	  }
      }
	warnings
  }
  
  def setWarningAsInconsistentCell(warnings : List[(ValuedCell, String)]) {
	for (warning <- warnings) {
		val cell = warning._1
		// Remove old interpretation
		val oldInterpretation = cell.getInterpretation()
		EcoreUtil.delete(oldInterpretation,true)
		
		// Create new interpretation
		val interpretation = PcmmmFactory.eINSTANCE.createInconsistent()
		interpretation.setName("Warning: " +  cell.getVerbatim())
		cell.setInterpretation(interpretation)
		
	}
  } 
  
 
  "VariabilityExtractor" should "run on every input file" in {
	  val variabilityExtractor = new VariabilityExtractor
    
	  var sumValuedCells : Double = 0
	  var sumInterpretedCells : Double = 0
	  var sumAveragePerMatrix : Double = 0
	  var nbMatrix : Double = 0
	  
	  val modelFolder = new File("../WikipediaPCMParser/output/models")
	  for (file <- modelFolder.listFiles()) {
	    // Load model
	    println(file.getName())
	    val pcm = loadPCMModel(file)
	    
	    // Load configuration
	    val configFile = "input/configs/" + file.getName.substring(0, file.getName.size - 4) + ".config"  
	    variabilityExtractor.setConfiguration(VariabilityExtractor.parseConfigurationFile(configFile))
	    
	    // Extract variability
	    variabilityExtractor.extractVariability(pcm)
	    
	    // Validate and save model
	    val diagnostic = Diagnostician.INSTANCE.validate(pcm)
	    if (diagnostic.getSeverity() == Diagnostic.OK) {
	      println("OK")
	    } else {
	      println("NOT VALID")
	    }
	    
	    // Save model
	    savePCMModel(pcm, file.getName())
	    	
	    // Compute stats on interpreted cells
	    for (matrix <- pcm.getMatrices()) yield {
		  val cells = matrix.getCells()
		  
		  val valuedCells = cells.filter(cell => cell.isInstanceOf[ValuedCell])
		   val interpretedCells = valuedCells.filter(cell => 
			    Option(cell.asInstanceOf[ValuedCell].getInterpretation()).isDefined &&
			    cell.asInstanceOf[ValuedCell].getInterpretation().isConfident
			    )
		  
		  if (valuedCells.size > 0) {
			  sumValuedCells += valuedCells.size
			  sumInterpretedCells += interpretedCells.size
			  val averageMatrix : Double = interpretedCells.size.toDouble / valuedCells.size.toDouble
			  sumAveragePerMatrix += averageMatrix
			  nbMatrix += 1
		  	  println("\t\t" + (averageMatrix * 100).toInt + "% of interpreted cells")
		  }else {
		    println("\t\tno valued cells")
		  }
		  
	    }
	  }
	  
	  println("Average per cell : " + ((sumInterpretedCells * 100) / sumValuedCells).toInt + "%" + " (" + sumInterpretedCells.toInt + "/" + sumValuedCells.toInt + ")")
	  println("Average per matrix : " + ((sumAveragePerMatrix * 100)/ nbMatrix).toInt + "%")
  }
  
  it should "run on nikon DSLR PCM" in {
      val file = new File("../WikipediaPCMParser/output/models/Comparison_of_Nikon_DSLR_cameras.pcm")
      val configFile = new File("input/configs/Comparison_of_Nikon_DSLR_cameras.config")
	  val pcm = loadPCMModel(file)
	  
	  val variabilityExtractor = new VariabilityExtractor
      variabilityExtractor.setConfiguration(VariabilityExtractor.parseConfigurationFile(configFile.getAbsolutePath()))
	  variabilityExtractor.extractVariability(pcm)
	  
	  // Compute warnings
	  val warnings = computeWarnings(pcm)
	  warnings.foreach(warning => 
	    println("WARNING : \"" + warning._2 + 
    		      "\" in cell (" + 
    		      warning._1.getRow() + "," + warning._1.getColumn() + 
    		      ") is inconsistent"))
	  setWarningAsInconsistentCell(warnings)
	 
	   // Validate model
	  val diagnostic = Diagnostician.INSTANCE.validate(pcm)
	  if (diagnostic.getSeverity() == Diagnostic.OK) {
		println("OK")
	  } else {
	    println(diagnostic.getSeverity())
	    println(diagnostic)
		println("NOT VALID")
	  }
	  
	  // Save model
	  savePCMModel(pcm, file.getName())
	  
	 
	  
	  // Compute number of interpreted cells
	  for (matrix <- pcm.getMatrices()) {
		  val valuedCells = matrix.getCells().filter(cell => cell.isInstanceOf[ValuedCell])
		  val interpretedCells = valuedCells.filter(cell => Option(cell.asInstanceOf[ValuedCell].getInterpretation()).isDefined)
		  if (valuedCells.size != 0) {
			  println((interpretedCells.size * 100) / valuedCells.size + "% of non extra cells"  + 
			      " (" + interpretedCells.size + "/" + valuedCells.size + ")")
		  }
	  }

  }
  
  it should "run on the test set" in {
	  // get files from test set
	  val testSetFile = Source.fromFile("input/test_set.txt") 
	  val files = for (line <- testSetFile.getLines) yield {
	    new File("../WikipediaPCMParser/output/models/" + line + ".pcm")
	  }
	  
	  val variabilityExtractor = new VariabilityExtractor
	
	  var sumValuedCells : Double = 0
	  var sumInterpretedCells : Double = 0
	  var sumAveragePerMatrix : Double = 0
	  var nbMatrix : Double = 0
	
	  for (file <- files) {
		  // Load model
		  println(file.getName())	
		  val pcm = loadPCMModel(file)
		
		  // Load configuration
		  val configFile = "input/configs/" + file.getName.substring(0, file.getName.size - 4) + ".config"  
		  variabilityExtractor.setConfiguration(VariabilityExtractor.parseConfigurationFile(configFile))
		
		  // Extract variability
		  variabilityExtractor.extractVariability(pcm)
		
		  // Compute warnings
//		  val warnings = computeWarnings(pcm)
//		  setWarningAsInconsistentCell(warnings)
		  
		  // Validate model
		  val diagnostic = Diagnostician.INSTANCE.validate(pcm)
		  if (diagnostic.getSeverity() == Diagnostic.OK) {
			  println("... model is OK")
		  } else {
			  println("... model is NOT VALID")
		  }
	
		  // Save model
		  savePCMModel(pcm, file.getName())
		
		  // Compute stats on interpreted cells
		  for (matrix <- pcm.getMatrices()) yield {
			  val cells = matrix.getCells()
		
			  val valuedCells = cells.filter(cell => cell.isInstanceOf[ValuedCell])
			  val interpretedCells = valuedCells.filter(cell => 
			    Option(cell.asInstanceOf[ValuedCell].getInterpretation()).isDefined &&
			    cell.asInstanceOf[ValuedCell].getInterpretation().isConfident
			    )
		
			  if (valuedCells.size > 0) {
				  sumValuedCells += valuedCells.size
				  sumInterpretedCells += interpretedCells.size
				  val averageMatrix : Double = interpretedCells.size.toDouble / valuedCells.size.toDouble
				  sumAveragePerMatrix += averageMatrix
				  nbMatrix += 1
				  println("\t\t" + (averageMatrix * 100).toInt + "% of cells with confident interpretation")
			  }else {
				  println("\t\tno valued cells")
			  }
	
		  }
		  
      // Display warnings
//	  warnings.foreach(warning => 
//	    println("WARNING : \"" + warning._2 + 
//    		      "\" in cell (" + 
//    		      warning._1.getRow() + "," + warning._1.getColumn() + 
//    		      ") is inconsistent"))
	  }
	
	  println("Average per cell : " + ((sumInterpretedCells * 100) / sumValuedCells).toInt + "%" + " (" + sumInterpretedCells.toInt + "/" + sumValuedCells.toInt + ")")
	  println("Average per matrix : " + ((sumAveragePerMatrix * 100)/ nbMatrix).toInt + "%")
  }
  
  
  it should "generate the model for the demo" in {
	  val variabilityExtractor = new VariabilityExtractor
	  
	  // Load model
	  val file = new File("../evaluation/input/demo.pcm")
	  val pcm = loadPCMModel(file)

	  // Load configuration
	  val configFile = "../evaluation/input/demo.config"  
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
	  savePCMModel(pcm, file.getName())
  }
  
   "PCMNormalizer" should "set headers correctly" in {
	  val pcmNormalizer = new PCMNormalizer
	  val file = new File("../WikipediaPCMParser/output/models/Comparison_of_Nikon_DSLR_cameras.pcm")
	  val pcm = loadPCMModel(file)
	  
	  // Set headers
	  for  (matrix <- pcm.getMatrices()) {
		  pcmNormalizer.setHeaders(matrix)
	  }
	  
	  // Check matrices
	  for (matrix <- pcm.getMatrices(); cell <- matrix.getCells()) {
	    val row = cell.getRow()
	    val column = cell.getColumn()
	    
	    // Check that headers are in first row and column
	    if (row == 0 && column == 0 ) {
	      cell shouldBe an [Extra]
	    } else if (row == 0) {
	      cell shouldBe a [Header]
	    } else if (column == 0) {
	      cell shouldBe a [Header]
	    } else {
	      cell should not be a [Header]
	    }
	    
	  }
  }
   
   "CellCluster" should "cluster cells" in {
	   val file = new File("output/models/Comparison_of_Nikon_DSLR_cameras.pcm")
	   val pcm = loadPCMModel(file)
     
	   val feature = pcm.getConcepts().find(concept => concept match {
	     case f : Feature => f.getName() == "ISO max"
	     case _ => false
	   }).get.asInstanceOf[Feature]
	   
	   val cells = feature.getMyValuedCells()

	   val metric = new Levenshtein
	   val dissimilarityMetric : (Cell, Cell) => Double = (v1, v2) => 
	     1 - metric.getSimilarity(v1.getVerbatim(), v2.getVerbatim())
	   val threshold = 0.4
	   val cellClusterer = new HierarchicalClusterer(dissimilarityMetric, threshold)
	   
	   val clusters = cellClusterer.cluster(cells.toList)
	   for (cluster <- clusters) {
		   	val verbatims = cluster.toList.map(c => c.getVerbatim())
			println(verbatims.mkString("{", ", ", "}") + " : " + cluster.size )
	   }
   }
   
}