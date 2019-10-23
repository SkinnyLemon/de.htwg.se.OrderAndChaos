package de.htwg.se.orderandchaos.model

import scala.annotation.tailrec

class Grid private(fields: Vector[Vector[Field]]) {
  override def toString: String = fields.map(row => row.mkString(", ")).reverse.mkString("\n")

  def mapEachField(f: Field => Field): Grid = new Grid(fields.map(row => row.map(f)))

  def forEachField(f: Field => Unit): Unit = fields.foreach(row => row.foreach(f))

  def apply(x: Int, y: Int): Field = fields(y)(x)

  def set(x: Int, y: Int, fieldType: String): Grid = new Grid(
    fields.updated(y,
      fields(y).updated(x,
        fields(y)(x).setType(fieldType))))

  def getRow(y: Int): Vector[Field] = fields(y)

  def getColumn(x: Int): Vector[Field] = fields.map(row => row(x))

  def getUpDiagonal(xStart: Int, yStart: Int): Vector[Field] = {
    if (xStart < 0 || yStart < 0 || (xStart > 0 && yStart > 0)) {
      throw new IllegalArgumentException("parameters cant be negative and point needs to be on a starting axis")
    }
    getDiagonal(xStart, yStart, 1)
  }

  def getDownDiagonal(xStart: Int, yStart: Int): Vector[Field] = {
    if (xStart < 0 || yStart < 0 || (xStart > 0 && yStart != Grid.width - 1)) {
      throw new IllegalArgumentException("parameters cant be negative and point needs to be on a starting axis")
    }
    getDiagonal(xStart, yStart, -1)
  }

  def getRows: Vector[Vector[Field]] = fields

  def getColumns: Vector[Vector[Field]] = Vector.empty ++ (for (x <- 0 until Grid.width) yield getColumn(x))

  def getUpDiagonals: Vector[Vector[Field]] = Vector.empty ++
    (for (x <- 0 until Grid.width) yield getUpDiagonal(x, 0)) ++
    (for (y <- 0 until Grid.width) yield getUpDiagonal(0, y))

  def getDownDiagonals: Vector[Vector[Field]] = Vector.empty ++
    (for (x <- 0 until Grid.width) yield getDownDiagonal(x, Grid.width -1)) ++
    (for (y <- 0 until Grid.width) yield getDownDiagonal(0, y))

  private final def getDiagonal(xStart: Int, yStart: Int, deltaY: Int): Vector[Field] = {
    @tailrec
    def getDiagonalPartRec(x: Int, y: Int, fields: Vector[Field]): Vector[Field] = {
      if (x >= Grid.width || y >= Grid.width) {
        fields
      } else {
        getDiagonalPartRec(x + 1, y + deltaY, fields :+ this (x, y))
      }
    }
    getDiagonalPartRec(xStart, yStart, Vector.empty)
  }
}

object Grid {
  val width = 6

  def empty: Grid = new Grid(Vector.fill(width, width)(Field.empty))
}
