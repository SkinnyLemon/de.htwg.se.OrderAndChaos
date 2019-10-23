package de.htwg.se.orderandchaos.model

class Field private (val fieldType: String) {
   override def toString:String = fieldType

   def setType(fieldType: String): Field = {
      if (fieldType != Field.BLUE && fieldType != Field.RED) {
         throw new IllegalArgumentException("Field needs to be set to RED or BLUE")
      }
      new Field(fieldType)
   }
}

object Field {
   val EMPTY = "Empty"
   val BLUE = "Blue"
   val RED = "Red"
   def empty: Field = new Field(EMPTY)
   def blue: Field = new Field(BLUE)
   def red: Field = new Field(RED)
}
