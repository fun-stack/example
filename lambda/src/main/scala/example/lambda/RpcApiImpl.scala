package example.lambda

import cats.data.Kleisli
import cats.effect.IO
import example.api.{EventApi, Numbers, RpcApi}
import funstack.backend.Fun
import funstack.lambda.apigateway.Request
import sloth.Client

import chameleon.ext.circe._

class RpcApiImpl(request: Request) extends RpcApi[IO] {
  private val client     = Client.contra(Fun.ws.sendTransport[String])
  private val streamsApi = client.wire[EventApi[Kleisli[IO, *, Unit]]]

  override def numberToString(number: Int): IO[String] = IO(number.toString)

  override def sum(numbers: Numbers): IO[Int] = IO { numbers.a + numbers.b }

  override def getRandomNumber: IO[Int] = {
    val userId = request.auth.map(_.sub)
    // val userAttrs = userId.traverse(Fun.auth.getUser(_))

    val sendEvent = streamsApi.myMessages.apply(s"Api Request by ${userId}!")
    val response  = IO(scala.util.Random.nextInt(1000))

    sendEvent *> response
  }
}
