package example.lambda

import example.api.WsApi

import funstack.backend.Fun
import funstack.lambda.ws.Handler

import cats.data.Kleisli
import cats.effect.IO

import boopickle.Default._
import chameleon.ext.boopickle._

object WsApiImpl extends WsApi[Handler.IOKleisli] {
  private val ws = Fun.ws[String].get

  def getNumber = Kleisli(_ => IO.pure(23))

  def getRandomNumber = Kleisli { req =>
    println(req)
    val userId       = req.auth.map(_.sub)
    val connectionId = req.event.requestContext.connectionId

    val sendEvent = userId match {
      case Some(userId) => ws.sendToUser(userId = userId, data = s"Ws Request of $userId!")
      case None         => ws.sendToConnection(connectionId = connectionId, data = "Ws Request of Anon!")
    }
    val response  = IO(scala.util.Random.nextInt(1000))

    sendEvent *> response
  }
}
