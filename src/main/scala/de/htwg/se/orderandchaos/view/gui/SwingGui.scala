package de.htwg.se.orderandchaos.view.gui

import de.htwg.se.orderandchaos.control.{CellSet, Control, Win}
import de.htwg.se.orderandchaos.model.grid.Grid

import scala.swing._
import scala.util.{Failure, Success}

class SwingGui(control: Control) extends Frame {
  listenTo(control)
  title = "Order and Chaos"
  menuBar = new MenuBar {
    contents += new Menu("Edit") {
      contents += new MenuItem(Action("Undo") {
        control.undo()
      })
      contents += new MenuItem(Action("Redo") {
        control.redo()
      })
      contents += new MenuItem(Action("Reset") {
        control.reset()
      })
    }
    contents += new Menu("File") {
      contents += new MenuItem(Action("Save") {
        control.save()
      })
      contents += new MenuItem(Action("Load") {
        control.load()
      })
    }
  }
  contents = update()
  visible = true
  repaint()

  def update(): BoxPanel = {
    val locked = !control.controller.isOngoing
    val grid = control.controller.grid
    val cells: Vector[CellPanel] =
      (for (y <- Grid.WIDTH - 1 to 0 by -1; x <- 0 until Grid.WIDTH)
        yield new CellPanel(x, y, control, locked))
        .toVector
    val gridPanel = new GridPanel(Grid.WIDTH, Grid.WIDTH) {
      contents ++= cells
    }
    new BoxPanel(Orientation.Vertical) {
      contents += menuBar
      contents += new Label(control.controller.header)
      contents += gridPanel
    }
  }

  reactions += {
    case _: CellSet => contents = update()
      repaint()
    case _: Win => contents = update()
      repaint()
  }
}
