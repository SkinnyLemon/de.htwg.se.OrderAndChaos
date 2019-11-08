package de.htwg.se.orderandchaos.control

import de.htwg.se.orderandchaos.model.{CommandParsingException, Cell}

import scala.util.{Failure, Success, Try}

class CommandInterpreter(control: Control) {
  def interpretSet(input: String): Unit = {
    Try({
      val values = input.split(" ")
      if (values.length != 2) throw new CommandParsingException("Both coordinates and the field type need to be set!")
      val coordinates = buildCoordinates(values(0))
      val fieldType = checkFieldType(values(1))
      control.play(coordinates(0), coordinates(1), fieldType)
    }) match {
      case Failure(e: CommandParsingException) => throw new CommandParsingException(s"${e.getMessage} - Usage: ${CommandInterpreter.setInstruction}")
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

  private def checkFieldType(input: String): String = if (input == Cell.TYPE_RED || input == Cell.TYPE_BLUE) {
    input
  } else {
    throw new CommandParsingException(s"The Field Type needs to be specified as ${Cell.TYPE_RED} or ${Cell.TYPE_BLUE}")
  }
}

object CommandInterpreter {
  val setInstruction: String = "set X_VALUE,Y_VALUE FIELD_TYPE"
}
