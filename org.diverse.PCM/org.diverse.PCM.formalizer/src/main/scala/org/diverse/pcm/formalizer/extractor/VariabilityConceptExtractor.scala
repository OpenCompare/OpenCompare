package org.diverse.pcm.formalizer.extractor

import pcmmm.PCM
import scala.collection.JavaConversions._
import pcmmm.Header
import pcmmm.VariabilityConcept
import pcmmm.Feature
import pcmmm.PcmmmFactory
import pcmmm.ValuedCell
import pcmmm.Product
import pcmmm.Constraint
import pcmmm.Simple
import pcmmm.Multiple
import pcmmm.Partial
import org.diverse.pcm.formalizer.configuration.PCMConfiguration
import pcmmm.VariabilityConceptRef

class VariabilityConceptExtractor {

  /**
   * Extract variability concepts (products and features) from headers
   * @param pcm : model of PCM
   * @param transposed : true if the rows and columns represent respectively features and products
   */
  def extractConceptsFromHeaders(pcm : PCM, config : PCMConfiguration) {

	  for (
	      matrix <- pcm.getMatrices();
	      cell <- matrix.getCells().filter(_ .isInstanceOf[Header])
	  ) {

		  val matrixConfig = config.getConfig(matrix)
		  val concept : VariabilityConcept = 
			  if (cell.getRow() < matrixConfig.headerRows) {
			    if (!matrixConfig.inverted) {
			    	PcmmmFactory.eINSTANCE.createFeature()
			    } else {
			    	PcmmmFactory.eINSTANCE.createProduct()
			    }
			  } else {
			    if (!matrixConfig.inverted) {
			    	PcmmmFactory.eINSTANCE.createProduct()
			    } else {
			    	PcmmmFactory.eINSTANCE.createFeature()
			    }
			  }
		  
		  concept.setName(cell.getVerbatim())
		  cell.asInstanceOf[Header].setConcept(concept)
		  pcm.getConcepts().add(concept)
	  }
  }
  
  /**
   * Extract variability concepts from interpreted cells
   * @param pcm : model of PCM
   */
  def extractConceptsFromInterpretedCells(pcm : PCM) {
    for (
	      matrix <- pcm.getMatrices();
	      cell <- matrix.getCells().filter(_ .isInstanceOf[ValuedCell])
	  ) {
    	val valuedCell = cell.asInstanceOf[ValuedCell]
    	val extractedConcepts = extractVariabilityConcepts(pcm, valuedCell.getInterpretation()) // FIXME
    	valuedCell.getConcepts().addAll(extractedConcepts.diff(valuedCell.getMyHeaderFeatures().toSet))
    }
  }
  
  private def extractVariabilityConcepts(pcm : PCM, interpretation : Constraint) : Set[VariabilityConcept] = {
	  interpretation match {
	    case c : VariabilityConceptRef =>
	      val concept = getConcept(pcm, c.getVerbatim())
	      c.setConcept(concept)
	      Set(concept)
	    case c : Multiple => 
	      val concepts = for (constraint <- c.getContraints()) yield {extractVariabilityConcepts(pcm, constraint)}
	      if (!concepts.isEmpty) {
	        concepts.reduceLeft((s1 ,s2) => s1 union s2)
	      } else {
	        Set.empty
	      }
	    case c : Partial => 
	      extractVariabilityConcepts(pcm, c.getCondition()).union(extractVariabilityConcepts(pcm, c.getArgument()))
	    case _ => Set.empty
	  }
  }
  
  /**
   * Get concept in PCM or create one if it does not exist
   */
  private def getConcept(pcm : PCM, name : String) : VariabilityConcept = {
    val existingConcept = pcm.getConcepts().find(c => c.getName() == name)
    if (existingConcept.isDefined) {
      existingConcept.get
    } else {
      val newFeature = PcmmmFactory.eINSTANCE.createFeature()
      newFeature.setName(name)
      pcm.getConcepts().add(newFeature)
      newFeature
    }
  }
  
  
}