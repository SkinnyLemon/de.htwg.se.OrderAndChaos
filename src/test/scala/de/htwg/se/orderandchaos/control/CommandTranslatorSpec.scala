package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.cell.{Cell, TestCell}
import de.htwg.se.orderandchaos.model.{CommandParsingException, InvalidCellTypeException}
import de.htwg.se.orderandchaos.util.ExceptionChecker
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

import scala.io.AnsiColor.{BLUE, RED, RESET}
import scala.util.Success

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
      val checkInterpret = ExceptionChecker.checkTryF1[CommandParsingException, String](interpreter.interpretSet, "accepted command")
      checkInterpret("4,3A")
      checkInterpret("4,A")
      checkInterpret("a,3 A")
      checkInterpret("0,2 A")
      checkInterpret("1,2")
      checkInterpret("1 A")
      checkInterpret(Cell.TYPE_RED)
    }
    "make colored strings" in {
      val cell = Cell.blue
      val control = new TestControl(cell = cell)
      val interpreter = new CommandTranslator(control)
      Success(interpreter.makeColorString) should be(interpreter.colorCell(cell))
      control.makeStringCalls should be(1)
    }
    "color cells" in {
      val control = new TestControl
      val interpreter = new CommandTranslator(control)
      interpreter.colorCell(Cell.blue) should be(Success(s"$BLUE${Cell.blue}$RESET"))
      interpreter.colorCell(Cell.red) should be(Success(s"$RED${Cell.red}$RESET"))
      interpreter.colorCell(Cell.empty) should be(Success(Cell.empty.toString))
    }
    "refuse to color broken cells" in {
      val control = new TestControl
      val interpreter = new CommandTranslator(control)
      ExceptionChecker.checkTry[IllegalArgumentException](interpreter.colorCell(new TestCell), "accepted broken cell")
    }
    "pass on errors" in {
      val control = new ErrorControl(new UnsupportedOperationException)
      val interpreter = new CommandTranslator(control)
      ExceptionChecker.checkTry[UnsupportedOperationException](interpreter.interpretSet(s"3,2 ${Cell.TYPE_BLUE}"),
        "did not pass on error",
        "passed on wrong error")
    }
    "translate invalid cell types to parsing exceptions" in {
      val control = new ErrorControl(new InvalidCellTypeException)
      val interpreter = new CommandTranslator(control)
      ExceptionChecker.checkTry[CommandParsingException](interpreter.interpretSet(s"3,2 GG"),
        "accepted invalid cell types",
        "did not translate to the correct error")
    }
  }
}
