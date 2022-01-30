package example.api

trait WsApi[F[_]] {
  def getNumber: F[Int]
  def getRandomNumber: F[Int]
}
