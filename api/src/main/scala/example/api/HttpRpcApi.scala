package example.api

trait HttpRpcApi[F[_]] {
  def numberToString(number: Int): F[String]
  def getRandomNumber: F[Int]
}
