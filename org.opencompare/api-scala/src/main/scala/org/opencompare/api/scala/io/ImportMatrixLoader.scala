package org.opencompare.api.scala.io

import org.opencompare.api.scala._
import org.opencompare.api.scala.interpreter.CellContentInterpreter
import org.opencompare.api.scala.metadata._

import scala.collection.mutable.ListBuffer

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
    pcm.features = topFeatures

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


  class IONode(var content : String, var children : Set[IONode] = Set.empty[IONode], var positions : Set[Int] = Set.empty[Int]) {
    def isLeaf() : Boolean = children.isEmpty
    def leaves() : Set[IONode] = if (isLeaf()) {
      Set(this)
    } else {
      children.flatMap(_.leaves())
    }

    override def toString = s"IONode($content, $children)"
  }

  /**
    * Detect features from the information contained in the matrix
    * @param matrix matrix
    * @return graph representing the hierarchy of features
    */
  protected def detectFeatures(matrix : ImportMatrix) : IONode = {

    val root = new IONode("")

    // Init parents
    var parents = (0 until matrix.numberOfColumns()).map(_ => root).toList

    // Create hierarchy of features
    for (row <- 0 until matrix.numberOfRows()) {

      val nextParents = ListBuffer.empty[IONode]

      for ((parent, column) <- parents.zipWithIndex) yield {
        val cell = matrix.getCell(row, column).getOrElse(new ImportCell())
        val previousCell = if (column > 0) {
          Some(matrix.getCell(row, column - 1).getOrElse(new ImportCell()))
        } else {
          None
        }

        if (cell.content == parent.content) { // Same feature as the one above
          parent.positions += column
          nextParents += parent
        } else if (previousCell.isDefined && cell.content == previousCell.get.content) { // Same feature as the one on the left
          val previousCellParent = nextParents.last
          previousCellParent.positions += column
          nextParents += previousCellParent
        } else { // New feature
          val newParent = new IONode(cell.content)
          parent.children = parent.children + newParent
          newParent.positions += column
          nextParents += newParent
        }
      }

      parents = nextParents.toList

      if (root.leaves().size == matrix.numberOfColumns()) {
        return root
      }
    }

    root
  }

  def createFeatures(parent : IONode) : (Set[AbstractFeature], Map[Int, Feature]) = {

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
        featureGroup.subFeatures = subFeatures.toSet
        (List(featureGroup), positionToFeature)
      }
    }

    val unzippedResult = result.unzip
    val features = unzippedResult._1.flatten
    val featureToPosition = unzippedResult._2.flatten.toMap

    (features, featureToPosition)
  }

  /**
    * Detect and create products from the information contained in the matrix and the provided direction
    * @param matrix matrix
    * @param pcm PCM
    * @param positionToFeature map between positions and features
    */
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
