package example.lambda

import cats.data.Kleisli
import cats.effect.IO
import example.api.{HttpRpcApi, WebsocketEventApi}
import funstack.backend.Fun
import funstack.lambda.http.rpc.Handler
import sloth.Client

import java.nio.ByteBuffer

class HttpRpcApiImpl(request: Handler.Request) extends HttpRpcApi[IO] {
  def numberToString(number: Int): IO[String] = IO { number.toString }

  def getRandomNumber: IO[Int] = IO {
    // val userId = request.auth.map(_.sub)
    // val userAttrs = userId.traverse(Fun.auth.getUser(_))

    scala.util.Random.nextInt(1000)
  }
}
