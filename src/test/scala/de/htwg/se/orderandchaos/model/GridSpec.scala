package de.htwg.se.orderandchaos.model

;

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GridSpec extends WordSpec with Matchers {
  "A Grid" when {
    "new" should {
      val grid = Grid.empty
      "be empty" in {
        grid.forEachField(field => field.fieldType should be(Field.EMPTY))
      }
      "have a nice String representation" in {
        val stringRepresentation = grid.toString
        stringRepresentation should startWith(s"${Field.EMPTY}, ")
        stringRepresentation should endWith(s", ${Field.EMPTY}")
      }
    }
  }
  "returns a Field" in {
    val grid = Grid.empty
    grid(3, 2).fieldType should be(Field.EMPTY)
  }
  "returns a row" in {
    val grid = Grid.empty
    val row = grid.getRow(2)
    row.length should be(Grid.width)
  }
  "returns a column" in {
    val grid = Grid.empty
    val column = grid.getColumn(3)
    column.length should be(Grid.width)
  }
  "maps its field types" in {
    val grid = Grid.empty.mapEachField(_ => Field.blue)
    grid.forEachField(field => field.fieldType should be(Field.BLUE))
    grid(4, 1).fieldType should be(Field.BLUE)
  }
}
