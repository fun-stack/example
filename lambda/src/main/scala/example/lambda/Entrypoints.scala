package example.lambda

import cats.effect.IO
import example.api.{EventApi, RpcApi}
import funstack.lambda.{http, ws}
import funstack.lambda.apigateway.Request
import sloth.Router

import chameleon.ext.circe._

import scala.scalajs.js

object Entrypoints {
  @js.annotation.JSExportTopLevel("httpApi")
  val httpApi = http.api.tapir.Handler.handleKleisli(
    HttpApiImpl.endpoints,
  )

  @js.annotation.JSExportTopLevel("httpRpc")
  val httpRpc = http.rpc.Handler.handle { request: Request =>
    Router[String, IO](new ApiRequestLogger[IO])
      .route[RpcApi[IO]](new RpcApiImpl(request))
  }

  @js.annotation.JSExportTopLevel("wsRpc")
  val wsRpc = ws.rpc.Handler.handle[String] { request: Request =>
    Router[String, IO](new ApiRequestLogger[IO])
      .route[RpcApi[IO]](new RpcApiImpl(request))
  }

  @js.annotation.JSExportTopLevel("wsEventAuth")
  val wsEventAuth = ws.eventauthorizer.Handler.handleKleisli(
    Router
      .contra[String, ws.eventauthorizer.Handler.IOKleisli]
      .route[EventApi[ws.eventauthorizer.Handler.IOKleisli]](EventApiAuthImpl),
  )
}
