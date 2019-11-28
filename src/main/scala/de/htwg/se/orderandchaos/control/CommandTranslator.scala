package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.control.winconditionchecker.WinConditionChecker
import de.htwg.se.orderandchaos.model.cell.Cell
import de.htwg.se.orderandchaos.model.{CommandParsingException, InvalidCellTypeException}

import scala.io.AnsiColor.{BLUE, RED, RESET}
import scala.util.{Failure, Success, Try}

class CommandTranslator(control: Control) {
  def makeColorString: String = {
    control.makeString(colorCell)
  }

  def colorCell(cell: Cell): String = cell match {
    case Cell.blue => s"$BLUE${Cell.blue}$RESET"
    case Cell.red => s"$RED${Cell.red}$RESET"
    case Cell.empty => Cell.empty.toString
    case Cell(_) => throw new IllegalArgumentException("Cell did not match known types")
  }

  def interpretSet(input: String): Unit = {
    Try({
      val values = input.split(" ")
      if (values.length != 2) throw new CommandParsingException("Both coordinates and the field type need to be set!")
      val coordinates = buildCoordinates(values(0))
      val cellType = values(1)
      control.play(coordinates(0), coordinates(1), cellType)
    }) match {
      case Failure(e: CommandParsingException) => throw new CommandParsingException(s"${e.getMessage} - Usage: ${CommandTranslator.setInstruction}")
      case Failure(_: InvalidCellTypeException) => throw new CommandParsingException(
        s"The field type was invalid! types: ${Cell.validSetTypes.mkString(", ")} - Usage: ${CommandTranslator.setInstruction}")
      case Failure(e) => throw e
      case Success(_) =>
    }
  }

  private def buildCoordinates(input: String): Array[Int] = {
    val values = input.split(",")
    if (values.length != 2) throw new CommandParsingException("X and Y value required to set a field!")
    val coordinates = Try (values.map(_.toInt)) match {
      case Success(v) => v
      case Failure(_) => throw new CommandParsingException("X and Y value need to be numbers")
    }
    if (coordinates(0) > WinConditionChecker.WINNINGSTREAK + 1 || coordinates(0) < 1
      || coordinates(1) > WinConditionChecker.WINNINGSTREAK + 1 || coordinates(0) < 1) {
      throw new CommandParsingException(s"The coordinates need to be between 1 and ${WinConditionChecker.WINNINGSTREAK + 1}")
    }
    coordinates
  }
}

object CommandTranslator {
  val setInstruction: String = "set X_VALUE,Y_VALUE CELL_TYPE"
}
