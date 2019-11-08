package de.htwg.se.orderandchaos.model

import io.AnsiColor.{BLUE, RED, RESET}

abstract case class Cell(cellType: String) {
  val coloredType: String
  def setType(fieldType: String): Cell
  def isSet: Boolean
  def isBlue: Boolean = cellType equals Cell.TYPE_BLUE
  def isRed: Boolean = cellType equals Cell.TYPE_RED
  def isEmpty: Boolean = cellType equals Cell.TYPE_EMPTY
  override def toString: String = coloredType
}

private abstract class CellImpl(override val cellType: String) extends Cell(cellType) {
  override val coloredType: String = if (cellType == Cell.TYPE_EMPTY) {
    Cell.TYPE_EMPTY
  }
  else if (cellType == Cell.TYPE_BLUE) {
    s"$BLUE${Cell.TYPE_BLUE}$RESET"
  }
  else if (cellType == Cell.TYPE_RED) {
    s"$RED${Cell.TYPE_RED}$RESET"
  } else {
    throw new IllegalStateException("Invalid field type")
  }
}

private class SetCell(override val cellType: String) extends CellImpl(cellType) {
  override def setType(cellType: String): Cell = throw new IllegalOverrideException

  override def isSet: Boolean = true
}

private class EmptyCell extends CellImpl(Cell.TYPE_EMPTY) {
  override def setType(cellType: String): Cell = {
    if (cellType != Cell.TYPE_BLUE && cellType != Cell.TYPE_RED) {
      throw new IllegalArgumentException("Field needs to be set to RED or BLUE")
    }
    new SetCell(cellType)
  }

  override def isSet: Boolean = false
}

object Cell {
  val TYPE_EMPTY = "E"
  val TYPE_BLUE = "B"
  val TYPE_RED = "R"
  def empty: Cell = new EmptyCell
  def blue: Cell = new SetCell(TYPE_BLUE)
  def red: Cell = new SetCell(TYPE_RED)
}
