package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.control.controller.TestController
import de.htwg.se.orderandchaos.control.winconditionchecker.TestWinConditionChecker
import de.htwg.se.orderandchaos.model.NoMoreMovesException
import de.htwg.se.orderandchaos.model.cell.Cell
import de.htwg.se.orderandchaos.util.ExceptionChecker
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class ControlSpec extends WordSpec with Matchers {
  "A Control" should {
    val startController = new TestController
    val winConditionChecker = new TestWinConditionChecker
    "play a red cell" in {
      val control: Control = new ControlImpl(startController, winConditionChecker)
      control.playRed(0, 1)
      val newController = control.controller.asInstanceOf[TestController]
      newController.lastX should be(0)
      newController.lastY should be(1)
      newController.playCalls should be(1)
      newController.redCalls should be(1)
    }
    "play a blue cell" in {
      val control: Control = new ControlImpl(startController, winConditionChecker)
      control.playBlue(2, 3)
      val newController = control.controller.asInstanceOf[TestController]
      newController.lastX should be(2)
      newController.lastY should be(3)
      newController.playCalls should be(1)
      newController.blueCalls should be(1)
    }
    "play a cell" in {
      val control: Control = new ControlImpl(startController, winConditionChecker)
      control.play(2, 3, Cell.TYPE_EMPTY)
      val newController = control.controller.asInstanceOf[TestController]
      newController.lastX should be(2)
      newController.lastY should be(3)
      newController.playCalls should be(1)
      newController.redCalls should be(0)
      newController.blueCalls should be(0)
    }
    "undo a turn" in {
      val control: Control = new ControlImpl(startController, winConditionChecker)
      control.play(2, 3, Cell.TYPE_EMPTY)
      control.play(4, 5, Cell.TYPE_BLUE)
      control.undo()
      val newController = control.controller.asInstanceOf[TestController]
      newController.lastX should be(2)
      newController.lastY should be(3)
      newController.playCalls should be(1)
      newController.redCalls should be(0)
      newController.blueCalls should be(0)
    }
    "redo a turn" in {
      val control: Control = new ControlImpl(startController, winConditionChecker)
      control.play(2, 3, Cell.TYPE_EMPTY)
      control.play(4, 5, Cell.TYPE_BLUE)
      control.undo()
      control.redo()
      val newController = control.controller.asInstanceOf[TestController]
      newController.lastX should be(4)
      newController.lastY should be(5)
      newController.playCalls should be(2)
      newController.redCalls should be(0)
      newController.blueCalls should be(1)
    }
    "limit undos" in {
      val control: Control = new ControlImpl(startController, winConditionChecker)
      ExceptionChecker.checkTry[NoMoreMovesException](control.undo(), "Did too many undos")
      control.play(4, 5, Cell.TYPE_BLUE)
      control.undo()
      ExceptionChecker.checkTry[NoMoreMovesException](control.undo(), "Did too many undos")
    }
    "limit redos" in {
      val control: Control = new ControlImpl(startController, winConditionChecker)
      ExceptionChecker.checkTry[NoMoreMovesException](control.redo(), "Did too many redos")
      control.play(4, 5, Cell.TYPE_BLUE)
      control.undo()
      control.redo()
      ExceptionChecker.checkTry[NoMoreMovesException](control.redo(), "Did too many redos")
    }
    "reset" in {
      val control: Control = new ControlImpl(startController, winConditionChecker)
      control.play(2, 3, Cell.TYPE_EMPTY)
      control.play(4, 5, Cell.TYPE_BLUE)
      control.reset()
      control.controller should be(startController)
    }
    "recognizes an order win" in {
      val control: Control = new ControlImpl(startController, new TestWinConditionChecker(1))
      control.play(2, 3, Cell.TYPE_EMPTY)
      control.controller.isOngoing should be(false)
    }
    "recognizes a chaos win" in {
      val control: Control = new ControlImpl(startController, new TestWinConditionChecker(2))
      control.play(2, 3, Cell.TYPE_EMPTY)
      control.controller.isOngoing should be(false)
    }
    "have a nice string representation" in {
      val control: Control = new ControlImpl(startController, new TestWinConditionChecker(2))
      control.toString should be(TestController.STRING_REPRESENTATION)
    }
    "make a custom string representation" in {
      val control: Control = new ControlImpl(startController, new TestWinConditionChecker(2))
      control.makeString(_ => "") should be("\n")
      control.controller.asInstanceOf[TestController].headerCalls should be(1)
    }
  }
}
