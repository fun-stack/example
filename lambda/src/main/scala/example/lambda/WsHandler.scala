package example.lambda

import example.api.WsApi

import funstack.lambda.ws.Handler

import boopickle.Default._
import chameleon.ext.boopickle._

import java.nio.ByteBuffer

import scala.scalajs.js

object WsHandler {
  val router = sloth.Router[ByteBuffer, Handler.IOKleisli].route[WsApi[Handler.IOKleisli]](WsApiImpl)

  @js.annotation.JSExportTopLevel("handlerWs")
  val handler = Handler.handleKleisli(router)
}
