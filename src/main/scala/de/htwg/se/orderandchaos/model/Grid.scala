package de.htwg.se.orderandchaos.model

import scala.annotation.tailrec

trait Grid {
  def mapEachCell(f: Cell => Cell): Grid

  def forEachCell(f: Cell => Unit): Unit

  def exists(f: Cell => Boolean): Boolean

  def apply(x: Int, y: Int): Cell

  def set(x: Int, y: Int, fieldType: String): Grid

  def getRow(y: Int): Vector[Cell]

  def getColumn(x: Int): Vector[Cell]

  def getUpDiagonal(xStart: Int, yStart: Int): Vector[Cell]

  def getDownDiagonal(xStart: Int, yStart: Int): Vector[Cell]

  def getRows: Vector[Vector[Cell]]

  def getColumns: Vector[Vector[Cell]]

  def getUpDiagonals: Vector[Vector[Cell]]

  def getDownDiagonals: Vector[Vector[Cell]]

  def makeString(cellToString: Cell => String): String
}

private class GridImpl(cells: Vector[Vector[Cell]]) extends Grid {

  override def mapEachCell(f: Cell => Cell): Grid = new GridImpl(cells.map(row => row.map(f)))

  override def forEachCell(f: Cell => Unit): Unit = cells.foreach(row => row.foreach(f))

  override def exists(f: Cell => Boolean): Boolean = cells.exists(row => row.exists(f))

  override def apply(x: Int, y: Int): Cell = cells(y)(x)

  override def set(x: Int, y: Int, fieldType: String): Grid = new GridImpl(
    cells.updated(y,
      cells(y).updated(x,
        cells(y)(x).setType(fieldType))))

  override def toString: String = makeString(_.toString)

  def makeString(cellToString: Cell => String): String =
    cells.map(row => row.map(cellToString).mkString(" "))
      .reverse.mkString("\n")

  override def getRow(y: Int): Vector[Cell] = cells(y)

  override def getColumn(x: Int): Vector[Cell] = cells.map(row => row(x))

  override def getUpDiagonal(xStart: Int, yStart: Int): Vector[Cell] = {
    if (xStart > 0 && yStart > 0) {
      throw new IllegalArgumentException("parameters need to be on a starting axis")
    }
    getDiagonal(xStart, yStart, 1)
  }

  override def getDownDiagonal(xStart: Int, yStart: Int): Vector[Cell] = {
    if (xStart > 0 && yStart != Grid.WIDTH - 1) {
      throw new IllegalArgumentException("parameters need to be on a starting axis")
    }
    getDiagonal(xStart, yStart, -1)
  }

  override def getRows: Vector[Vector[Cell]] = cells

  override def getColumns: Vector[Vector[Cell]] = Vector.empty ++ (for (x <- 0 until Grid.WIDTH) yield getColumn(x))

  override def getUpDiagonals: Vector[Vector[Cell]] = Vector.empty ++
    (for (x <- 0 until Grid.WIDTH) yield getUpDiagonal(x, 0)) ++
    (for (y <- 1 until Grid.WIDTH) yield getUpDiagonal(0, y))

  override def getDownDiagonals: Vector[Vector[Cell]] = Vector.empty ++
    (for (x <- 0 until Grid.WIDTH) yield getDownDiagonal(x, Grid.WIDTH - 1)) ++
    (for (y <- 1 until Grid.WIDTH) yield getDownDiagonal(0, y))

  private final def getDiagonal(xStart: Int, yStart: Int, deltaY: Int): Vector[Cell] = {
    if (xStart < 0 || xStart >= Grid.WIDTH || yStart < 0 || yStart >= Grid.WIDTH) {
      throw new IllegalArgumentException("parameters can't be negative or greater than / equal to the width")
    }
    @tailrec
    def getDiagonalPartRec(x: Int, y: Int, fields: Vector[Cell]): Vector[Cell] = {
      if (x >= Grid.WIDTH || y >= Grid.WIDTH || x < 0 || y < 0) {
        fields
      } else {
        getDiagonalPartRec(x + 1, y + deltaY, fields :+ this (x, y))
      }
    }

    getDiagonalPartRec(xStart, yStart, Vector.empty)
  }
}

object Grid {
  val WIDTH = 6

  def empty: Grid = new GridImpl(Vector.fill(WIDTH, WIDTH)(Cell.empty))

  def fill(cell: Cell): Grid = new GridImpl(Vector.fill(WIDTH, WIDTH)(cell))
}
