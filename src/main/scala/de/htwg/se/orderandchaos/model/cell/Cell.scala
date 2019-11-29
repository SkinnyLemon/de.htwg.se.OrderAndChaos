package de.htwg.se.orderandchaos.model.cell

import de.htwg.se.orderandchaos.model.{IllegalOverrideException, InvalidCellTypeException}

import scala.util.{Failure, Success, Try}

abstract case class Cell(cellType: String) {
  def setType(fieldType: String): Try[Cell]

  def isSet: Boolean

  def isBlue: Boolean = cellType equals Cell.TYPE_BLUE

  def isRed: Boolean = cellType equals Cell.TYPE_RED

  def isEmpty: Boolean = cellType equals Cell.TYPE_EMPTY

  override def toString: String = cellType
}

private abstract class CellImpl(override val cellType: String) extends Cell(cellType) {
  if ((cellType != Cell.TYPE_EMPTY) && (cellType != Cell.TYPE_BLUE) && (cellType != Cell.TYPE_RED)) {
    throw new IllegalStateException("Invalid Cell type was set")
  }
}

private class SetCell(override val cellType: String) extends CellImpl(cellType) {
  override def setType(cellType: String): Try[Cell] = Failure(new IllegalOverrideException)

  override def isSet: Boolean = true
}

private class EmptyCell extends CellImpl(Cell.TYPE_EMPTY) {
  override def setType(cellType: String): Try[Cell] =
    if (cellType != Cell.TYPE_BLUE && cellType != Cell.TYPE_RED)
      Failure(new InvalidCellTypeException)
    else
      Success(new SetCell(cellType))

  override def isSet: Boolean = false
}

object Cell {
  val TYPE_EMPTY = "E"
  val TYPE_BLUE = "B"
  val TYPE_RED = "R"
  val empty: Cell = new EmptyCell
  val blue: Cell = new SetCell(TYPE_BLUE)
  val red: Cell = new SetCell(TYPE_RED)
  val validSetTypes: Array[String] = Array(TYPE_BLUE, TYPE_RED)
  val validTypes: Array[String] = Array(TYPE_BLUE, TYPE_RED, TYPE_EMPTY)

  def ofType(cellType: String): Try[Cell] = cellType match {
    case TYPE_EMPTY => Success(new EmptyCell)
    case TYPE_BLUE => Success(new SetCell(TYPE_BLUE))
    case TYPE_RED => Success(new SetCell(TYPE_RED))
    case _ => Failure(new InvalidCellTypeException)
  }
}
