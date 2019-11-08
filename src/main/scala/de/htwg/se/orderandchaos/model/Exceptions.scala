package de.htwg.se.orderandchaos.model

class OacException(message: String) extends IllegalArgumentException(message)
class CommandParsingException(message: String) extends OacException(message)
class IllegalOverrideException extends OacException("Already set fields cannot be overridden")
class NoMoreMovesException extends OacException("No moves left to get")
