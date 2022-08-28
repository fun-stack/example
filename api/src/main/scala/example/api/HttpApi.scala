package example.api

import io.circe.generic.JsonCodec
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

// example from: https://github.com/softwaremill/tapir

object HttpApi {
  object types {
    type Limit     = Int
    type AuthToken = String
  }
  import types._

  @JsonCodec case class BooksFromYear(genre: String, year: Int)
  @JsonCodec case class Book(title: String)

  val booksListing: PublicEndpoint[(BooksFromYear, Limit), String, List[Book], Any] =
    endpoint.get
      .in(("books" / path[String]("genre") / path[Int]("year")).mapTo[BooksFromYear])
      .in(query[Limit]("limit").description("Maximum number of books to retrieve"))
      .errorOut(stringBody)
      .out(jsonBody[List[Book]])
}
