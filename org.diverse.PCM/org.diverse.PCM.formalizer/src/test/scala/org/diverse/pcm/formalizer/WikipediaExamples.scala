package org.diverse.pcm.formalizer

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.diverse.pcm.formalizer.extractor.VariabilityExtractor
import scala.io.Source
import org.diverse.pcm.formalizer.export.PCM2CSV

class WikipediaExamples extends FlatSpec with Matchers {
  
	val MODEL_EXT = ".pcm"
	val HTML_EXT = ".html"
	val CONFIG_EXT = ".config"
	val CSV_EXT = ".csv"
	  
	val PARSED_PCM_MODEL_DIR = "../examples/parsed-pcms/models/"
	val PARSED_PCM_HTML_DIR = "../examples/parsed-pcms/html/"
	val CONFIG_DIR = "../examples/config_files/"
	val FORMALIZED_PCM_MODEL_DIR = "../examples/formalized-pcms/models/"
	val FORMALIZED_PCM_HTML_DIR = "../examples/formalized-pcms/html/"
	val FORMALIZED_PCM_CSV_DIR = "../examples/formalized-pcms/csv/"
  
	val TEST_SET_FILE = "../examples/list-of-examples.txt"

		  
	it should "formalize the PCM from the example set and export it several formats" in {
		val testSet = Source.fromFile(TEST_SET_FILE).getLines.toList
				
		val variabilityExtractor = new VariabilityExtractor
		
		for (pcmName <- testSet) {
			val pcmModelFileName = pcmName + MODEL_EXT

			// Formalize
			val pcm = VariabilityExtractor.loadPCMModel(PARSED_PCM_MODEL_DIR + pcmModelFileName)
			val config = VariabilityExtractor.parseConfigurationFile(CONFIG_DIR + pcmName + CONFIG_EXT)
			variabilityExtractor.setConfiguration(config)
			variabilityExtractor.extractVariability(pcm)
			
			// Export
			VariabilityExtractor.exportPCM2Model(pcm, FORMALIZED_PCM_MODEL_DIR + pcmModelFileName)
			VariabilityExtractor.exportPCM2HTML(pcm, FORMALIZED_PCM_HTML_DIR + pcmName + HTML_EXT)
			val csvExport = new PCM2CSV
			csvExport.convertPCM2CSV(pcm, FORMALIZED_PCM_CSV_DIR + pcmName + CSV_EXT)
		}
		
	} 
	  
  
}