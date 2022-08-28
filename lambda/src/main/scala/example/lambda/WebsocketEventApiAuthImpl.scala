package example.lambda

import example.api.WebsocketEventApi

import funstack.lambda.ws.eventauthorizer.Handler

import cats.data.Kleisli

object WebsocketEventApiAuthImpl extends WebsocketEventApi[Handler.IOKleisli] {
  def logs: Handler.IOKleisli[String] = Kleisli { case (request @ _, event @ _) =>
    // cats.effect.IO.pure(request.auth.isDefined)
    cats.effect.IO.pure(true)
  }
}
