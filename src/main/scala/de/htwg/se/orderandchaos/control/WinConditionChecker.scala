package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.{Field, Grid}

import scala.annotation.tailrec

object WinConditionChecker {
  val WINNINGSTREAK = 5

  def checkGrid(grid: Grid): Boolean =
    grid.getRows.exists(checkLine) ||
    grid.getColumns.exists(checkLine) ||
    grid.getUpDiagonals.exists(checkLine) ||
    grid.getDownDiagonals.exists(checkLine)

  def checkLine(input: Vector[Field]): Boolean = {
    @tailrec
    def checkRest(line: Vector[Field], streak: Int, previous: String): Boolean = {
      if (streak >= WinConditionChecker.WINNINGSTREAK) {
        true
      } else if (line.length + streak < WinConditionChecker.WINNINGSTREAK) {
        false
      } else {
        val fieldType = line(0).fieldType
        if (fieldType == Field.EMPTY || fieldType != previous) {
          checkRest(line.tail, 0, fieldType)
        } else {
          checkRest(line.tail, streak + 1, fieldType)
        }
      }
    }
    checkRest(input, 0, Field.EMPTY)
  }
}
