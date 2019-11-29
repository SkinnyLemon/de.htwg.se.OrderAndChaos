package de.htwg.se.orderandchaos.control.controller

import de.htwg.se.orderandchaos.model.MoveOnDecidedGameException
import de.htwg.se.orderandchaos.model.cell.{Cell, TestCell}
import de.htwg.se.orderandchaos.model.grid.TestGrid
import de.htwg.se.orderandchaos.util.ExceptionChecker
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

import scala.util.{Failure, Success}

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {
  "A Controller" should {
    "provide a new Controller" in {
      val controller = Controller.getNew
      controller.isOngoing should be(true)
    }
    "provide an ongoing Controller" in {
      val controller = Controller.getOngoing(new TestGrid, "Order")
      controller.isOngoing should be(true)
    }
    "provide a finished Controller" in {
      val controller = Controller.getFinished(new TestGrid, "Order")
      controller.isOngoing should be(false)
    }
    "play turns" in {
      val controller: Controller = Controller.getOngoing(new TestGrid, "Order")
      controller.play(1, 1, TestCell.STANDARD_TYPE) match {
        case Success(newController) =>
          val newTestGrid: TestGrid = newController.grid.asInstanceOf[TestGrid]
          newTestGrid.setCalls should be(1)
          newController.turn should be("Chaos")
        case Failure(e) => fail(e)
      }

    }
    "not play turns when the game is over" in {
      val controller: Controller = Controller.getFinished(new TestGrid, "Order")
      ExceptionChecker.checkTry[MoveOnDecidedGameException](
        controller.play(1, 1, TestCell.STANDARD_TYPE),
        "played turn")
    }
    "have a nice string representation while playing" in {
      val controller: Controller = Controller.getOngoing(new TestGrid, "Order")
      controller.toString should be(s"Turn: Order\n${TestGrid.STRING_REPRESENTATION}")
    }
    "have a nice string representation after the game is over" in {
      val controller: Controller = Controller.getFinished(new TestGrid, "Order")
      controller.toString should be(s"Game over!\n${TestGrid.STRING_REPRESENTATION}")
    }
    "make colored strings" in {
      val grid = new TestGrid(Cell.blue)
      val controller: Controller = Controller.getFinished(grid, "Order")
      controller.makeString(_ => "A") should be(s"${controller.header}\nA")
      grid.makeStringCalls = 1
    }
  }
}
