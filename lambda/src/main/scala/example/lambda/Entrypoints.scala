package example.lambda

import cats.effect.IO
import example.api.{HttpRpcApi, WebsocketApi, WebsocketEventApi}
import funstack.lambda.{http, ws}
import sloth.Router

import chameleon.ext.circe._

import scala.scalajs.js

object Entrypoints {
  @js.annotation.JSExportTopLevel("httpApi")
  val httpApi = http.api.tapir.Handler.handleKleisli(
    HttpApiImpl.endpoints,
  )

  @js.annotation.JSExportTopLevel("httpRpc")
  val httpRpc = http.rpc.Handler.handle { request: http.rpc.Handler.Request =>
    Router[String, IO](new ApiRequestLogger[IO])
      .route[HttpRpcApi[IO]](new HttpRpcApiImpl(request))
  }

  @js.annotation.JSExportTopLevel("wsRpc")
  val wsRpc = ws.rpc.Handler.handle[String] { request: ws.rpc.Handler.Request =>
    Router[String, IO](new ApiRequestLogger[IO])
      .route[WebsocketApi[IO]](new WebsocketApiImpl(request))
  }

  @js.annotation.JSExportTopLevel("wsEventAuth")
  val wsEventAuth = ws.eventauthorizer.Handler.handleKleisli(
    Router
      .contra[String, ws.eventauthorizer.Handler.IOKleisli]
      .route[WebsocketEventApi[ws.eventauthorizer.Handler.IOKleisli]](WebsocketEventApiAuthImpl),
  )
}
