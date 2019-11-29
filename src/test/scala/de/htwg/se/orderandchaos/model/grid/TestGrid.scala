package de.htwg.se.orderandchaos.model.grid

import de.htwg.se.orderandchaos.model.cell.Cell

import scala.util.{Success, Try}

class TestGrid(var cell: Cell = Cell.blue, var vector: Vector[Cell] = Vector.empty, vector2: Vector[Vector[Cell]] = Vector.empty) extends Grid {
  var mapCalls = 0
  var forEachCalls = 0
  var existsCalls = 0
  var applyCalls = 0
  var setCalls = 0
  var getRowCalls = 0
  var getColumnCalls = 0
  var getUpDiagonalCalls = 0
  var getDownDiagonalCalls = 0
  var getRowsCalls = 0
  var getColumnsCalls = 0
  var getUpDiagonalsCalls = 0
  var getDownDiagonalsCalls = 0
  var makeStringCalls = 0

  override def mapEachCell(f: Cell => Cell): Grid = {
    mapCalls += 1
    f(cell)
    this
  }

  override def forEachCell(f: Cell => Unit): Unit = {
    f(cell)
    forEachCalls += 1
  }

  override def exists(f: Cell => Boolean): Boolean = {
    existsCalls += 1
    f(cell)
  }

  override def apply(x: Int, y: Int): Cell = {
    applyCalls += 1
    cell
  }

  override def set(x: Int, y: Int, fieldType: String): Try[Grid] = {
    setCalls += 1
    Success(this)
  }

  override def getRow(y: Int): Try[Vector[Cell]] = {
    getRowCalls += 1
    Success(vector)
  }

  override def getColumn(x: Int): Try[Vector[Cell]] = {
    getColumnCalls += 1
    Success(vector)
  }

  override def getUpDiagonal(xStart: Int, yStart: Int): Try[Vector[Cell]] = {
    getUpDiagonalCalls += 1
    Success(vector)
  }

  override def getDownDiagonal(xStart: Int, yStart: Int): Try[Vector[Cell]] = {
    getDownDiagonalCalls += 1
    Success(vector)
  }

  override def getRows: Try[Vector[Vector[Cell]]] = {
    getRowsCalls += 1
    Success(vector2)
  }

  override def getColumns: Try[Vector[Vector[Cell]]] = {
    getColumnsCalls += 1
    Success(vector2)
  }

  override def getUpDiagonals: Try[Vector[Vector[Cell]]] = {
    getUpDiagonalsCalls += 1
    Success(vector2)
  }

  override def getDownDiagonals: Try[Vector[Vector[Cell]]] = {
    getDownDiagonalsCalls += 1
    Success(vector2)
  }

  override def toString: String = TestGrid.STRING_REPRESENTATION

  override def makeString(cellToString: Cell => String): String = {
    makeStringCalls += 1
    cellToString(cell)
  }
}

object TestGrid {
  val STRING_REPRESENTATION = "TestGrid"
}