package de.htwg.se.orderandchaos.model

class OacException(message: String) extends IllegalArgumentException(message)
class CommandParsingException(message: String) extends OacException(message)
class IllegalOverrideException extends OacException("Already set fields cannot be overridden")
class NoMoreMovesException extends OacException("No moves left to get")
class MoveOnDecidedGameException extends OacException("Cannot do moves on an already decided game")
class InvalidCellTypeException extends OacException("Invalid cell type")
class JsonParsingException(objectType: String, error: String) extends OacException(s"Failed to parse $objectType:\n$error")
