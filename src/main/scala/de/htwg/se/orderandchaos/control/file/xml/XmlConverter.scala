package de.htwg.se.orderandchaos.control.file.xml

import de.htwg.se.orderandchaos.control.controller.Controller
import de.htwg.se.orderandchaos.model.cell.Cell
import de.htwg.se.orderandchaos.model.grid.Grid

import scala.util.Success
import scala.xml.{Elem, Node}

object XmlConverter {
  def convertToController(xmlRepresentation: Elem): Controller = {
    val turn = (xmlRepresentation \\ "turn").text
    val xmlRows = (xmlRepresentation \\ "grid" \\ "row")
    val cellRows = xmlRows.map(convertToCells)
    val grid = Grid.fromSeq(cellRows)
    Controller.getOngoing(grid, turn)
  }

  def convertToCells(xmlRow: Node): Vector[Cell] = {
    val xmlCells = (xmlRow \\ "cell")
    xmlCells.map(xmlCell => Cell.ofType((xmlCell \ "@type").text) match {
      case Success(cell) => cell
    }).toVector
  }

  def convertToXml(controller: Controller): Elem =
    <game>
      <turn>{controller.turn}</turn>
      <grid>
        {
        controller.grid.getRows match {
          case Success(row) => row.map(rowToXml)
        }
        }
      </grid>
    </game>

  def rowToXml(row: Vector[Cell]): Elem =
    <row>
      {
        row.map(cell =>
          <cell type={cell.cellType}></cell>)
      }
    </row>
}
