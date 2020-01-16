package de.htwg.se.orderandchaos.util

import org.scalatest.{Matchers, WordSpec}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

object ExceptionChecker extends WordSpec with Matchers  {
  val standardWrongFailureError = "threw wrong kind of exception"
  def checkTry[E <: Throwable](toTest: Try[Any], successError: String, wrongFailureError: String = standardWrongFailureError)(implicit tag: ClassTag[E]): Unit = toTest match {
    case Success(_) => fail(successError)
    case Failure(_: E) =>
    case Failure(e) => fail(s"$wrongFailureError\n$e")
  }
  def checkTryF0[E <: Throwable](toTest: () => Try[Any], successError: String, wrongFailureError: String = standardWrongFailureError)(implicit tag: ClassTag[E]): () => Unit =
    () => checkTry[E](toTest(), successError, wrongFailureError)
  def checkTryF1[E <: Throwable, T](toTest: T => Try[Any], successError: String, wrongFailureError: String = standardWrongFailureError)(implicit tag: ClassTag[E]): T => Unit =
    v1 => checkTry[E](toTest(v1), successError, wrongFailureError)
  def checkTryF2[E <: Throwable, T](toTest: (T, T) => Try[Any], successError: String, wrongFailureError: String = standardWrongFailureError)(implicit tag: ClassTag[E]): (T, T) => Unit =
    (v1, v2) => checkTry[E](toTest(v1, v2), successError, wrongFailureError)
}
