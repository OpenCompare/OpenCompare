package org.opencompare.api.java.clustering

import org.opencompare.hac.HierarchicalAgglomerativeClusterer
import org.opencompare.hac.agglomeration.{AgglomerationMethod, CompleteLinkage}
import org.opencompare.hac.dendrogram.{Dendrogram, DendrogramBuilder, DendrogramNode, MergeNode, ObservationNode}

class HierarchicalClusterer[T](
		val dissimilarityMetric : (T,T) => Double,
		val threshold : Option[Double],
		val maxClusterSize : Option[Int],
		val mergingCondition : Option[(List[T], List[T]) => Boolean],
		val agglomerationMethod: AgglomerationMethod = new CompleteLinkage
) {

  
	def cluster(values : List[T]) : List[List[T]] = {
		val experiment = new ClusteringExperiment(values.toIndexedSeq) 
		val dissimilarityMeasure = new ClusteringDissimilarityMeasure(dissimilarityMetric)
		//val agglomerationMethod : AgglomerationMethod = new CompleteLinkage
		val dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations())
		val clusterer = new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure, agglomerationMethod)
		clusterer.cluster(dendrogramBuilder)
		extractClusters(experiment, dendrogramBuilder.getDendrogram)
	}

	private def extractClusters(experiment : ClusteringExperiment[T], dendrogram : Dendrogram) : List[List[T]] = {

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
							(!maxClusterSize.isDefined || (left.head.size + right.head.size) <= maxClusterSize.get) &&
							(!mergingCondition.isDefined || mergingCondition.get(left.head, right.head))
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