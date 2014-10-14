//package org.diverse.pcm.formalizer.extractor
//
//import pcmmm.PCM
//import pcmmm.PcmmmFactory
//import org.diverse.pcm.formalizer.clustering.HierarchicalClusterer
//import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein
//import scala.collection.JavaConversions._
//import pcmmm.Feature
//import pcmmm.Cell
//import pcmmm.Multiple
//import pcmmm.ValuedCell
//import pcmmm.Constraint
//import pcmmm.Partial
//import pcmmm.Unknown
//import pcmmm.Inconsistent
//import pcmmm.Empty
//import pcmmm.Domain
//import uk.ac.shef.wit.simmetrics.similaritymetrics.SmithWaterman
//import pcmmm.Integer
//import pcmmm.VariabilityConceptRef
//import pcmmm.Simple
//import org.diverse.pcm.formalizer.configuration.PCMConfiguration
//
//class DomainExtractor {
//
//  val metric = new Levenshtein
//  val dissimilarityMetric : (Simple, Simple) => Double = (v1, v2) =>
//	     1 - metric.getSimilarity(v1.getVerbatim().toLowerCase(), v2.getVerbatim().toLowerCase())
//  val cellClusterer = new HierarchicalClusterer(dissimilarityMetric, 0.5)
//
//  def extractDomains(pcm : PCM, config : PCMConfiguration) {
//	  val domainCollection = PcmmmFactory.eINSTANCE.createDomainCollection()
//	  pcm.setDomainCollection(domainCollection)
//
//	  val domains = for (concept <- pcm.getConcepts()) yield {
//		  concept match {
//		    case feature : Feature if !feature.getMyValuedCells().isEmpty() =>
//		     	Some(extractDomain(feature, config.filterDomainValues))
//		    case feature : Feature => Some(setDefaultDomain(feature))
//		    case _ => None
//		  }
//	  }
//	  domainCollection.getDomains().addAll(domains.flatten.toList)
//  }
//
//  def extractDomain(feature : Feature, filtered : Boolean) : Domain = {
//	  val values = feature.getMyValuedCells().flatMap(cell => listValues(cell.getInterpretation())).toList
//
//	  val domainValues = if (filtered) {
//		  // Separate values according to types
//		  val numberDomain = values.filter(v => v.isInstanceOf[Integer] || v.isInstanceOf[Double])
//		  val booleanDomain = values.filter(_.isInstanceOf[pcmmm.Boolean])
//		  val variabilityConceptDomain = values.filter(_.isInstanceOf[VariabilityConceptRef])
//
//		  // Get most represented type
//		  val mainType = List(numberDomain, booleanDomain, variabilityConceptDomain).maxBy(_.size)
//
//
//		  // Cluster values if the type is String
//		  val filteredValues = if (!mainType.isEmpty && mainType.head.isInstanceOf[VariabilityConceptRef]) {
//
//			  val clusters = cellClusterer.cluster(values)
//			  val significantClusters = selectSignificantClusters(clusters, values.size)
//
//			  if (significantClusters.isEmpty) {
//			    Nil
//			  } else {
//			    significantClusters.reduceLeft(_ union _)
//			  }
//		  } else {
//			  mainType
//		  }
//
//		  filteredValues
//	  } else {
//	    values
//	  }
//
//	  // Create domain
//	  val domain = PcmmmFactory.eINSTANCE.createEnum()
//	  domain.getValues().addAll(domainValues)
//
//	  // Add domain to the feature
//	  feature.setDomain(domain)
//
//	  domain
//  }
//
//  /**
//   * List Simple and Boolean constraints from the interpretation of a cell
//   * (Unknown, Empty and Inconsistent constraints are ignored)
//   * (Partial and Multiple constraints are decomposed)
//   */
//  def listValues(interpretation : Constraint) : List[Simple] = {
//     interpretation match {
//       	case _ : Inconsistent => Nil
//	    case _ : Unknown => Nil
//	    case _ : Empty => Nil
//	    case c : Multiple => c.getContraints().flatMap(listValues(_)).toList
//	    case c : Partial => listValues(c.getArgument()) ::: listValues(c.getCondition())
//	  	case c : Simple if Option(c).isDefined => List(c)
//	  	case _ => Nil
//	  }
//  }
//
//  /**
//   * Select clusters that are significant enough to be integrated in the domain of a feature
//   */
//  def selectSignificantClusters(clusters : List[List[Simple]], nbOfValues : Int) : List[List[Simple]] = {
//		var significantClusters : List[List[Simple]] = Nil
//
////		println("--------------->\t" + clusters )
////  		clusters.foreach(cluster => {println(cluster.distinct)
////  		    println(cluster.distinct.size)})
//
//		val bigClusterThreshold = 0.20
//
//		for (cluster <- clusters) {
//
//			// Big clusters
//			if ((cluster.size.toDouble / nbOfValues.toDouble) > bigClusterThreshold) {
//				significantClusters ::= cluster
//			}
//			// Same value written 10 times
//			else if (cluster.distinct.size == 1 && cluster.size >= 10) {
////				println("===========>\t" + cluster )
//				significantClusters ::= cluster
//			}
//		}
//
//		significantClusters
//  }
//
//
//  def setDefaultDomain(feature : Feature) : Domain = {
//		  val domain = PcmmmFactory.eINSTANCE.createEnum()
//		  feature.setDomain(domain)
//		  domain
//  }
//
//
//}