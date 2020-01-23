package de.htwg.se.orderandchaos.control.file.json

import java.io.{File, PrintWriter}

import de.htwg.se.orderandchaos.control.controller.Controller
import de.htwg.se.orderandchaos.control.file.FileManager
import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object JsonFileManager {
  val SAVE_FILE_PATH = s"${FileManager.SAVE_FILE_PATH}.json"
}

class JsonFileManager extends FileManager {
  override def saveToFile(controller: Controller): Unit = {
    val jsonRepresentation = JsonConverter.convertToJson(controller)
    val stringRepresentation = Json.prettyPrint(jsonRepresentation)
    val writer = new PrintWriter(new File(JsonFileManager.SAVE_FILE_PATH))
    writer.write(stringRepresentation)
    writer.close()
  }

  override def loadFromFile: Controller = {
    val source: Source = Source.fromFile(JsonFileManager.SAVE_FILE_PATH)
    val stringRepresentation: String = source.getLines.mkString
    val jsonRepresentation: JsValue = Json.parse(stringRepresentation)
    JsonConverter.convertToController(jsonRepresentation)
  }
}
