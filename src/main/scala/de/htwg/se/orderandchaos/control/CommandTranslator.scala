package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.control.winconditionchecker.WinConditionChecker
import de.htwg.se.orderandchaos.model.cell.Cell
import de.htwg.se.orderandchaos.model.{CommandParsingException, InvalidCellTypeException}

import scala.io.AnsiColor.{BLUE, RED, RESET}
import scala.util.{Failure, Success, Try}

class CommandTranslator(control: Control) {
  def makeColorString: String = {
    control.makeString(colorCell(_) match {
      case Success(coloredString) => coloredString
      case Failure(e) => throw new IllegalStateException(e)
    })
  }

  def colorCell(cell: Cell): Try[String] = cell match {
    case Cell.blue => Success(s"$BLUE${Cell.blue}$RESET")
    case Cell.red => Success(s"$RED${Cell.red}$RESET")
    case Cell.empty => Success(Cell.empty.toString)
    case Cell(_) => Failure(new IllegalArgumentException("Cell did not match known types"))
  }

  def interpretSet(input: String): Try[Unit] = {
    val values = input.split(" ")
    if (values.length != 2) {
      Failure(new CommandParsingException("Both coordinates and the field type need to be set!"))
    } else {
      val cellType = values(1)
      buildCoordinates(values(0)) match {
        case Success(coordinates) => control.play(coordinates(0), coordinates(1), cellType)
        case Failure(e) => Failure(e)
      }
    } match {
      case Failure(e: CommandParsingException) => Failure(new CommandParsingException(s"${e.getMessage} - Usage: ${CommandTranslator.setInstruction}"))
      case Failure(_: InvalidCellTypeException) => Failure(new CommandParsingException(
        s"The field type was invalid! types: ${Cell.validSetTypes.mkString(", ")} - Usage: ${CommandTranslator.setInstruction}"))
      case Failure(e) => Failure(e)
      case Success(_) => Success()
    }
  }

  private def buildCoordinates(input: String): Try[Array[Int]] = {
    val values = input.split(",")
    if (values.length != 2)
      Failure(new CommandParsingException("X and Y value required to set a field!"))
    else
      Try(values.map(_.toInt)) match {
        case Success(coordinates) =>
          if (coordinates(0) > WinConditionChecker.WINNINGSTREAK + 1 || coordinates(0) < 1
            || coordinates(1) > WinConditionChecker.WINNINGSTREAK + 1 || coordinates(0) < 1)
            Failure(new CommandParsingException(s"The coordinates need to be between 1 and ${WinConditionChecker.WINNINGSTREAK + 1}"))
          else
            Success(coordinates)
        case Failure(_) => Failure(new CommandParsingException("X and Y value need to be numbers"))
      }

  }
}

object CommandTranslator {
  val setInstruction: String = "set X_VALUE,Y_VALUE CELL_TYPE"
}
