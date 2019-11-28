package de.htwg.se.orderandchaos.control.winconditionchecker

import de.htwg.se.orderandchaos.model.cell.Cell
import de.htwg.se.orderandchaos.model.grid.{Grid, TestGrid}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WinConditionCheckerSpec extends WordSpec with Matchers {
  if (WinConditionChecker.WINNINGSTREAK < 3) throw new IllegalStateException("Cannot test for streaks of less than 3")

  "A WinConditionChecker" should {
    "build a new instance" in {
      WinConditionChecker.get.isInstanceOf[WinConditionCheckerImpl] should be(true)
    }
  }
  "A WinConditionChecker" when {
    val winConditionChecker = new WinConditionCheckerImpl
    "checking for an Order win" should {
      "check every line direction" in {
        val grid = new TestGrid
        winConditionChecker.winningLineExists(grid)
        grid.getRowsCalls should be(1)
        grid.getColumnsCalls should be(1)
        grid.getDownDiagonalsCalls should be(1)
        grid.getUpDiagonalsCalls should be(1)
      }
      "recognize a full blue line as a win" in {
        val streak = Vector.fill[Cell](WinConditionChecker.WINNINGSTREAK)(Cell.blue)
        winConditionChecker.checkForWinningLine(streak) should be(true)
      }
      "recognize a full red line as a win" in {
        val streak = Vector.fill[Cell](WinConditionChecker.WINNINGSTREAK)(Cell.red)
        winConditionChecker.checkForWinningLine(streak) should be(true)
      }
      "recognize the smallest possible line at the end as a win" in {
        val streak =
          Vector.fill[Cell](2)(Cell.blue) ++
            Vector.fill[Cell](WinConditionChecker.WINNINGSTREAK)(Cell.red) ++
            Vector.fill[Cell](2)(Cell.blue)
        winConditionChecker.checkForWinningLine(streak) should be(true)
      }
      "recognize an empty line as not a win" in {
        val streak = Vector.fill[Cell](WinConditionChecker.WINNINGSTREAK + 3)(Cell.empty)
        winConditionChecker.checkForWinningLine(streak) should be(false)
      }
      "recognize too short a streak as not a win" in {
        val streak =
          Vector.fill[Cell](WinConditionChecker.WINNINGSTREAK - 1)(Cell.red) ++
            Vector.fill[Cell](WinConditionChecker.WINNINGSTREAK - 1)(Cell.blue)
        winConditionChecker.checkForWinningLine(streak) should be(false)
      }
      "recognize a separated streak as not a win" in {
        val streak =
          Vector.fill[Cell](2)(Cell.blue) ++
            Vector.fill[Cell](WinConditionChecker.WINNINGSTREAK - 1)(Cell.red) ++
            Vector.fill[Cell](2)(Cell.blue) ++
            Vector.fill[Cell](WinConditionChecker.WINNINGSTREAK - 1)(Cell.red) ++
            Vector.fill[Cell](2)(Cell.blue)
        winConditionChecker.checkForWinningLine(streak) should be(false)
      }
    }
  }
  "A WinConditionChecker" when {
    val winConditionChecker = new WinConditionCheckerImpl
    "checking for a Chaos win" should {
      "check every line direction for every color" in {
        val grid = new TestGrid
        winConditionChecker.noWinningLinePossible(grid)
        grid.getRowsCalls should be(2)
        grid.getColumnsCalls should be(2)
        grid.getDownDiagonalsCalls should be(2)
        grid.getUpDiagonalsCalls should be(2)
      }
      "convert empty fields to set ones" in {
        val grid = Grid.empty
        val setGrid = winConditionChecker.convertEmptyFields(grid, Cell.red)
        setGrid.exists {
          case Cell(Cell.TYPE_EMPTY) => true
          case Cell(Cell.TYPE_BLUE) => true
          case Cell(Cell.TYPE_RED) => false
          case _ => throw new IllegalStateException
        } should be(false)
      }
      "convert and already set field" in {
        val grid = Grid.empty
        val setGrid = winConditionChecker.convertEmptyFields(winConditionChecker.convertEmptyFields(grid, Cell.red), Cell.red)
        setGrid.exists {
          case Cell(Cell.TYPE_EMPTY) => true
          case Cell(Cell.TYPE_BLUE) => true
          case Cell(Cell.TYPE_RED) => false
          case _ => throw new IllegalStateException
        } should be(false)
      }
    }
  }
}
