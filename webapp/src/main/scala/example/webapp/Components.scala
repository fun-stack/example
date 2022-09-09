package example.webapp

import colibri.Subject
import example.api
import outwatch.dsl._
import funstack.web.tapir

object Components {
  import example.api.HttpApi

  val httpApi = div(
    h2("Http Api", cls := "text-xl"),
    div(
      // openapi with tapir
      b("My books: "),
      span(
        cls := "tapir-result",
        tapir.Fun.http
          .client(HttpApi.booksListing)((HttpApi.BooksFromYear("drama", 2011), 10))
          .map(_.toString),
      ),
    ),
  )

  def RpcApi = {
    val currentRandomNumber = Subject.behavior[Option[Int]](None)

    div(
      h2("Http Rpc Api", cls := "text-xl"),
      div(
        // example of rendering an async call directly
        // https://outwatch.github.io/docs/readme.html#rendering-futures
        // https://outwatch.github.io/docs/readme.html#rendering-async-effects
        b("Number to string via api call: "),
        span(HttpClient.api.numberToString(3), cls := "http-rpc-number-to-string"),
      ),
      div(
        // example of dynamic content with EmitterBuilder (onClick), IO (asEffect), and Subject/Observable/Observer (currentRandomNumber)
        // https://outwatch.github.io/docs/readme.html#dynamic-content
        div(
          b("Current random number: "),
          span(currentRandomNumber),
        ),
        button(
          "Get New Random Number from API",
          onClick.asEffect(HttpClient.api.getRandomNumber).map(Some.apply) --> currentRandomNumber,
          cls := "btn btn-primary btn-sm",
        ),
      ),
    )
  }

  def websocketRpcApi = {
    val currentRandomNumber = Subject.behavior[Option[Int]](None)

    div(
      h2("Websocket Rpc Api", cls := "text-xl"),
      div(
        // example of rendering an async call directly
        // https://outwatch.github.io/docs/readme.html#rendering-futures
        // https://outwatch.github.io/docs/readme.html#rendering-async-effects
        b("Sum via api call: "),
        span(WsClient.api.scale(api.Point(1, 4), 3).map(_.toString), cls := "websocket-rpc-scaled-point"),
      ),
      div(
        // example of dynamic content with EmitterBuilder (onClick), IO (asEffect), and Subject/Observable/Observer (currentRandomNumber)
        // https://outwatch.github.io/docs/readme.html#dynamic-content
        div(
          b("Current random number: "),
          span(currentRandomNumber),
        ),
        button(
          "Get New Random Number from API",
          onClick.asEffect(WsClient.api.getRandomNumber).map(Some.apply) --> currentRandomNumber,
          cls := "btn btn-primary btn-sm",
          cls := "websocket-rpc-new-random-number-button",
        ),
      ),
    )
  }

  def websocketEvents =
    div(
      h2("Websocket Events", cls := "text-xl"),
      div(
        // incoming events from the websocket
        div("(press random number button)", cls := "text-gray-500"),
        div(
          WsClient.eventApi.myMessages.map(div(_)).scanToList,
          cls := "websocket-event-list",
        ),
      ),
    )

}
