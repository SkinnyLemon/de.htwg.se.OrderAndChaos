package de.htwg.se.orderandchaos.model.grid

import de.htwg.se.orderandchaos.model.cell.Cell

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

trait Grid {
  def mapEachCell(f: Cell => Cell): Grid

  def forEachCell(f: Cell => Unit): Unit

  def exists(f: Cell => Boolean): Boolean

  def apply(x: Int, y: Int): Cell

  def set(x: Int, y: Int, fieldType: String): Try[Grid]

  def getRow(y: Int): Try[Vector[Cell]]

  def getColumn(x: Int): Try[Vector[Cell]]

  def getUpDiagonal(xStart: Int, yStart: Int): Try[Vector[Cell]]

  def getDownDiagonal(xStart: Int, yStart: Int): Try[Vector[Cell]]

  def getRows: Try[Vector[Vector[Cell]]]

  def getColumns: Try[Vector[Vector[Cell]]]

  def getUpDiagonals: Try[Vector[Vector[Cell]]]

  def getDownDiagonals: Try[Vector[Vector[Cell]]]

  def makeString(cellToString: Cell => String): String
}

private class GridImpl(cells: Vector[Vector[Cell]]) extends Grid {

  override def mapEachCell(f: Cell => Cell): Grid = new GridImpl(cells.map(_.map(f)))

  override def forEachCell(f: Cell => Unit): Unit = cells.foreach(_.foreach(f))

  override def exists(f: Cell => Boolean): Boolean = cells.exists(_.exists(f))

  override def apply(x: Int, y: Int): Cell = cells(y)(x)

  override def set(x: Int, y: Int, fieldType: String): Try[Grid] = cells(y)(x).setType(fieldType) match {
    case Success(newCell) => Success(new GridImpl(cells.updated(y, cells(y).updated(x, newCell))))
    case Failure(e) => Failure(e)
  }

  override def toString: String = makeString(_.toString)

  def makeString(cellToString: Cell => String): String =
    cells.map(row => row.map(cellToString).mkString(" "))
      .reverse.mkString("\n")

  override def getRow(y: Int): Try[Vector[Cell]] = Success(cells(y))

  override def getColumn(x: Int): Try[Vector[Cell]] = Success(cells.map(row => row(x)))

  override def getUpDiagonal(xStart: Int, yStart: Int): Try[Vector[Cell]] =
    if (xStart > 0 && yStart > 0)
      Failure(new IllegalArgumentException("parameters need to be on a starting axis"))
    else
      getDiagonal(xStart, yStart, 1)

  override def getDownDiagonal(xStart: Int, yStart: Int): Try[Vector[Cell]] =
    if (xStart > 0 && yStart != Grid.WIDTH - 1)
      Failure(new IllegalArgumentException("parameters need to be on a starting axis"))
    else
      getDiagonal(xStart, yStart, -1)

  private final def getDiagonal(xStart: Int, yStart: Int, deltaY: Int): Try[Vector[Cell]] = {
    @tailrec
    def getDiagonalPart(x: Int, y: Int, fields: Vector[Cell]): Vector[Cell] = {
      if (x >= Grid.WIDTH || y >= Grid.WIDTH || x < 0 || y < 0) {
        fields
      } else {
        getDiagonalPart(x + 1, y + deltaY, fields :+ this (x, y))
      }
    }

    if (xStart < 0 || xStart >= Grid.WIDTH || yStart < 0 || yStart >= Grid.WIDTH)
      Failure(new IllegalArgumentException("parameters can't be negative or greater than / equal to the width"))
    else
      Success(getDiagonalPart(xStart, yStart, Vector.empty))
  }

  override def getRows: Try[Vector[Vector[Cell]]] = Success(cells)

  override def getColumns: Try[Vector[Vector[Cell]]] = getLinesOverVal(0, x => getColumn(x))

  override def getUpDiagonals: Try[Vector[Vector[Cell]]] = getDiagonals(
    x => getUpDiagonal(x, 0),
    y => getUpDiagonal(0, y))

  override def getDownDiagonals: Try[Vector[Vector[Cell]]] = getDiagonals(
    x => getDownDiagonal(x, Grid.WIDTH - 1),
    y => getDownDiagonal(0, y)
  )

  private def getDiagonals(buildDiagonalX: Int => Try[Vector[Cell]], buildDiagonalY: Int => Try[Vector[Cell]]): Try[Vector[Vector[Cell]]] = {
    getLinesOverVal(0, buildDiagonalX) match {
      case Success(xDiagonals) => getLinesOverVal(1, buildDiagonalY) match {
        case Success(yDiagonals) => Success(xDiagonals ++ yDiagonals)
        case Failure(e) => Failure(e)
      }
      case Failure(e) => Failure(e)
    }
  }

  private def getLinesOverVal(start: Int, method: Int => Try[Vector[Cell]]): Try[Vector[Vector[Cell]]] = {
    @tailrec
    def getLinesOverValRec(value: Int, sum: Vector[Vector[Cell]] = Vector.empty): Try[Vector[Vector[Cell]]] =
      if (value < Grid.WIDTH) {
        method(value) match {
          case Success(diagonal) => getLinesOverValRec(value + 1, sum :+ diagonal)
          case Failure(e) => Failure(e)
        }
      } else {
        Success(sum)
      }

    getLinesOverValRec(start)
  }
}

object Grid {
  val WIDTH = 6

  def empty: Grid = new GridImpl(Vector.fill(WIDTH, WIDTH)(Cell.empty))

  def fill(cell: Cell): Grid = new GridImpl(Vector.fill(WIDTH, WIDTH)(cell))

  def fromSeq(cells: Seq[Seq[Cell]]): Grid = new GridImpl(cells.map(_.toVector).toVector)
}
