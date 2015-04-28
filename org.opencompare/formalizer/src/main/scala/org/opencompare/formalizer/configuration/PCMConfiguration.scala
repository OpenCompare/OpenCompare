//package org.opencompare.formalizer.configuration
//
//import pcmmm.Matrix
//
//class PCMConfiguration {
//
//	var defaultConfiguration : MatrixConfiguration = new MatrixConfiguration
//	var matrixConfigurations : Map[(String, Int), MatrixConfiguration] = Map()
//	var filterDomainValues : Boolean = true
//
//	def getConfig(matrix : Matrix) : MatrixConfiguration = {
//	  val id = matrix.getId()
//	  val lastUnderscore = id.lastIndexOf("_")
//	  val name = id.substring(0, lastUnderscore)
//	  val index = Integer.parseInt(id.substring(lastUnderscore+1, id.length()))
//
//	  matrixConfigurations.getOrElse((name, index),
//			  matrixConfigurations.getOrElse((name, -1), defaultConfiguration)
//	  )
//	}
//}