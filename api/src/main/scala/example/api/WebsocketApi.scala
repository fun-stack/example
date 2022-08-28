package example.api

trait WebsocketApi[F[_]] {
  def numberToString(number: Int): F[String]
  def getRandomNumber: F[Int]
}

trait WebsocketEventApi[F[_]] {
  def logs: F[String]
}
