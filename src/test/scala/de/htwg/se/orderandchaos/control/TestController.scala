package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.{Cell, Grid, TestGrid}

case class TestController(override val grid: Grid = new TestGrid, override val turn: String = "",
                          playCalls: Int = 0, redCalls: Int = 0, blueCalls: Int = 0,
                          lastX: Int = -1,lastY: Int = -1) extends Controller(grid, turn) {

  override def play(x: Int, y: Int, cellType: String): Controller = cellType match {
    case Cell.TYPE_BLUE => this.copy(playCalls = playCalls + 1, blueCalls = blueCalls + 1, lastX = x, lastY = y)
    case Cell.TYPE_RED => this.copy(playCalls = playCalls + 1, redCalls = redCalls + 1, lastX = x, lastY = y)
    case Cell.TYPE_EMPTY => this.copy(playCalls = playCalls + 1, lastX = x, lastY = y)
    case _ => throw new IllegalArgumentException("Invalid cell type")
  }

  override def toString: String = TestController.STRING_REPRESENTATION

  override def header: String = ""
}

object TestController {
  val STRING_REPRESENTATION = "TestController"
}
