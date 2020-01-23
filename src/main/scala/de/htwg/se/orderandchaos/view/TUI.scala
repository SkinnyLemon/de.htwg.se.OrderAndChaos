package de.htwg.se.orderandchaos.view

import com.google.inject.Inject
import de.htwg.se.orderandchaos.control.{CellSet, CommandTranslator, Control, Win}
import de.htwg.se.orderandchaos.model.{CommandParsingException, OacException}

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.swing.Reactor
import scala.util.{Failure, Success, Try}

//noinspection ScalaStyle
final class TUI(@Inject control: Control) extends Reactor {
  listenTo(control)
  val interpreter = new CommandTranslator(control)
  var stop = false
  println("Welcome to Order and Chaos! Type \"help\" to display the available commands.")
  println(control.toString)

  @tailrec
  def interpretLines(): Unit = {
    readLine() match {
      case "help" => println("The available commands are: set, print, undo, redo, q. To learn more about them type them with a -h")
      case "set -h" => println(s"This command sets a field to the specified type. Only empty fields can be set. Use like this:\n${CommandTranslator.setInstruction}")
      case "print -h" => println("This command prints out the current board")
      case "undo -h" => println("This undoes the last move made and saves it to redo later")
      case "redo -h" => println("This redoes the last move that was undone")
      case "q -h" => println(s"This command ends the game")
      case "q" =>
        println("Ending game")
        return
      case command =>
        if (stop) {
          println("Game ended!")
          return
        }
        Try(command match {
          case set if set startsWith "set " => interpreter.interpretSet(set.substring(4))
          case "undo" => control.undo()
          case "redo" => control.redo()
          case "reset" => control.reset()
          case "save" => control.save()
          case "load" => control.load()
          case "print" => println(interpreter.makeColorString)
          case _ => throw new CommandParsingException("Unrecognized command!")
        }) match {
          case Failure(e: CommandParsingException) => println(s"Error parsing command: ${e.getMessage}")
          case Failure(e: OacException) => println(s"Error executing command: ${e.getMessage}")
          case Failure(e) => throw e
          case Success(_) =>
        }
    }
    if (stop) {
      println("Game ended!")
      return
    }
    interpretLines()
  }

  reactions += {
    case _: CellSet => println(interpreter.makeColorString)
    case win: Win => handleWin(win)
  }

  def handleWin(win: Win): Unit = {
    println(s"Player ${win.player} won the game!")
    println(interpreter.makeColorString)
  }
}
