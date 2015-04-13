package org.diverse.pcm.formalizer.clustering

import ch.usi.inf.sape.hac.HierarchicalAgglomerativeClusterer
import ch.usi.inf.sape.hac.agglomeration.{AgglomerationMethod, CompleteLinkage}
import ch.usi.inf.sape.hac.dendrogram.{Dendrogram, DendrogramBuilder, DendrogramNode, MergeNode, ObservationNode}

class HierarchicalClusterer[T](
		val dissimilarityMetric : (T,T) => Double,
		val threshold : Option[Double],
		val maxClusterSize : Option[Int]
) {

  
	def cluster(values : List[T]) : List[List[T]] = {
		val experiment = new ClusteringExperiment(values.toIndexedSeq) 
		val dissimilarityMeasure = new ClusteringDissimilarityMeasure(dissimilarityMetric)
		val agglomerationMethod : AgglomerationMethod = new CompleteLinkage
		val dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations())
		val clusterer = new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure, agglomerationMethod)
		clusterer.cluster(dendrogramBuilder)
		extractClusters(experiment, dendrogramBuilder.getDendrogram)
	}

	private def extractClusters[T](experiment : ClusteringExperiment[T], dendrogram : Dendrogram) : List[List[T]] = {

		def extractClustersRecursion(node : DendrogramNode) : List[List[T]] = {
			node match {
//				  case n : MergeNode if n.getDissimilarity() > threshold =>
//				    extractClustersRecursion(n.getLeft()) union extractClustersRecursion(n.getRight())
//				  case n : MergeNode =>
//				    val left = extractClustersRecursion(n.getLeft())
//				    val right = extractClustersRecursion(n.getRight())
//				    List((left ::: right).flatten)
				case n : MergeNode => {
					val left = extractClustersRecursion(n.getLeft())
					val right = extractClustersRecursion(n.getRight())

					if (
						left.size == 1 &&
							right.size == 1 &&
							(!threshold.isDefined || n.getDissimilarity < threshold.get) &&
							(!maxClusterSize.isDefined || (left.head.size + right.head.size) <= maxClusterSize.get)
					) {
						List((left ::: right).flatten)

					} else {
						left ::: right
					}
				}
				case n : ObservationNode => {
					List(experiment.getObservation(n.getObservation) :: Nil) //Set(List(experiment.getObservation(n.getObservation())))
				}

			}
		}

		if (Option(dendrogram.getRoot()).isDefined) {
			extractClustersRecursion(dendrogram.getRoot())
		} else {
			Nil
		}
	}

}