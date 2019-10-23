package de.htwg.se.orderandchaos.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FieldSpec extends WordSpec with Matchers {
  "A Field" when { "new" should {
    val field = Field.empty
    "be empty"  in {
      field.fieldType should be(Field.EMPTY)
    }
    "have a nice String representation" in {
      field.toString should be(Field.EMPTY)
    }
  }}
  "can be red" in {
    Field.red.fieldType should be(Field.RED)
  }
  "can be blue" in {
    Field.blue.fieldType should be(Field.BLUE)
  }
}
