package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.Cell

class TestControl(controllerToUse: Controller = new TestController) extends Control {
  var redCalls = 0
  var blueCalls = 0
  var playCalls = 0
  var undoCalls = 0
  var redoCalls = 0
  var resetCalls = 0
  var controllerCalls = 0
  var lastX: Int = _
  var lastY: Int = _
  var lastType: String = _

  override def playRed(x: Int, y: Int): Unit = {
    redCalls += 1
    lastX = x
    lastY = y
    lastType = Cell.TYPE_RED
  }

  override def playBlue(x: Int, y: Int): Unit = {
    blueCalls += 1
    lastX = x
    lastY = y
    lastType = Cell.TYPE_BLUE
  }

  override def play(x: Int, y: Int, cellType: String): Unit = {
    playCalls += 1
    lastX = x
    lastY = y
    lastType = cellType
  }

  override def undo(): Unit = undoCalls += 1

  override def redo(): Unit = redoCalls += 1

  override def reset(): Unit = resetCalls += 1

  override def controller: Controller = {
    controllerCalls += 1
    controllerToUse
  }
}

class ErrorControl extends Control {
  override def playRed(x: Int, y: Int): Unit = throw new UnsupportedOperationException

  override def playBlue(x: Int, y: Int): Unit = throw new UnsupportedOperationException

  override def play(x: Int, y: Int, fieldType: String): Unit = throw new UnsupportedOperationException

  override def undo(): Unit = throw new UnsupportedOperationException

  override def redo(): Unit = throw new UnsupportedOperationException

  override def reset(): Unit = throw new UnsupportedOperationException

  override def controller: Controller = throw new UnsupportedOperationException
}
