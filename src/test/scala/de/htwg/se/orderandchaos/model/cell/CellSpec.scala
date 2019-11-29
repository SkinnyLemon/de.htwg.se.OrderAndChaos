package de.htwg.se.orderandchaos.model.cell

import de.htwg.se.orderandchaos.model.IllegalOverrideException
import de.htwg.se.orderandchaos.util.ExceptionChecker
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

import scala.util.Success

@RunWith(classOf[JUnitRunner])
class CellSpec extends WordSpec with Matchers {
  "be able to be blue" in {
    Cell.blue should be(new SetCell(Cell.TYPE_BLUE))
    Cell.ofType(Cell.TYPE_BLUE) should be(Success(new SetCell(Cell.TYPE_BLUE)))
  }
  "be able to be red" in {
    Cell.red should be(new SetCell(Cell.TYPE_RED))
    Cell.ofType(Cell.TYPE_RED) should be(Success(new SetCell(Cell.TYPE_RED)))
  }
  "be able to be empty" in {
    Cell.empty should be(new SetCell(Cell.TYPE_EMPTY))
    Cell.ofType(Cell.TYPE_EMPTY) should be(Success(new SetCell(Cell.TYPE_EMPTY)))
  }
  "set an empty cell" in {
    Cell.empty.setType(Cell.TYPE_BLUE) should be(Success(new SetCell(Cell.TYPE_BLUE)))
  }
  "have a nice string representation" in {
    Cell.blue.toString should be(Cell.blue.cellType)
    Cell.red.toString should be(Cell.red.cellType)
    Cell.empty.toString should be(Cell.empty.cellType)
  }
  "when created not accept wrong types" in {
    assertThrows[IllegalStateException] {
      new SetCell("ABC")
    }
  }
  "when empty not accept wrong types" in {
    ExceptionChecker.checkTry[IllegalArgumentException](Cell.empty.setType(Cell.TYPE_EMPTY), "accepted wrong type")
  }
  "recognize blue cells as such" in {
    val blueCell = new SetCell(Cell.TYPE_BLUE)
    blueCell.isBlue should be(true)
    blueCell.isRed should be(false)
    blueCell.isEmpty should be(false)
    blueCell.isSet should be(true)
  }
  "recognize red cells as such" in {
    val redCell = new SetCell(Cell.TYPE_RED)
    redCell.isBlue should be(false)
    redCell.isRed should be(true)
    redCell.isEmpty should be(false)
    redCell.isSet should be(true)
  }
  "recognize empty cells as such" in {
    val emptyCell = new EmptyCell
    emptyCell.isBlue should be(false)
    emptyCell.isRed should be(false)
    emptyCell.isEmpty should be(true)
    emptyCell.isSet should be(false)
  }
  "not accept being set when already set" in {
    ExceptionChecker.checkTry[IllegalOverrideException](Cell.blue.setType(Cell.TYPE_RED), "accepted override")
    ExceptionChecker.checkTry[IllegalOverrideException](Cell.red.setType(Cell.TYPE_BLUE), "accepted override")
  }
}
