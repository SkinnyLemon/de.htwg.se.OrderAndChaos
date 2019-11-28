package de.htwg.se.orderandchaos.model.cell

class TestCell(cellType: String = TestCell.STANDARD_TYPE, set: Boolean = true) extends Cell(cellType) {
  override def setType(cellType: String): Cell = new TestCell(cellType)

  override def isSet: Boolean = set
}

object TestCell {
  val STANDARD_TYPE = "T"
}
