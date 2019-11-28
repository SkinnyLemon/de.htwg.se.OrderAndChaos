package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.control.controller.Controller
import de.htwg.se.orderandchaos.control.winconditionchecker.WinConditionChecker
import de.htwg.se.orderandchaos.model.NoMoreMovesException
import de.htwg.se.orderandchaos.model.cell.Cell

import scala.swing.Publisher

trait Control extends Publisher {
  def playRed(x: Int, y: Int): Unit

  def playBlue(x: Int, y: Int): Unit

  def play(x: Int, y: Int, fieldType: String): Unit

  def undo(): Unit

  def redo(): Unit

  def reset(): Unit

  def controller: Controller

  def makeString(cellToString: Cell => String): String
}

class ControlImpl(startController: Controller = Controller.getNew,
                  winConditionChecker: WinConditionChecker = WinConditionChecker.get) extends Control {
  private var currentController: Controller = startController
  private var pastMoves: Vector[Controller] = Vector.empty
  private var futureMoves: Vector[Controller] = Vector.empty

  override def controller: Controller = currentController

  override def playRed(x: Int, y: Int): Unit = play(x, y, Cell.TYPE_RED)

  override def playBlue(x: Int, y: Int): Unit = play(x, y, Cell.TYPE_BLUE)

  override def play(x: Int, y: Int, fieldType: String): Unit = {
    futureMoves = Vector.empty
    pastMoves = currentController +: pastMoves
    val newController = currentController.play(x, y, fieldType)
    val grid = newController.grid
    if (winConditionChecker.winningLineExists(grid)) {
      currentController = Controller.getFinished(grid, "Order")
      publish(new Win("Order"))
    } else if (winConditionChecker.noWinningLinePossible(grid)) {
      currentController = Controller.getFinished(grid, "Chaos")
      publish(new Win("Chaos"))
    } else {
      currentController = newController
      publish(new CellSet)
    }
  }

  //noinspection DuplicatedCode
  override def undo(): Unit = {
    if (pastMoves.isEmpty) throw new NoMoreMovesException
    futureMoves = currentController +: futureMoves
    currentController = pastMoves.head
    pastMoves = pastMoves.tail
    publish(new CellSet)
  }

  //noinspection DuplicatedCode
  override def redo(): Unit = {
    if (futureMoves.isEmpty) throw new NoMoreMovesException
    pastMoves = currentController +: pastMoves
    currentController = futureMoves.head
    futureMoves = futureMoves.tail
    publish(new CellSet)
  }

  override def reset(): Unit = {
    pastMoves = currentController +: pastMoves
    currentController = startController
    publish(new CellSet)
  }

  override def toString: String = currentController.toString
  override def makeString(cellToString: Cell => String): String = currentController.makeString(cellToString)
}
