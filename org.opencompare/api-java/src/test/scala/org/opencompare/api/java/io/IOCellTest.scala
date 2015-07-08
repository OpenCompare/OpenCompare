package org.opencompare.api.java.io

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

/**
 * Created by smangin on 7/3/15.
 */
class IOCellTest extends FlatSpec with Matchers with BeforeAndAfterAll  {

  behavior of "IOCell"

  val content = "sdqkjpohsojgv"
  val row = 16
  val rowspan = 12
  var cell : IOCell = _
  var rawContent = "!il13256ih4i861u:1o6846h6ZDfzdz<>"
  var column = 18
  var colspan = 5

  override def beforeAll() = {
    cell = new IOCell(content, rawContent)
    cell.setRow(row)
    cell.setColumn(column)
    cell.setRowspan(rowspan)
    cell.setColspan(colspan)
  }
  "A Cell" should "have equal content with the reference cell" in{
    content.equals(cell.getContent) shouldBe true
  }

  it should "have equal raw content with the reference cell" in {
    rawContent.equals(cell.getRawContent) shouldBe true
  }

  it should "have equal row/column position with the reference cell" in {
    row.equals(cell.getRow) shouldBe true
    column.equals(cell.getColumn) shouldBe true
  }

  it should "have equal rowspan/colspan with the reference cell" in {
    rowspan.equals(cell.getRowspan) shouldBe true
    colspan.equals(cell.getColspan) shouldBe true
  }

  it should "be equal with the reference cell" in {
    val newCell = new IOCell(content, rawContent)
    newCell.setRow(row)
    newCell.setColumn(column)
    newCell.setRowspan(rowspan)
    newCell.setColspan(colspan)
    cell.equals(newCell) shouldBe true
  }

}
