package $name;format="camel"$.lambda

import scala.scalajs.js
import $name;format="camel"$.apihttp._
import funstack.lambda.http.Handler
import cats.effect.IO

object Lambda {
  val booksListingImpl = Api.booksListing.serverLogic[IO]({ case (_, _) => IO.pure(Right(Nil)) })

  val endpoints = List(
    booksListingImpl,
  )

  @js.annotation.JSExportTopLevel("handler")
  val handler = Handler.handle[IO](endpoints)
}
