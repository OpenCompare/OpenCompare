package org.opencompare.io.wikipedia.pcm

import org.opencompare.api.java.PCM

import scala.xml.Elem
import scala.xml.Text

class Matrix {

  var name : String = ""
  var cells : Map[(Int, Int), Cell] = Map()
  
  def setCell(cell : Cell, row : Int, column : Int) {
    cells += ((row, column) -> cell)
  }
  
  def getCell(row : Int, column : Int) : Option[Cell] = {
    cells get (row, column)
  }
  
  def getNumberOfRows() : Int = {
    if (!cells.isEmpty) {
    	cells.keys.maxBy(_._1)._1 + 1
    } else {
      0
    }
  }
  
  def getNumberOfColumns() : Int = {
    if (!cells.isEmpty) {
    	cells.keys.maxBy(_._2)._2 + 1
    } else {
      0
    }
  }
  
  override def toString() : String = {
    val result = new StringBuilder
    for (row <- 0 until getNumberOfRows; column <- 0 until getNumberOfColumns) {
      result ++= row + "," + column + ":"
      
      val cell = cells get (row, column)
      if (cell.isDefined) {
        result ++= cell.get.content 
      } else {
        result ++= "/!\\ This cell is not defined /!\\"
      }
      result += '\n'
    }
    result.toString
  }
  
  def toHTML() : Elem = {
    val htmlCode = 
    <div>
    <h1>{name}</h1>
    <table border="1">
    { 
    	for {row <- 0 until getNumberOfRows} 
	    yield <tr> 
      	  {
		    for {column <- 0 until getNumberOfColumns} 
		  	yield <td>
		  	{
		  	  val cell = cells.get((row, column))
		  	  if (cell.isDefined) {
		  	    // Convert new lines in <br/>
		  	    val lines = cell.get.content.split("\n")
		  	    var firstLine = true
		  	    for (line <- lines) yield {
		  	      if (firstLine) {
		  	        firstLine = false
		  	        Text(line)
		  	      } else {
		  	        <br/> ++ Text(line)
		  	      }
		  	    }
		  	  } else {
		  	    "/!\\ Not defined /!\\"
		  	  }
		  	}
		  	</td>
		  }
    	</tr>
	} 
    </table>
    </div>
    
    htmlCode
  }

  def comparePosition(c1 : Cell, c2 : Cell) : Boolean = {
    (c1.row < c2.row) || 
    (c1.row == c2.row && c1.column < c2.column)
  }
  
  def toCSV() : String = {
    val result = new StringBuilder
    for (row <- 0 until getNumberOfRows) {
      for (column <- 0 until getNumberOfColumns) {
    	  val cell = cells get (row, column)
    	  if (cell.isDefined) {
    	    val content = cell.get.content
    	    val formattedContent = '"' + content.replaceAll("\"", "\"\"") + '"'
    		result ++= formattedContent 
    	  } else {
    		result ++= "\"/!\\ This cell is not defined /!\\\""
    	  }
    	  if (column < getNumberOfColumns - 1) {
    	    result += ','
    	  }
      }
      
      if (row < getNumberOfRows - 1) {
    	  result += '\n'  
      }
      
    }
    result.toString
  }
  
  def toPCM(pcm : PCM) {
    val nbFeatureLines = 1
    val nbProductColumns = 1


    // TODO : set first line (+ followings if rowspan) as features
    // TODO : set first column (+ followings if colspan) as products

  }
}