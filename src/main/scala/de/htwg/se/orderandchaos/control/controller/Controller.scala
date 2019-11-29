package de.htwg.se.orderandchaos.control.controller

import de.htwg.se.orderandchaos.model.cell.Cell
import de.htwg.se.orderandchaos.model.MoveOnDecidedGameException
import de.htwg.se.orderandchaos.model.grid.Grid

import scala.util.{Failure, Success, Try}

abstract class Controller(val grid: Grid, val turn: String) {
  def play(x: Int, y: Int, fieldType: String): Try[Controller]
  def isOngoing: Boolean
  override def toString: String = s"$header\n${grid.toString}"
  def makeString(cellToString: Cell => String): String = s"$header\n${grid.makeString(cellToString)}"
  def header: String
}

object Controller {
  def getNew: Controller = new StandardController
  def getOngoing(grid: Grid, turn: String): Controller = new StandardController(grid, turn)
  def getFinished(grid: Grid, winner: String): Controller = new GameOverController(grid, winner)
}

private class StandardController(grid: Grid = Grid.empty, override val turn: String = "Order") extends Controller(grid, turn) {
  override def play(x: Int, y: Int, fieldType: String): Try[Controller] = {
    val nextTurn = if (turn == "Order") "Chaos" else "Order"
    grid.set(x - 1, y - 1, fieldType) match {
      case Success(newGrid) => Success(new StandardController(newGrid, nextTurn))
      case Failure(e) => Failure(e)
    }
  }

  override def isOngoing: Boolean = true

  override def header: String = s"Turn: $turn"
}

private class GameOverController(grid: Grid, winner: String) extends Controller(grid, winner) {
  override def play(x: Int, y: Int, fieldType: String): Try[Controller] = Failure(new MoveOnDecidedGameException)

  override def isOngoing: Boolean = false

  override def header: String = s"Game over!"
}