package example.lambda

import funstack.lambda.http.Handler

import scala.scalajs.js

object HttpHandler {
  @js.annotation.JSExportTopLevel("handlerHttp")
  val handler = Handler.handleKleisli(HttpApiImpl.endpoints)
}
