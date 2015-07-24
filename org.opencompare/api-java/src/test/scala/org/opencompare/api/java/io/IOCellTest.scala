package org.opencompare.api.java.io

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

/**
 * Created by smangin on 7/3/15.
 */
class IOCellTest extends FlatSpec with Matchers with BeforeAndAfterAll  {

  val content = "sdqkjpohsojgv"
  var cell : IOCell = _

  override def beforeAll() = {
    cell = new IOCell(content)
  }
  "A Cell" should "have equal content with the reference cell" in{
    content.equals(cell.getContent) shouldBe true
  }

  it should "be equal with the reference cell" in {
    val newCell = new IOCell(content)
    cell.equals(newCell) shouldBe true
  }

}
