package de.htwg.se.orderandchaos.control.winconditionchecker

import de.htwg.se.orderandchaos.model.cell.Cell
import de.htwg.se.orderandchaos.model.grid.Grid

import scala.util.{Failure, Success, Try}

trait WinConditionChecker {
  def winningLineExists(grid: Grid): Boolean

  def noWinningLinePossible(grid: Grid): Boolean
}

private class WinConditionCheckerImpl extends WinConditionChecker {
  private val blueLine = Cell.TYPE_BLUE * WinConditionChecker.WINNINGSTREAK
  private val redLine = Cell.TYPE_RED * WinConditionChecker.WINNINGSTREAK

  def winningLineExists(grid: Grid): Boolean =
    checkLines(grid.getRows) ||
      checkLines(grid.getColumns) ||
      checkLines(grid.getUpDiagonals) ||
      checkLines(grid.getDownDiagonals)

  def checkLines(in: Try[Vector[Vector[Cell]]]): Boolean = in match {
    case Success(lines) => lines.exists(checkForWinningLine)
    case Failure(e) => throw new IllegalStateException(e)
  }

  def noWinningLinePossible(grid: Grid): Boolean =
    !winningLineExists(convertEmptyFields(grid, Cell.blue)) &&
      !winningLineExists(convertEmptyFields(grid, Cell.red))

  def checkForWinningLine(input: Vector[Cell]): Boolean = {
    val line = input.mkString("")
    line.contains(blueLine) || line.contains(redLine)
  }

  def convertEmptyFields(grid: Grid, cell: Cell): Grid = grid.mapEachCell {
    case Cell(Cell.TYPE_EMPTY) => cell
    case f: Cell => f
  }
}

object WinConditionChecker {
  val WINNINGSTREAK = 5

  def get: WinConditionChecker = new WinConditionCheckerImpl
}