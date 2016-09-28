package org.opencompare.api.scala.io

import org.opencompare.api.scala._
import org.opencompare.api.scala.interpreter.CellContentInterpreter
import org.opencompare.api.scala.metadata._

class ImportMatrixLoader(val cellContentInterpreter: CellContentInterpreter, orientation : PCMOrientation) {


  def load(matrix: ImportMatrix) : PCM = {
    // Detect types and information for each cell
    detectTypes(matrix)

    // Expand rowpsan and colspan
    matrix.flattenCells()

    // Remove holes in matrix
    removeHoles(matrix)

    // Detect orientation of the matrix
    val detectedOrientation = orientation match {
      case Unknown() => detectOrientation(matrix)
      case _ => orientation
    }

    // Remove empty and duplicated rows
    matrix.removeEmptyRows()
    matrix.removeDuplicatedRows()

    // Remove empty and duplicated columns
    matrix.transpose()
    matrix.removeEmptyRows()
    matrix.removeDuplicatedRows()

    // Transpose matrix if necessary
    detectedOrientation match {
      case ProductsAsRows() => matrix.transpose()
      case _ =>
    }

    // Create PCM
    val pcm = new PCM with Orientation with Positions
    pcm.name = matrix.name

    // Create features
    val featureTreeRoot = detectFeatures(matrix)
    val (topFeatures, positionToFeature) = createFeatures(featureTreeRoot)
    pcm.features = topFeatures.toSet

    // Set feature positions in metadata
    pcm.featurePositions = positionToFeature.map(e => e._2 -> e._1)

    // Create products
    createProducts(matrix, pcm, positionToFeature)

    // Set products key
    setProductsKey(pcm)

    pcm
  }

  /**
    * Detect types of each cell of the matrix
    * @param matrix matrix
    */
  protected def detectTypes(matrix: ImportMatrix): Unit = {
    for (row <- 0 until matrix.numberOfRows;
         column <- 0 until matrix.numberOfColumns) {
      matrix.getCell(row, column).foreach { cell =>
        if (cell.interpretation.isEmpty) {
          cell.interpretation = cellContentInterpreter.interpretString(cell.content)
        }
      }
    }
  }

  protected def removeHoles(matrix: ImportMatrix): Unit = {
    for (row <- 0 until matrix.numberOfRows;
         column <- 0 until matrix.numberOfColumns) {
      val cell = matrix.getCell(row, column)
      if (cell.isEmpty) {
        matrix.setCell(new ImportCell(), row, column)
      }
    }
  }

  protected def detectOrientation(matrix: ImportMatrix): PCMOrientation = {

    // Compute homogeneity of rows
    val homogeneityOfRows = for (row <- 0 until matrix.numberOfRows) yield {
      // Get types
      val types = (for (column <- 0 until matrix.numberOfColumns) yield {
        matrix.getCell(row, column).flatMap(_.interpretation.map(_.getClass.getName))
      }).flatten

      // Count the number of cells with the main types
      val mainType = types.groupBy(identity[String]).map(_._2.size).max

      // Compute homogeneity of the row
      val homogeneity = mainType.toDouble / matrix.numberOfRows().toDouble
      homogeneity
    }

    val globalHomogeneityOfRows = homogeneityOfRows.sum / matrix.numberOfRows

    // Compute homogeneity of columns
    val homogeneityOfColumns = for (column <- 0 until matrix.numberOfColumns) yield {
      // Get types
      val types = (for (row <- 0 until matrix.numberOfRows) yield {
        matrix.getCell(row, column).flatMap(_.interpretation.map(_.getClass.getName))
      }).flatten

      // Count the number of cells with the main types
      val mainType = types.groupBy(identity[String]).map(_._2.size).max

      // Compute homogeneity of the row
      val homogeneity = mainType.toDouble / matrix.numberOfColumns().toDouble
      homogeneity
    }

    val globalHomogeneityOfColumns = homogeneityOfColumns.sum / matrix.numberOfColumns

    if (globalHomogeneityOfRows > globalHomogeneityOfColumns) {
      ProductsAsColumns()
    } else {
      ProductsAsRows()
    }


  }


  class IONode(var content : String, var children : List[IONode] = List.empty[IONode], var positions : Set[Int] = Set.empty[Int]) {
    def isLeaf() : Boolean = children.isEmpty
    def leaves() : List[IONode] = if (isLeaf()) {
      List(this)
    } else {
      children.flatMap(_.leaves())
    }
  }

  protected def detectFeatures(matrix : ImportMatrix) : IONode = {

    val root = new IONode("")
    var parents = List.empty[IONode]

    // Init parents
    for (column <- 0 until matrix.numberOfColumns()) {
      parents = root :: parents
    }
    parents = parents.reverse

    // Detect features
    for (row <- 0 until matrix.numberOfRows()) {
      var nextParents = parents

      for (column <- 0 until matrix.numberOfColumns()) {
        val currentCell = matrix.getCell(row, column).getOrElse(new IOCell())

        val parent = parents(column)

        val sameAsParent = currentCell.content == parent.content
        var sameAsPrevious = false
        var sameParentAsPrevious = true

        if (column > 0) {
          val previousCell = matrix.getCell(row, column - 1).getOrElse(new IOCell())

          sameAsPrevious = currentCell.content == previousCell.content

          // if (parent.content != null) {
          sameParentAsPrevious = parent.content == parents(column - 1).content
          // }
        }

        if (!sameAsParent && (!sameParentAsPrevious || !sameAsPrevious)) {

          val newNode = new IONode(currentCell.content)
          newNode.positions += column
          parent.children = parent.children :+ newNode
          nextParents = nextParents.patch(column, Seq(newNode), 1)

        } else if (column > 0 && sameParentAsPrevious && sameAsPrevious) {
          val previousNode = nextParents(column - 1)
          previousNode.positions += column
          nextParents.patch(column, Seq(previousNode), 1)
        }

      }

      parents = nextParents

      if (root.leaves().size == matrix.numberOfColumns()) {
        return root
      }

    }

    root
  }

  def createFeatures(parent : IONode) : (List[AbstractFeature], Map[Int, Feature]) = {

    val result = parent.children.map { child =>
      if (child.isLeaf()) {
        val feature = new Feature
        feature.name = child.content


        val featureToPosition = (for (position <- child.positions) yield {
          position -> feature
        }).toMap

        (List(feature), featureToPosition)

      } else {
        val featureGroup = new FeatureGroup
        featureGroup.name = child.content

        val (subFeatures, positionToFeature) = createFeatures(child)
        featureGroup.subFeatures = subFeatures
        (List(featureGroup), positionToFeature)
      }
    }

    val unzippedResult = result.unzip
    val features = unzippedResult._1.flatten
    val featureToPosition = unzippedResult._2.flatten.toMap

    (features, featureToPosition)
  }

  def createProducts(matrix : ImportMatrix, pcm : PCM with Positions, positionToFeature : Map[Int, Feature]): Unit = {
    val depthOfFeatureHierarchy = pcm.depthOfFeatureHierarchy()

    val (products, positions) = (for (row <- depthOfFeatureHierarchy until matrix.numberOfRows()) yield {
      val cells = for (column <- 0 until matrix.numberOfColumns()) yield {
        val cell = new Cell
        cell.feature = positionToFeature(column)

        val ioCell = matrix.getCell(row, column).getOrElse(new ImportCell())
        cell.content = ioCell.content
        cell.rawContent = ioCell.rawContent
        cell.interpretation = ioCell.interpretation
        cell
      }

      val product = new Product
      product.cells = cells.toSet
      (product, product -> row)
    }).unzip

    pcm.products = products.toSet
    pcm.productPositions = positions.toMap

  }


  def setProductsKey(pcm : PCM with Positions): Unit = {
    // TODO

    // Find feature that can be the products key
    val productsKey = pcm.sortedFeatures().find { feature =>
      feature.cells.map(_.content).size == feature.cells.size
    }

    if (productsKey.isDefined) {
      pcm.productsKey = productsKey.get
    } else { // If no feature can be a products key, then we create a new feature
      val arbitraryProductsKey = new Feature
      arbitraryProductsKey.name = "Products"

      for ((product, index) <- pcm.sortedProducts().zipWithIndex) {

        // Clear position because we will modify the product and thus its hashcode
        val position = pcm.productPositions(product)
        pcm.productPositions = pcm.productPositions - product

        // Create cell for the products key
        val cell = new Cell
        cell.content = "P" + index
        cell.rawContent = cell.content
        cell.feature = arbitraryProductsKey

        product.cells += cell

        // Restore position
        pcm.productPositions += (product -> position)

      }

      pcm.featurePositions = pcm.featurePositions.map(e => e._1 -> (e._2 + 1))
      pcm.featurePositions += arbitraryProductsKey -> 0

      pcm.features += arbitraryProductsKey
      pcm.productsKey = arbitraryProductsKey

    }


  }

}
