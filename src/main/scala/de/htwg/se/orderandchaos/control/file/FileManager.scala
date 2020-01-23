package de.htwg.se.orderandchaos.control.file

import de.htwg.se.orderandchaos.control.controller.Controller

trait FileManager {
  def saveToFile(controller: Controller): Unit

  def loadFromFile: Controller
}

object FileManager {
  val CONTROLLER_STANDARD_TYPE = "standard"
  val CONTROLLER_GAME_OVER_TYPE = "game-over"
  val SAVE_FILE_PATH = "save"
}
