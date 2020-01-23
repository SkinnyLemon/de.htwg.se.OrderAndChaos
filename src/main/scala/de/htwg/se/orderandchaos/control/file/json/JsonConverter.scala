package de.htwg.se.orderandchaos.control.file.json

import de.htwg.se.orderandchaos.control.controller.Controller
import de.htwg.se.orderandchaos.control.file.FileManager
import de.htwg.se.orderandchaos.model.JsonParsingException
import de.htwg.se.orderandchaos.model.cell.Cell
import de.htwg.se.orderandchaos.model.grid.Grid
import play.api.libs.json._

import scala.util.{Failure, Success}

object JsonConverter {
  implicit val cellWrites: Writes[Cell] = (cell: Cell) => Json.obj("type" -> cell.cellType)
  implicit val gridWrites: Writes[Grid] = (grid: Grid) => Json.obj("fields" -> (grid.getRows match {
    case Success(rows) => rows.map(_.map(Json.toJson(_)))
    case Failure(exception) => throw exception
  }))
  implicit val controllerWrites: Writes[Controller] = (controller: Controller) => Json.obj(
    "type" -> Json.toJson(
      if (controller.isOngoing) FileManager.CONTROLLER_STANDARD_TYPE
      else FileManager.CONTROLLER_GAME_OVER_TYPE),
    "turn" -> Json.toJson(controller.turn),
    "grid" -> Json.toJson(controller.grid))

  implicit val cellReads: Reads[Cell] = (json: JsValue) => JsSuccess(Cell.ofType(validateString(json \ "type")) match {
    case Success(cell) => cell
    case Failure(exception) => throw exception
  })
  implicit val gridReads: Reads[Grid] = (json: JsValue) => (json \ "fields").get.validate[Seq[Seq[Cell]]] match {
    case JsSuccess(cells, _) => JsSuccess(Grid.fromSeq(cells))
    case e: JsError => throw new JsonParsingException("Grid", convertError(e))
  }
  implicit val controllerReads: Reads[Controller] = (json: JsValue) => {
    val turn = validateString(json \ "turn")
    val grid = convertToGrid((json \ "grid").get)
    val controllerType = validateString(json \ "type")
    if (controllerType equals FileManager.CONTROLLER_STANDARD_TYPE) JsSuccess(Controller.getOngoing(grid, turn))
    else if (controllerType equals FileManager.CONTROLLER_GAME_OVER_TYPE) JsSuccess(Controller.getFinished(grid, turn))
    else throw new JsonParsingException("Controller", "Invalid type")
  }

  def convertToJson(cell: Cell): JsValue = Json.toJson(cell)

  def convertToJson(grid: Grid): JsValue = Json.toJson(grid)

  def convertToJson(controller: Controller): JsValue = Json.toJson(controller)

  def convertToCell(json: JsValue): Cell = json.validate[Cell] match {
    case JsSuccess(cell, _) => cell
    case e: JsError => throw new JsonParsingException("Cell", convertError(e))
  }

  def convertToGrid(json: JsValue): Grid = json.validate[Grid] match {
    case JsSuccess(grid, _) => grid
    case e: JsError => throw new JsonParsingException("Grid", convertError(e))
  }

  def convertToController(json: JsValue): Controller = json.validate[Controller] match {
    case JsSuccess(controller, _) => controller
    case e: JsError => throw new JsonParsingException("Controller", convertError(e))
  }

  private def validateString(json: JsLookupResult): String = json.validate[String] match {
    case JsSuccess(str, _) => str
    case e: JsError => throw new JsonParsingException("String", convertError(e))
  }

  private def convertError(e: JsError): String = Json.prettyPrint(JsError.toJson(e))
}
