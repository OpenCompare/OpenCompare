package org.opencompare.api.java.clustering

import org.opencompare.hac.experiment.Experiment

class ClusteringExperiment[T](values : IndexedSeq[T]) extends Experiment {

  val observations = values
  
	override def getNumberOfObservations() : Int = {
		observations.size
	}
  
	def getObservation(index : Int) : T = {
		observations(index)
	}
  
}