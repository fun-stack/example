package example.webapp

import cats.effect.IO
import example.api.{HttpRpcApi, WebsocketApi, WebsocketEventApi}
import colibri.Observable
import sloth.Client
import funstack.web.Fun

import java.nio.ByteBuffer
import boopickle.Default._
import chameleon.ext.boopickle._

object WsClient {
  val client                = Client(Fun.ws.transport[ByteBuffer])
  val api: WebsocketApi[IO] = client.wire[WebsocketApi[IO]]

  val eventClient                             = Client(Fun.ws.streamsTransport[ByteBuffer])
  val eventApi: WebsocketEventApi[Observable] = eventClient.wire[WebsocketEventApi[Observable]]
}

object HttpClient {
  val client              = Client(Fun.http.transport[ByteBuffer])
  val api: HttpRpcApi[IO] = client.wire[HttpRpcApi[IO]]
}
