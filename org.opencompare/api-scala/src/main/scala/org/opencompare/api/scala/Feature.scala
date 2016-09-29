package org.opencompare.api.scala

class Feature extends AbstractFeature {

  private var _cells : Set[Cell] = Set.empty[Cell]

  def cells : Set[Cell] = _cells
  def cells_= (value : Set[Cell]) : Unit = {
    _cells = value
    for (cell <- _cells if cell.feature != this) { // Update other side of the association
      if (Option(cell.feature).isDefined) {
        cell.feature.cells = cell.feature.cells - cell
      }
      cell.feature = this
    }
  }


  def canEqual(other: Any): Boolean = other.isInstanceOf[Feature]

  override def equals(other: Any): Boolean = other match {
    case that: Feature =>
      (that canEqual this) &&
        name == that.name &&
        parent == that.parent
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, parent)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"Feature($name)"



}
