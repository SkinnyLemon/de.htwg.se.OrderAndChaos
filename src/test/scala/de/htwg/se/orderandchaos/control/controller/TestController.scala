package de.htwg.se.orderandchaos.control.controller

import de.htwg.se.orderandchaos.model.cell.Cell
import de.htwg.se.orderandchaos.model.grid.{Grid, TestGrid}

import scala.util.{Failure, Success, Try}

case class TestController(override val grid: Grid = new TestGrid, override val turn: String = "",
                          playCalls: Int = 0, redCalls: Int = 0, blueCalls: Int = 0,
                          lastX: Int = -1,lastY: Int = -1, ongoing: Boolean = true, var headerCalls: Int = 0) extends Controller(grid, turn) {

  override def play(x: Int, y: Int, cellType: String): Try[Controller] = cellType match {
    case Cell.TYPE_BLUE => Success(this.copy(playCalls = playCalls + 1, blueCalls = blueCalls + 1, lastX = x, lastY = y))
    case Cell.TYPE_RED => Success(this.copy(playCalls = playCalls + 1, redCalls = redCalls + 1, lastX = x, lastY = y))
    case Cell.TYPE_EMPTY => Success(this.copy(playCalls = playCalls + 1, lastX = x, lastY = y))
    case _ => Failure(new IllegalArgumentException("Invalid cell type"))
  }

  override def toString: String = TestController.STRING_REPRESENTATION

  override def header: String = {
    headerCalls += 1
    ""
  }

  override def isOngoing: Boolean = ongoing
}

object TestController {
  val STRING_REPRESENTATION = "TestController"
}
