package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.control.controller.{Controller, TestController}
import de.htwg.se.orderandchaos.model.cell.Cell

import scala.util.{Failure, Success, Try}

class TestControl(controllerToUse: Controller = new TestController, cell: Cell = Cell.red, returnValue: Try[Unit] = Success()) extends Control {
  var redCalls = 0
  var blueCalls = 0
  var playCalls = 0
  var undoCalls = 0
  var redoCalls = 0
  var resetCalls = 0
  var saveCalls = 0
  var loadCalls = 0
  var controllerCalls = 0
  var makeStringCalls = 0
  var lastX: Int = _
  var lastY: Int = _
  var lastType: String = _
  var lastCellMethod: Cell => String = _

  override def playRed(x: Int, y: Int): Try[Unit] = {
    redCalls += 1
    lastX = x
    lastY = y
    lastType = Cell.TYPE_RED
    returnValue
  }

  override def playBlue(x: Int, y: Int): Try[Unit] = {
    blueCalls += 1
    lastX = x
    lastY = y
    lastType = Cell.TYPE_BLUE
    returnValue
  }

  override def play(x: Int, y: Int, cellType: String): Try[Unit] = {
    playCalls += 1
    lastX = x
    lastY = y
    lastType = cellType
    returnValue
  }

  override def undo(): Try[Unit] = {
    undoCalls += 1
    returnValue
  }

  override def redo(): Try[Unit] = {
    redoCalls += 1
    returnValue
  }

  override def reset(): Try[Unit] = {
    resetCalls += 1
    returnValue
  }

  override def save(): Unit = saveCalls += 1

  override def load(): Unit = loadCalls += 1

  override def controller: Controller = {
    controllerCalls += 1
    controllerToUse
  }

  override def makeString(cellToString: Cell => String): String = {
    lastCellMethod = cellToString
    makeStringCalls += 1
    cellToString(cell)
  }
}

class ErrorControl(exception: Exception) extends Control {
  override def playRed(x: Int, y: Int): Try[Unit] = Failure(exception)

  override def playBlue(x: Int, y: Int): Try[Unit] = Failure(exception)

  override def play(x: Int, y: Int, fieldType: String): Try[Unit] = Failure(exception)

  override def undo(): Try[Unit] = Failure(exception)

  override def redo(): Try[Unit] = Failure(exception)

  override def reset(): Try[Unit] = Failure(exception)

  override def save(): Unit = throw exception

  override def load(): Unit = throw exception

  override def controller: Controller = throw exception

  override def makeString(cellToString: Cell => String): String = ""
}
