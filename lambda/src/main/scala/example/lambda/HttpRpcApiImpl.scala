package example.lambda

import cats.effect.IO
import example.api.HttpRpcApi
import funstack.lambda.http.rpc.Handler

class HttpRpcApiImpl(request: Handler.Request) extends HttpRpcApi[IO] {
  def numberToString(number: Int): IO[String] = IO { number.toString }

  def getRandomNumber: IO[Int] = IO {
    val userId = request.auth.map(_.sub)
    // val userAttrs = userId.traverse(Fun.auth.getUser(_))
    println(s"Request from userId: ${userId}")

    scala.util.Random.nextInt(1000)
  }
}
