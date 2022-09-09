package example.webapp

import cats.effect.IO
import example.api.{EventApi, RpcApi}
import colibri.Observable
import sloth.Client
import funstack.web.Fun

import chameleon.ext.circe._

object WsRpcClient {
  val client          = Client(Fun.ws.transport[String])
  val api: RpcApi[IO] = client.wire[RpcApi[IO]]

  val eventClient                    = Client(Fun.ws.streamsTransport[String])
  val eventApi: EventApi[Observable] = eventClient.wire[EventApi[Observable]]
}

object HttpRpcClient {
  val client          = Client(Fun.http.transport[String])
  val api: RpcApi[IO] = client.wire[RpcApi[IO]]
}
