package example.lambda

import cats.data.Kleisli
import cats.effect.IO
import example.api.{WebsocketApi, WebsocketEventApi}
import funstack.backend.Fun
import funstack.lambda.ws.rpc.Handler
import sloth.Client

import java.nio.ByteBuffer
import boopickle.Default._
import chameleon.ext.boopickle._

class WebsocketApiImpl(request: Handler.Request) extends WebsocketApi[IO] {
  private val client     = Client.contra(Fun.ws.sendTransport[ByteBuffer])
  private val streamsApi = client.wire[WebsocketEventApi[Kleisli[IO, *, Unit]]]

  def numberToString(number: Int) = IO { number.toString }

  def getRandomNumber = {
    val userId = request.auth.map(_.sub)
    // val userAttrs = userId.traverse(Fun.auth.getUser(_))

    val sendEvent = streamsApi.logs.apply(s"Api Request by ${userId}!")
    val response  = IO(scala.util.Random.nextInt(1000))

    sendEvent *> response
  }
}
