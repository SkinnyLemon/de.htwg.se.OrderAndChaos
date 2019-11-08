package de.htwg.se.orderandchaos.model

class TestCell(cellType: String = TestCell.STANDARD_TYPE, set: Boolean = true) extends Cell(cellType) {
  override def setType(cellType: String): Cell = new TestCell(cellType)

  override def isSet: Boolean = set

  override val coloredType: String = cellType
}

object TestCell {
  val STANDARD_TYPE = "T"
}
