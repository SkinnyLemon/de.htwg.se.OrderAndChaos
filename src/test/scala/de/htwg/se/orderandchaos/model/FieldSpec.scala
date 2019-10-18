package de.htwg.se.orderandchaos.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FieldSpec extends WordSpec with Matchers {
  "A Field" when { "new" should {
    val field = Field("Your Value")
    "have a value"  in {
      field.value should be("Your Value")
    }
    "have a nice String representation" in {
      field.toString should be("Your Value")
    }
  }}
}
