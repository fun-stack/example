package example.api

trait EventApi[F[_]] {
  def myMessages: F[String]
}
