package de.htwg.se.orderandchaos

import com.google.inject.AbstractModule
import de.htwg.se.orderandchaos.control.json.JsonFileManager
import de.htwg.se.orderandchaos.control.{Control, ControlImpl, FileManager}
import net.codingwell.scalaguice.ScalaModule

class OacModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Control].toInstance(new ControlImpl)
    bind[FileManager].toInstance(new JsonFileManager)
  }
}
