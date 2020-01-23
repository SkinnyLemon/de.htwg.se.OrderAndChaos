package de.htwg.se.orderandchaos

import com.google.inject.AbstractModule
import de.htwg.se.orderandchaos.control.file.FileManager
import de.htwg.se.orderandchaos.control.file.json.JsonFileManager
import de.htwg.se.orderandchaos.control.file.xml.XmlFileManager
import de.htwg.se.orderandchaos.control.{Control, ControlImpl}
import net.codingwell.scalaguice.ScalaModule

class OacModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    val fileManager = new XmlFileManager
    bind[FileManager].toInstance(fileManager)
    bind[Control].toInstance(new ControlImpl(fileManager = fileManager))
  }
}
