package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.{Cell, NoMoreMovesException}
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ControlSpec extends WordSpec with Matchers {
  "A Controller" should {
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
      assertThrows[NoMoreMovesException] {
        control.undo()
      }
      control.play(4, 5, Cell.TYPE_BLUE)
      control.undo()
      assertThrows[NoMoreMovesException] {
        control.undo()
      }
    }
    "limit redos" in {
      val control: Control = new ControlImpl(startController, winConditionChecker)
      assertThrows[NoMoreMovesException] {
        control.redo()
      }
      control.play(4, 5, Cell.TYPE_BLUE)
      control.undo()
      control.redo()
      assertThrows[NoMoreMovesException] {
        control.redo()
      }
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
      control.controller.isInstanceOf[GameOverController] should be(true)
    }
    "recognizes a chaos win" in {
      val control: Control = new ControlImpl(startController, new TestWinConditionChecker(2))
      control.play(2, 3, Cell.TYPE_EMPTY)
      control.controller.isInstanceOf[GameOverController] should be(true)
    }
    "have a nice string representation" in {
      val control: Control = new ControlImpl(startController, new TestWinConditionChecker(2))
      control.toString should be(TestController.STRING_REPRESENTATION)
    }
  }
}
