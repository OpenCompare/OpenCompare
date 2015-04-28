//package org.opencompare.formalizer.export
//
//import pcmmm.PCM
//import scala.xml.Elem
//import scala.collection.JavaConversions._
//import pcmmm.Matrix
//import pcmmm.Cell
//import pcmmm.Header
//import pcmmm.Extra
//import pcmmm.ValuedCell
//import pcmmm.Constraint
//import pcmmm.Boolean
//import pcmmm.Unknown
//import pcmmm.Empty
//import pcmmm.Inconsistent
//import pcmmm.Simple
//import pcmmm.Partial
//import pcmmm.Multiple
//import pcmmm.And
//import pcmmm.XOr
//import pcmmm.Or
//import pcmmm.Integer
//import pcmmm.Double
//import pcmmm.VariabilityConceptRef
//
//class PCM2HTML {
//
//  def pcm2HTML(pcm : PCM) : Elem = {
//    val htmlCode =
//    <html>
//    <head>
//    		<meta charset="utf-8"/>
//    </head>
//    <body>
//    	{ for(matrix <- pcm.getMatrices()) yield matrix2HTML(matrix)}
//    </body>
//    </html>
//
//    htmlCode
//  }
//
//  def matrix2HTML(matrix : Matrix) : Elem = {
//    val cells = matrix.getCells()
////    val sortedCells = cells.sortWith((c1,c2) => (
////        c1.getRow() < c2.getRow())
////        || ((c1.getRow() == c2.getRow()) && (c1.getColumn() < c2.getColumn()))
////        )
//    val rows = cells.groupBy(cell => cell.getRow()).toList
//    val sortedRows = rows.sortBy(r => r._1).map(r => r._2)
//
//    val htmlCode =
//    <div>
//    	<h1>{matrix.getName}</h1>
//    	<table border="1">
//			{for (row <- sortedRows) yield {
//				<tr>
//				{for (cell <- row.sortBy(c => c.getColumn())) yield {
//					cell2HTML(cell)
//				}}
//				</tr>
//    			}}
//    	</table>
//    </div>
//    htmlCode
//  }
//
//  def cell2HTML(cell : Cell) : Elem = {
//    val htmlCode =
//    <td
//    	rowspan={cell.getRowspan().toString}
//    	colspan={cell.getColspan().toString}
//    >
//    {
//      cell match {
//        case c : ValuedCell if Option(c.getInterpretation()).isDefined => interpretation2String(c.getInterpretation())
//        case c : ValuedCell => "ValuedCell(" + c.getVerbatim() + ")"
//        case c : Header => "Header(" + c.getVerbatim() + ")"
//        case c : Extra => "Extra(" + c.getVerbatim() + ")"
//      }
//    }
//    </td>
//    htmlCode
//  }
//
//  def interpretation2String(interpretation : Constraint) : String = {
//		  if (Option(interpretation).isDefined) {
//			interpretation match {
//		    case i : Boolean => "Boolean(" + i.getVerbatim() + ", " + i.isValue() + ")"
//		    case i : Integer => "Integer(" + i.getVerbatim() + ", " + i.getValue() + ")"
//		    case i : Double => "Double(" + i.getVerbatim() + ", " + i.getValue() + ")"
//		    case i : VariabilityConceptRef => "VarConceptRef(" + i.getVerbatim() + ", " + i.getConcept().getName() + ")"
//		    case i : Unknown => "Unknown(" + i.getVerbatim() + ")"
//		    case i : Empty => "Empty(" + i.getVerbatim() + ")"
//		    case i : Inconsistent => "Inconsistent(" + i.getVerbatim() + ")"
//		    case i : Simple => "Simple(" + i.getVerbatim() + ")"
//		    case i : Partial => "Partial(" + interpretation2String(i.getArgument()) + ", " +
//		    					 interpretation2String(i.getCondition()) + ")"
//		    case i : And => "And(" + multipleInterpretation2string(i) + ")"
//		    case i : XOr => "XOr(" + multipleInterpretation2string(i) + ")"
//		    case i : Or => "Or(" + multipleInterpretation2string(i) + ")"
//		    case i : Multiple => "Multiple(" + multipleInterpretation2string(i) + ")"
//			}
//		  } else {
//		    ""
//		  }
//
//  }
//
//  def multipleInterpretation2string(interpretation : Multiple) : String = {
//		  (for (c <- interpretation.getContraints()) yield {interpretation2String(c)}).mkString(", ")
//  }
//}