package pcm


class Cell(
  val content : String,
  val isHeader : Boolean,
  val row : Int,
  val rowspan : Int,
  val column : Int,
  val colspan : Int
  ) {
  
  override def toString() : String = {
     content
  }
  
}