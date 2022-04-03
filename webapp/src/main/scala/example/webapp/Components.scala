package example.webapp

import example.api.Api
import colibri.Subject
import outwatch.VModifier
import outwatch.dsl._
import funstack.web.tapir
import cats.effect.IO

object Components {
  import example.api.HttpApi

  def renderApi(api: Api[IO]): VModifier = {
    val currentRandomNumber = Subject.behavior[Option[Int]](None)

    VModifier(
      div(
        // example of rendering an async call directly
        // https://outwatch.github.io/docs/readme.html#rendering-futures
        // https://outwatch.github.io/docs/readme.html#rendering-async-effects
        b("Number: "),
        api.numberToString(3),
      ),
      div(
        // example of dynamic content with EmitterBuilder (onClick), IO (asEffect), and Subject/Observable/Observer (currentRandomNumber)
        // https://outwatch.github.io/docs/readme.html#dynamic-content
        b("Call: "),
        button(
          cls := "btn btn-primary btn-sm",
          "New Random Number",
          onClick.asEffect(api.getRandomNumber).map(Some.apply) --> currentRandomNumber,
        ),
        currentRandomNumber,
      ),
    )
  }

  val httpApi = div(
    cls := "text-lg",
    "Http",
    div(
      // openapi with tapir
      b("My books: "),
      tapir.Fun.http
        .client(HttpApi.booksListing)((HttpApi.BooksFromYear("drama", 2011), 10))
        .map(_.toString),
    ),
    renderApi(HttpClient.api),
  )

  val websocketApi = div(
    cls := "text-lg",
    "Ws",
    renderApi(WsClient.api),
    div(
      // incoming events from the websocket
      b("Events (ws):"),
      WsClient.streamsApi.logs.map(div(_)).scanToList,
    ),
  )
}
