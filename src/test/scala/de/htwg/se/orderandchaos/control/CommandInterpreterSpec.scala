package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.{Cell, CommandParsingException}
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommandInterpreterSpec extends WordSpec with Matchers {
  "A CommandInterpreter" should {
    "interpret a set command" in {
      val control = new TestControl
      val interpreter = new CommandInterpreter(control)
      interpreter.interpretSet(s"4,3 ${Cell.TYPE_RED}")
      control.lastX should be(4)
      control.lastY should be(3)
      control.lastType should be(Cell.TYPE_RED)
    }
    "reject a wrong set command" in {
      val control = new TestControl
      val interpreter = new CommandInterpreter(control)
      assertThrows[CommandParsingException] {
        interpreter.interpretSet(s"4,3${Cell.TYPE_RED}")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet(s"4,${Cell.TYPE_RED}")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet(s"a,3 ${Cell.TYPE_RED}")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet(s"0,2 ${Cell.TYPE_RED}")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet("1,2")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet("1,2 GG")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet(Cell.TYPE_RED)
      }
    }
    "passes on errors" in {
      val control = new ErrorControl
      val interpreter = new CommandInterpreter(control)
      assertThrows[UnsupportedOperationException] {
        interpreter.interpretSet(s"3,2 ${Cell.TYPE_BLUE}")
      }
    }
  }
}
