package example.lambda

import example.api.HttpApi

import funstack.lambda.http.Handler
import funstack.backend.Fun

import cats.effect.IO
import cats.data.Kleisli
import cats.implicits._

import boopickle.Default._
import chameleon.ext.boopickle._

object HttpApiImpl {
  private val ws = Fun.wsWithEvents[String].get

  val booksListingImpl = HttpApi.booksListing.serverLogic[Handler.IOKleisli] { case (_, _) =>
    Kleisli { req =>
      println(req)
      val userId = req.auth.map(_.sub)

      val sendEvent = userId.traverse(userId => ws.sendToUser(userId = userId, data = s"Http Request of $userId!")).void
      val response  = IO.pure(Right(List(HttpApi.Book("Programming in Scala"))))

      sendEvent *> response
    }
  }

  val endpoints = List(
    booksListingImpl,
  )
}
