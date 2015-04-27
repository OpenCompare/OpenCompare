package org.diverse.pcm.formalizer.clustering

import ch.usi.inf.sape.hac.experiment.Experiment

class ClusteringExperiment[T](values : IndexedSeq[T]) extends Experiment {

  val observations = values
  
	override def getNumberOfObservations() : Int = {
		observations.size
	}
  
	def getObservation(index : Int) : T = {
		observations(index)
	}
  
}