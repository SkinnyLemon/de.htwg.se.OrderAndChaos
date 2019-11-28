package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.cell.{Cell, TestCell}
import de.htwg.se.orderandchaos.model.{CommandParsingException, InvalidCellTypeException}
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

import scala.io.AnsiColor.{BLUE, RED, RESET}

@RunWith(classOf[JUnitRunner])
class CommandTranslatorSpec extends WordSpec with Matchers {
  "A CommandInterpreter" should {
    "interpret a set command" in {
      val control = new TestControl
      val interpreter = new CommandTranslator(control)
      val cellType = "A"
      interpreter.interpretSet(s"4,3 $cellType")
      control.lastX should be(4)
      control.lastY should be(3)
      control.lastType should be(cellType)
    }
    "reject a wrong set command" in {
      val control = new TestControl
      val interpreter = new CommandTranslator(control)
      assertThrows[CommandParsingException] {
        interpreter.interpretSet("4,3A")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet("4,A")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet("a,3 A")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet("0,2 A")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet("1,2")
      }
      assertThrows[CommandParsingException] {
        interpreter.interpretSet(Cell.TYPE_RED)
      }
    }
    "make colored strings" in {
      val cell = Cell.blue
      val control = new TestControl(cell = cell)
      val interpreter = new CommandTranslator(control)
      interpreter.makeColorString should be(interpreter.colorCell(cell))
      control.makeStringCalls should be(1)
    }
    "color cells" in {
      val control = new TestControl
      val interpreter = new CommandTranslator(control)
      interpreter.colorCell(Cell.blue) should be(s"$BLUE${Cell.blue}$RESET")
      interpreter.colorCell(Cell.red) should be(s"$RED${Cell.red}$RESET")
      interpreter.colorCell(Cell.empty) should be(Cell.empty.toString)
    }
    "refuse to color broken cells" in {
      val control = new TestControl
      val interpreter = new CommandTranslator(control)
      assertThrows[IllegalArgumentException] {
        interpreter.colorCell(new TestCell)
      }
    }
    "passes on errors" in {
      val control = new ErrorControl(new UnsupportedOperationException)
      val interpreter = new CommandTranslator(control)
      assertThrows[UnsupportedOperationException] {
        interpreter.interpretSet(s"3,2 ${Cell.TYPE_BLUE}")
      }
    }
    "translates invalid cell types to parsing exceptions" in {
      val control = new ErrorControl(new InvalidCellTypeException)
      val interpreter = new CommandTranslator(control)
      assertThrows[CommandParsingException] {
        interpreter.interpretSet(s"3,2 GG")
      }
    }
  }
}
