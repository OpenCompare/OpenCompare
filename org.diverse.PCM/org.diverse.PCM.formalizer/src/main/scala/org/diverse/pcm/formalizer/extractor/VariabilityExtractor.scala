package org.diverse.pcm.formalizer.extractor

import pcmmm.PCM
import scala.collection.JavaConversions._
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import org.diverse.pcm.formalizer.interpreters.BooleanPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.UnknownPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.VariabilityConceptRefPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.MultiplePatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PatternInterpreter
import org.diverse.pcm.formalizer.interpreters.BooleanPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.PartialPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.EmptyPatternInterpreter
import org.diverse.pcm.formalizer.interpreters.InconsistentPatternInterpreter
import org.diverse.pcm.formalizer.configuration.ConfigurationFileParser
import org.diverse.pcm.formalizer.configuration.PCMConfiguration
import org.diverse.pcm.formalizer.configuration.PCMConfiguration
import org.diverse.pcm.formalizer.configuration.PCMConfiguration
import scala.io.Source
import java.io.File
import pcmmm.PcmmmPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.Diagnostician
import org.eclipse.emf.common.util.Diagnostic
import java.util.Collections
import org.diverse.pcm.formalizer.export.PCM2HTML
import java.io.FileWriter
import scala.xml.PrettyPrinter

class VariabilityExtractor {

  private var config : PCMConfiguration = new PCMConfiguration
  private val pcmNormalizer = new PCMNormalizer
  private val variabilityConceptExtractor = new VariabilityConceptExtractor
  private val cellContentInterpreter = new CellContentInterpreter
  private val domainExtractor = new DomainExtractor
  private var simpleParameters : Map[String, Int] = Map()
  private var complexParameters : Map[String,List[String]] = Map()
  
  
  def setConfiguration(configuration : PCMConfiguration) {
	  config = configuration
  }
  
  def extractVariability(pcm : PCM) {
	  // Normalize PCM
	  pcmNormalizer.normalizePCM(pcm, config)

	  // Extract features and products from headers
	  variabilityConceptExtractor.extractConceptsFromHeaders(pcm, config)
	  
	  // Interpret contents in cells (detect variability patterns (e.g. Boolean pattern))
	  // and specify header products and features related to each cell
	  cellContentInterpreter.interpretCells(pcm, config)
	  
	  // Extract features and products from cells
	  variabilityConceptExtractor.extractConceptsFromInterpretedCells(pcm)
	  
	  // Extract feature's domains
	  domainExtractor.extractDomains(pcm, config)
  }
  
}

object VariabilityExtractor {
   def loadPCMModel(file : String) : PCM = {
    // Initialize the model
    PcmmmPackage.eINSTANCE.eClass();
    
    val reg = Resource.Factory.Registry.INSTANCE
    val m = reg.getExtensionToFactoryMap()
    m.put("pcm", new XMIResourceFactoryImpl())

    // Obtain a new resource set
    val resSet = new ResourceSetImpl()

    // Get the resource
    val resource = resSet.getResource(URI.createURI(file), true)
    // Get the first model element and cast it to the right type, in my
    // example everything is hierarchical included in this first node
    val pcm = resource.getContents().get(0).asInstanceOf[PCM]
    pcm
  }
  
  def exportPCM2Model(pcm : PCM, path : String) {
     val reg = Resource.Factory.Registry.INSTANCE;
     val m = reg.getExtensionToFactoryMap();
     m.put("pcm", new XMIResourceFactoryImpl());
     val resSet = new ResourceSetImpl();
     val resource = resSet.createResource(URI.createURI(path));
     resource.getContents().add(pcm);
     resource.save(Collections.EMPTY_MAP);
	    
  }
  
  def exportPCM2HTML(pcm : PCM, path : String) {
     val htmlExport = new PCM2HTML
     val htmlCode = htmlExport.pcm2HTML(pcm)
     
     val writer = new FileWriter(path)
	 writer.write((new PrettyPrinter(80,2)).format(htmlCode))
	 writer.close()
	    
  }
  
  
  def parseConfigurationFile(configFile : String) : PCMConfiguration = {
    val configParser = new ConfigurationFileParser
	configParser.parse(configFile)
  }
}