package de.htwg.se.orderandchaos.control.file.xml

import java.io.{File, PrintWriter}

import de.htwg.se.orderandchaos.control.controller.Controller
import de.htwg.se.orderandchaos.control.file.FileManager

import scala.xml.{Elem, PrettyPrinter, XML}

object XmlFileManager {
  val SAVE_FILE_PATH = s"${FileManager.SAVE_FILE_PATH}.xml"
}

class XmlFileManager extends FileManager {
  override def saveToFile(controller: Controller): Unit = {
    val xmlRepresentation = XmlConverter.convertToXml(controller)
    val stringRepresentation = new PrettyPrinter(120, 5).format(xmlRepresentation)
    val writer = new PrintWriter(new File(XmlFileManager.SAVE_FILE_PATH))
    writer.write(stringRepresentation)
    writer.close()
  }

  override def loadFromFile: Controller = {
    val xmlRepresentation: Elem = XML.loadFile(XmlFileManager.SAVE_FILE_PATH)
    XmlConverter.convertToController(xmlRepresentation)
  }
}
