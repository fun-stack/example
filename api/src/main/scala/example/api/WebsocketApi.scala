package example.api

import io.circe.generic.JsonCodec

trait WebsocketApi[F[_]] {
  def sum(numbers: Numbers): F[Int]
  def getRandomNumber: F[Int]
}

@JsonCodec case class Numbers(a: Int, b: Int)

trait WebsocketEventApi[F[_]] {
  def logs: F[String]
}
