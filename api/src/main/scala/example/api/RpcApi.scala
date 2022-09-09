package example.api

import io.circe.generic.JsonCodec

trait RpcApi[F[_]] {
  def numberToString(number: Int): F[String]
  def sum(numbers: Numbers): F[Int]
  def getRandomNumber: F[Int]
}

@JsonCodec case class Numbers(a: Int, b: Int)
