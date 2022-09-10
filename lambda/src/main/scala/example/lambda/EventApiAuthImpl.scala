package example.lambda

import example.api.EventApi

import funstack.lambda.ws.eventauthorizer.Handler

import cats.data.Kleisli

object EventApiAuthImpl extends EventApi[Handler.IOKleisli] {
  def myMessages: Handler.IOKleisli[String] = Kleisli { case (request @ _, event @ _) =>
    // cats.effect.IO.pure(request.auth.isDefined)
    cats.effect.IO.pure(true)
  }
}
