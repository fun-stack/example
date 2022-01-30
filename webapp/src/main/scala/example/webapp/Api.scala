package example.webapp

import boopickle.Default._
import cats.effect.IO
import chameleon.ext.boopickle._
import example.api.{HttpApi, WsApi}
import funstack.web.Fun

import java.nio.ByteBuffer

object WsClient {
  val ws     = Fun.wsWithEvents[String].get
  val client = ws.client[ByteBuffer]

  val api = client.wire[WsApi[IO]]
}

object HttpClient {
  val http = Fun.http.get

  val booksListing = http.client(HttpApi.booksListing)
}
