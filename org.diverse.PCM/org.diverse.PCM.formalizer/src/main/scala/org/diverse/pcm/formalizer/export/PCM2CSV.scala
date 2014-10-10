package org.diverse.pcm.formalizer.export

import pcmmm.PCM
import com.github.tototoshi.csv.CSVWriter
import scala.collection.JavaConversions._
import pcmmm.Feature
import pcmmm.Product
import java.io.Writer
import java.io.FileWriter
import pcmmm.Enum
import pcmmm.Integer
import pcmmm.VariabilityConceptRef
import pcmmm.Double
import pcmmm.Boolean

class PCM2CSV {

	def convertPCM2CSV(pcm : PCM, path : String) {
		val writer = new CSVWriter(new FileWriter(path))
		val (features, products) = getFeaturesAndProducts(pcm)
		
		val domains = features.map(f => f.getDomain() match {
		  case d : Enum if d.getValues.forall(_.isInstanceOf[VariabilityConceptRef]) => "ENUM"
		  case d : Enum if d.getValues.forall(_.isInstanceOf[Integer]) => "INT(" + java.lang.Integer.SIZE + ")"
		  case d : Enum if d.getValues.forall(v => v.isInstanceOf[Double] || v.isInstanceOf[Integer]) => "DOUBLE(" + java.lang.Double.SIZE + ")"
		  case d : Enum if d.getValues.forall(_.isInstanceOf[Boolean]) => "BOOLEAN"
		  case _ => "AMBIGUOUS"
		})
		
		writer.writeRow(features.map(_.getName()))
		writer.writeRow(domains)
		for (product <- products) {
		  val cells = product.getMyValuedCells().sortBy(c => (c.getRow,c.getColumn))
		  val values = cells.map(_.getVerbatim())
		  writer.writeRow(values)
		}
		
		writer.close
	}
	
	def getFeaturesAndProducts(pcm : PCM) : (List[Feature], List[Product]) = {
	  val features = for (concept <- pcm.getConcepts() if concept.isInstanceOf[Feature]) yield {
		  val feature = concept.asInstanceOf[Feature]
		  if (feature.getMyValuedCells().isEmpty()) {
			  None
		  } else {
			  Some(feature)
		  }
	  }
	  
	  val products = for (concept <- pcm.getConcepts() if concept.isInstanceOf[Product]) yield {
		  concept.asInstanceOf[Product]
	  }
	  
	  (features.flatten.toList, products.toList)
	}
  
}