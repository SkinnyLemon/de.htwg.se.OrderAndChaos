package de.htwg.se.orderandchaos.model

class TestGrid(var field: Cell = Cell.empty, var vector: Vector[Cell] = Vector.empty, vector2: Vector[Vector[Cell]] = Vector.empty) extends Grid {
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

  override def mapEachField(f: Cell => Cell): Grid = {
    mapCalls += 1
    f(field)
    this
  }

  override def forEachField(f: Cell => Unit): Unit = {
    f(field)
    forEachCalls += 1
  }

  override def exists(f: Cell => Boolean): Boolean = {
    existsCalls += 1
    f(field)
  }

  override def apply(x: Int, y: Int): Cell = {
    applyCalls += 1
    field
  }

  override def set(x: Int, y: Int, fieldType: String): Grid = {
    setCalls += 1
    this
  }

  override def getRow(y: Int): Vector[Cell] = {
    getRowCalls += 1
    vector
  }

  override def getColumn(x: Int): Vector[Cell] = {
    getColumnCalls += 1
    vector
  }

  override def getUpDiagonal(xStart: Int, yStart: Int): Vector[Cell] = {
    getUpDiagonalCalls += 1
    vector
  }

  override def getDownDiagonal(xStart: Int, yStart: Int): Vector[Cell] = {
    getDownDiagonalCalls += 1
    vector
  }

  override def getRows: Vector[Vector[Cell]] = {
    getRowsCalls += 1
    vector2
  }

  override def getColumns: Vector[Vector[Cell]] = {
    getColumnsCalls += 1
    vector2
  }

  override def getUpDiagonals: Vector[Vector[Cell]] = {
    getUpDiagonalsCalls += 1
    vector2
  }

  override def getDownDiagonals: Vector[Vector[Cell]] = {
    getDownDiagonalsCalls += 1
    vector2
  }

  override def toString: String = TestGrid.STRING_REPRESENTATION
}

object TestGrid {
  val STRING_REPRESENTATION = "TestGrid"
}