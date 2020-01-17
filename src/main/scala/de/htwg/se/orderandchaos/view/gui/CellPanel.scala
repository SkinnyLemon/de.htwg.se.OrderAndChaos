package de.htwg.se.orderandchaos.view.gui

import de.htwg.se.orderandchaos.control.Control
import de.htwg.se.orderandchaos.model.cell.Cell

import scala.swing.event.{MouseClicked, MouseEntered, MouseExited, MouseMoved}
import scala.swing.{BoxPanel, Color, Dimension, Orientation, Swing}

class CellPanel(x: Int, y: Int, control: Control, isLocked: Boolean) extends BoxPanel(Orientation.Horizontal) {
  val gameCell: Cell = control.controller.grid(x, y)

  if (!isLocked && gameCell.isEmpty) contents += redChoice += blueChoice
  preferredSize = new Dimension(50, 50)
  background =
    if (gameCell.isBlue) CellPanel.BLUE
    else if (gameCell.isRed) CellPanel.RED
    else if (isLocked) CellPanel.LOCKED
    else CellPanel.EMPTY
  border = Swing.BeveledBorder(Swing.Raised)

  def redChoice: BoxPanel = choice(() => control.playRed(x + 1, y + 1), CellPanel.RED)

  def blueChoice: BoxPanel = choice(() => control.playBlue(x + 1, y + 1), CellPanel.BLUE)

  def choice(choose: () => Unit, choiceColor: Color): BoxPanel = new BoxPanel(Orientation.Horizontal) {
    preferredSize = new Dimension(20, 40)
    background = CellPanel.EMPTY
    border = Swing.BeveledBorder(Swing.Raised)
    listenTo(mouse.clicks)
    listenTo(mouse.moves)
    reactions += {
      case MouseClicked(_, _, _, _, _) =>
        choose()
      case MouseEntered(_, _, _) =>
        background = choiceColor
        repaint()
      case MouseExited(_, _, _) =>
        background = CellPanel.EMPTY
        repaint()
    }
  }
}

object CellPanel {
  val RED = new Color(255, 0, 0)
  val BLUE = new Color(0, 0, 255)
  val EMPTY = new Color(255, 255, 255)
  val LOCKED = new Color(150, 150, 150)
}
