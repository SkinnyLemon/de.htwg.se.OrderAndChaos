package de.htwg.se.orderandchaos.control
import de.htwg.se.orderandchaos.model.Grid

class TestWinConditionChecker(returnValue: Int = 0) extends WinConditionChecker {
  override def winningLineExists(grid: Grid): Boolean = returnValue == 1

  override def noWinningLinePossible(grid: Grid): Boolean = returnValue == 2
}
