package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.{Cell, Grid}

import scala.annotation.tailrec

trait WinConditionChecker {
  def winningLineExists(grid: Grid): Boolean
  def noWinningLinePossible(grid: Grid): Boolean
}

private class WinConditionCheckerImpl extends WinConditionChecker {
  private val blueLine = Cell.TYPE_BLUE * WinConditionChecker.WINNINGSTREAK
  private val redLine = Cell.TYPE_RED * WinConditionChecker.WINNINGSTREAK

  def winningLineExists(grid: Grid): Boolean =
    grid.getRows.exists(checkForWinningLine) ||
      grid.getColumns.exists(checkForWinningLine) ||
      grid.getUpDiagonals.exists(checkForWinningLine) ||
      grid.getDownDiagonals.exists(checkForWinningLine)

  def noWinningLinePossible(grid: Grid): Boolean =
    !winningLineExists(convertEmptyFields(grid, Cell.blue)) &&
      !winningLineExists(convertEmptyFields(grid, Cell.red))

  def checkForWinningLine(input: Vector[Cell]): Boolean = {
    @tailrec
    def checkRest(line: Vector[Cell], streak: Int, previous: String): Boolean = {
      if (streak >= WinConditionChecker.WINNINGSTREAK) {
        true
      } else if (line.length + streak < WinConditionChecker.WINNINGSTREAK) {
        false
      } else {
        line(0).cellType match {
          case Cell.TYPE_EMPTY => checkRest(line.tail, 0, Cell.TYPE_EMPTY)
          case fieldType if fieldType equals previous => checkRest(line.tail, streak + 1, fieldType)
          case fieldType => checkRest(line.tail, 1, fieldType)
        }
      }
    }

    //checkRest(input, 0, Cell.TYPE_EMPTY)


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