package org.diverse.pcm.formalizer.extractor

import pcmmm.PCM
import scala.collection.JavaConversions._
import pcmmm.Header
import pcmmm.PcmmmFactory
import pcmmm.Cell
import pcmmm.Extra
import pcmmm.Matrix
import java.util.ListIterator
import org.eclipse.emf.common.util.EList
import org.diverse.pcm.formalizer.configuration.PCMConfiguration
import collection.mutable.Map

class PCMNormalizer {
  
  /**
   * Normalize a PCM according to the given configuration
   * - set an ID to each matrix
   * - remove ignored matrices
   * - add missing cells
   * - set headers
   * - convert cells of ignored rows or columns to Extra cell
   */
  def normalizePCM(pcm : PCM, config : PCMConfiguration) {
	  val nextIndexes : Map[String,Int] = Map.empty
    
	  val it = pcm.getMatrices().listIterator()
	  while (it.hasNext()) {
		  val matrix = it.next()
		  
  		  // Compute ID
		  val name = matrix.getName()

		  val index = nextIndexes.getOrElse(name, 0)
		  nextIndexes += name -> (index + 1)
		  
		  val id = name + "_" + index
		  matrix.setId(id)
		  
		  // Get config
		  val matrixConfig = config.getConfig(matrix)
		  
		  // Normalize
		  if (matrixConfig.ignored) {
			  it.remove()
		  } else {
			  normalizeMatrix(matrix)
			  setHeaders(matrix, matrixConfig.headerRows, matrixConfig.headerColumns)
			  ignoreLinesAndColumns(matrix, matrixConfig.ignoreRows, matrixConfig.ignoreColumns)
		  }
	  }
  }
  
  /**
   * Add missing cells to create a rectangular matrix and duplicate cell with row/colspan
   */
  def normalizeMatrix(matrix : Matrix) {
    // Duplicate cells with rowspan or colspan
    val newCells = for (cell <- matrix.getCells()) yield {
      
      val duplicatedCells = for (rowShift <- 0 until cell.getRowspan(); 
      columnShift <- 0 until cell.getColspan() 
      if (rowShift != 0 || columnShift != 0) ) yield {
    	  val row = cell.getRow() + rowShift
    	  val column = cell.getColumn() + columnShift
        
    	  val duplicatedCell = PcmmmFactory.eINSTANCE.createExtra() // FIXME : should copy with same type
	      duplicatedCell.setName(cell.getName() + " d(" + row + "," + column + ")")
	      duplicatedCell.setVerbatim(cell.getVerbatim())
	      duplicatedCell.setRow(row)
	      duplicatedCell.setColumn(column)
	      duplicatedCell.setRowspan(1)
	      duplicatedCell.setColspan(1)
	      duplicatedCell.setColspan(1)
	      duplicatedCell
      }
      
      
      cell.setRowspan(1)
      cell.setColspan(1)
      duplicatedCells.toList
    }
    
    matrix.getCells().addAll(newCells.reduceLeft(_ ::: _))
    
    // Get all existing cell positions
    val cellPositions = matrix.getCells().flatMap( c =>
      		for (row <- c.getRow until c.getRow + c.getRowspan;
      				column <- c.getColumn until c.getColumn + c.getColspan) yield {
      			(row, column)
      		} 
        ).toList
        
    val maxRow = cellPositions.maxBy(p => p._1)._1
    val maxColumn = cellPositions.maxBy(p => p._2)._2

    // Detect holes in the matrix and add a cell if necessary 
    for (row <- 0 to maxRow; column <- 0 to maxColumn) {
      if (!cellPositions.contains((row, column))) {
    	  val newCell = PcmmmFactory.eINSTANCE.createExtra()
    	  newCell.setName("")
    	  newCell.setVerbatim("")
    	  newCell.setRow(row)
    	  newCell.setRowspan(1)
    	  newCell.setColumn(column)
    	  newCell.setColspan(1)
    	  matrix.getCells().add(newCell)
      }
    }
    
  }
  
  /**
   * Define headers in a matrix
   * @param matrix 
   * @param numberOfRows : number of top rows that are headers
   * @param numberOfColumns : number of left columns that are headers
   */
  def setHeaders(matrix : Matrix, numberOfRows : Int = 1, numberOfColumns : Int = 1) {
    val cells = matrix.getCells()
    val sortedCells = cells.sortBy(c => (c.getRow(), c.getColumn())).toList
  
    for(cell <- sortedCells) {
      val row = cell.getRow()
      val column = cell.getColumn()

      // TODO : handle row/colspan 
      // TODO : handle product and feature clusters
      if (row < numberOfRows && column < numberOfColumns) { // Top left header
        val newCell = PcmmmFactory.eINSTANCE.createExtra
        copyAndReplaceCell(matrix, cell, newCell)
      } else if (row < numberOfRows) { // Top header
        if (!cell.isInstanceOf[Header]) {
	        val newCell = PcmmmFactory.eINSTANCE.createHeader
	        copyAndReplaceCell(matrix, cell, newCell)
        }
      } else if (column < numberOfColumns) { // Left header
        if (!cell.isInstanceOf[Header]) {
        	val newCell = PcmmmFactory.eINSTANCE.createHeader
        	copyAndReplaceCell(matrix, cell, newCell)
        }
      } else { // Inner cell
        val newCell = PcmmmFactory.eINSTANCE.createValuedCell
        copyAndReplaceCell(matrix, cell, newCell)
      }
      
      
      
    }
  }
  
  def ignoreLinesAndColumns(matrix : Matrix, rows : List[Int], columns : List[Int]) {
	  val it = matrix.getCells().listIterator()
	  while(it.hasNext()) {
	    val cell = it.next()
	    if (rows.contains(cell.getRow()) || columns.contains(cell.getColumn())) {
	    	it.remove()
	    	val newCell = PcmmmFactory.eINSTANCE.createExtra()
	    	copyCell(cell, newCell)
	    	it.add(newCell)
	    }
	  }
  }
  
  
  /**
   * Copy the content of a cell to another one
   */
  def copyCell(cell : Cell, newCell : Cell) {
    newCell.setName(cell.getName())
    newCell.setVerbatim(cell.getVerbatim())
    newCell.setRow(cell.getRow())
    newCell.setRowspan(cell.getRowspan())
    newCell.setColumn(cell.getColumn())
    newCell.setColspan(cell.getColspan())
  }

  /**
   * Copy the content of a cell to another one and replace it in the matrix
   * @param matrix : matrix containing the old cell
   * @param cell
   * @param newCell 
   */
  def copyAndReplaceCell(matrix : Matrix, cell : Cell, newCell : Cell) {
    copyCell(cell, newCell)
    val cellToRemove = matrix.getCells().find(c => 
      (c.getRow() == cell.getRow()) && (c.getColumn() == cell.getColumn()))
    matrix.getCells().remove(cellToRemove.get)  
    matrix.getCells().add(newCell)
  }
  
  
}