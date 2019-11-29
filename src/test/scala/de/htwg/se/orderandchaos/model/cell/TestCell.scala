package de.htwg.se.orderandchaos.model.cell

import scala.util.{Success, Try}

class TestCell(cellType: String = TestCell.STANDARD_TYPE, set: Boolean = true) extends Cell(cellType) {
  override def setType(cellType: String): Try[Cell] = Success(new TestCell(cellType))

  override def isSet: Boolean = set
}

object TestCell {
  val STANDARD_TYPE = "T"
}
