package org.opencompare.api.scala

class Cell {

  var _feature : Feature = _
  var product : Product = _
  var content : String = ""
  var rawContent : String = ""
  var interpretation : Option[Value] = None


  def feature : Feature = _feature
  def feature_= (value: Feature): Unit = {
    _feature = value
    _feature.cells += this
  }

  override def toString = s"Cell($content, $rawContent, $interpretation)"


  def canEqual(other: Any): Boolean = other.isInstanceOf[Cell]




  override def equals(other: Any): Boolean = other match { // TODO : interpretation
    case that: Cell =>
      (that canEqual this) &&
        _feature == that._feature &&
        content == that.content &&
        rawContent == that.rawContent
    case _ => false
  }

  override def hashCode(): Int = { // TODO : interpretation
    val state = Seq(_feature, content, rawContent)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
