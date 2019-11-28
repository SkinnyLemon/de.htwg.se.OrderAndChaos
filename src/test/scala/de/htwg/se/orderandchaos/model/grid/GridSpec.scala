package de.htwg.se.orderandchaos.model.grid

import de.htwg.se.orderandchaos.model.cell.{Cell, TestCell}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GridSpec extends WordSpec with Matchers {
  if (Grid.WIDTH < 4) throw new IllegalStateException("Cannot test for grid sizes of less than 4")
  "A Grid" when {
    "new" should {
      val grid = Grid.empty
      "be empty" in {
        grid.forEachCell(field => field.cellType should be(Cell.TYPE_EMPTY))
      }
      "have a nice string representation" in {
        val stringRepresentation = grid.toString
        stringRepresentation should startWith(s"${Cell.TYPE_EMPTY} ")
        stringRepresentation should endWith(s" ${Cell.TYPE_EMPTY}")
      }
      "make colored strings" in {
        grid.makeString(_ => "A") should be(
          ((("A " * Grid.WIDTH).dropRight(1) + "\n") * Grid.WIDTH).dropRight(1)
        )
      }
    }
  }
  val initialGrid: Grid = Grid.fill(new TestCell)
  "returns a cell" in {
    initialGrid(3, 2).cellType should be(TestCell.STANDARD_TYPE)
  }
  "maps its cell types" in {
    val newType = "T2"
    val newGrid = initialGrid.mapEachCell(field => field.setType(newType))
    newGrid.forEachCell(field => field.cellType should be(newType))
    newGrid(4, 1).cellType should be(newType)
  }
  var buildingGrid: Grid = initialGrid
  "sets cells" in {
    for (i <- 0 until Grid.WIDTH; j <- 0 until Grid.WIDTH) {
      val newType = s"$i$j"
      buildingGrid = buildingGrid.set(i, j, newType)
      buildingGrid(i, j).cellType should be(newType)
    }
  }
  for (i <- 0 until Grid.WIDTH; j <- 0 until Grid.WIDTH) {
    val newType = s"$i$j"
    buildingGrid = buildingGrid.set(i, j, newType)
    buildingGrid(i, j).cellType should be(newType)
  }
  val diverseGrid: Grid = buildingGrid
  "returns a row" in {
    val j = 2
    val row: Vector[Cell] = diverseGrid.getRow(j)
    row.length should be(Grid.WIDTH)
    for (i <- 0 until Grid.WIDTH) {
      row(i).cellType should be(s"$i$j")
    }
  }
  "returns a column" in {
    val i = 3
    val column = diverseGrid.getColumn(i)
    column.length should be(Grid.WIDTH)
    for (j <- 0 until Grid.WIDTH) {
      column(j).cellType should be(s"$i$j")
    }
  }
  "returns an upwards diagonal" in {
    val x = 1
    val y = 0
    val upDiagonal = diverseGrid.getUpDiagonal(x, y)
    upDiagonal.length should be(Grid.WIDTH - x - y)
    for (index <- upDiagonal.indices) {
      upDiagonal(index).cellType should be(s"${x + index}${y + index}")
    }
  }
  "does not allow wrong upwards diagonals" in {
    assertThrows[IllegalArgumentException] {
      diverseGrid.getUpDiagonal(1, 1)
    }
    assertThrows[IllegalArgumentException] {
      diverseGrid.getUpDiagonal(-1, 0)
    }
    assertThrows[IllegalArgumentException] {
      diverseGrid.getUpDiagonal(Grid.WIDTH, 0)
    }
  }
  "returns a downwards diagonal" in {
    val x = 0
    val y = 2
    val downDiagonal = diverseGrid.getDownDiagonal(x, y)
    val expectedLength = if (x > 0) Grid.WIDTH - y else y + 1
    downDiagonal.length should be(expectedLength)
    for (index <- downDiagonal.indices) {
      downDiagonal(index).cellType should be(s"${x + index}${y - index}")
    }
  }
  "does not allow wrong downwards diagonals" in {
    assertThrows[IllegalArgumentException] {
      diverseGrid.getDownDiagonal(1, 1)
    }
    assertThrows[IllegalArgumentException] {
      diverseGrid.getDownDiagonal(0, -1)
    }
    assertThrows[IllegalArgumentException] {
      diverseGrid.getDownDiagonal(2, 0)
    }
    assertThrows[IllegalArgumentException] {
      diverseGrid.getDownDiagonal(0, Grid.WIDTH)
    }
  }
  "returns rows" in {
    val rows = diverseGrid.getRows
    rows.length should be(Grid.WIDTH)
    rows(0).length should be(Grid.WIDTH)
    for (i <- 0 until Grid.WIDTH; j <- 0 until Grid.WIDTH) {
      rows(j)(i).cellType should be(s"$i$j")
    }
  }
  "returns columns" in {
    val columns = diverseGrid.getColumns
    columns.length should be(Grid.WIDTH)
    columns(0).length should be(Grid.WIDTH)
    for (i <- 0 until Grid.WIDTH; j <- 0 until Grid.WIDTH) {
      columns(i)(j).cellType should be(s"$i$j")
    }
  }
  "returns upwards diagonals" in {
    val upDiagonals = diverseGrid.getUpDiagonals
    upDiagonals.length should be(Grid.WIDTH * 2 - 1)
    for (diagonal <- upDiagonals.indices) {
      val x = if (diagonal < Grid.WIDTH) diagonal else 0
      val y = if (diagonal >= Grid.WIDTH) diagonal - Grid.WIDTH + 1 else 0
      for (index <- upDiagonals(diagonal).indices) {
        upDiagonals(diagonal)(index).cellType should be(s"${x + index}${y + index}")
      }
    }
  }
  "returns downwards diagonals" in {
    val downDiagonals = diverseGrid.getDownDiagonals
    downDiagonals.length should be(Grid.WIDTH * 2 - 1)
    for (diagonal <- downDiagonals.indices) {
      val x = if (diagonal < Grid.WIDTH) diagonal else 0
      val y = if (diagonal >= Grid.WIDTH) diagonal - Grid.WIDTH + 1 else Grid.WIDTH - 1
      for (index <- downDiagonals(diagonal).indices) {
        downDiagonals(diagonal)(index).cellType should be(s"${x + index}${y - index}")
      }
    }
  }
  "Builds from a Sequence" in {
    val customGrid = Grid.fromSeq(Seq.fill(6)(Seq.fill(6)(new TestCell)))
    customGrid.forEachCell(_.cellType should be(TestCell.STANDARD_TYPE))
  }
}
