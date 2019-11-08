package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.{TestCell, TestGrid}
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {
  "A Controller" should {
    "play turns" in {
      val controller: Controller = new StandardController(new TestGrid, "Order")
      val newController = controller.play(1, 1, TestCell.STANDARD_TYPE)
      val newTestGrid: TestGrid = newController.grid.asInstanceOf[TestGrid]
      newTestGrid.setCalls should be(1)
      newController.turn should be("Chaos")
    }
    "not play turns when the game is over" in {
      val controller: Controller = new GameOverController(new TestGrid)
      assertThrows[UnsupportedOperationException] {
        controller.play(1, 1, TestCell.STANDARD_TYPE)
      }
    }
    "have a nice string representation while playing" in {
      val controller: Controller = new StandardController(new TestGrid, "Order")
      controller.toString should be(s"Turn: Order\n${TestGrid.STRING_REPRESENTATION}")
    }
    "have a nice string representation after the game is over" in {
      val controller: Controller = new GameOverController(new TestGrid)
      controller.toString should be(s"Game over!\n${TestGrid.STRING_REPRESENTATION}")
    }
  }
}
