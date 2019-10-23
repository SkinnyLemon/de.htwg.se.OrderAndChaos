package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.{Field, Grid}

class Control private (grid: Grid) {
  def playRed(x: Int, y: Int): Control = new Control(grid.set(x, y, Field.RED))
  def playBlue(x: Int, y: Int): Control = new Control(grid.set(x, y, Field.BLUE))
  private def play(x: Int, y: Int, fieldType: String): Control = {
    val newGrid = grid.set(x, y, fieldType)
    if (WinConditionChecker.checkGrid(grid)) {
      println("WON")
    }
    new Control(newGrid)
  }
}

object Control {
  def newInstance: Control = new Control(Grid.empty)
}
