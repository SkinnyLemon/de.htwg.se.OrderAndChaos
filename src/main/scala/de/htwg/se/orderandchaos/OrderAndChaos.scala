package de.htwg.se.orderandchaos

import com.google.inject.{Guice, Injector}
import de.htwg.se.orderandchaos.control.Control
import de.htwg.se.orderandchaos.view.TUI

object OrderAndChaos {
  val injector: Injector = Guice.createInjector(new OacModule)
  val control: Control = injector.getInstance(classOf[Control])
  val tui: TUI = new TUI(control)

  def main(args: Array[String]): Unit = {
    tui.interpretLines()
  }
}
