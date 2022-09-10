package example.api

import io.circe.generic.JsonCodec

trait RpcApi[F[_]] {
  def numberToString(number: Int): F[String]
  def scale(point: Point, factor: Double): F[Point]
  def getRandomNumber: F[Int]
}

@JsonCodec case class Point(x: Double, y: Double)
