package example.webapp

import colibri.Subject
import outwatch._
import outwatch.dsl._

object App {
  val pageHeader = {
    def link(name: String, page: Page): VDomModifier = {
      val styling = Page.current.map {
        case `page` => cls := "btn-neutral"
        case _      => cls := "btn-ghost"
      }

      a(cls := "btn", name, page.href, styling)
    }

    header(
      cls := "navbar shadow-lg",
      div(
        cls := "space-x-2",
        link("Home", Page.Home),
        link("Page 2", Page.Page2),
      ),
      div(
        cls := "ml-auto",
        Auth.loginControls,
      ),
    )
  }

  val pageFooter =
    footer(
      cls := "p-5 footer bg-base-200 text-base-content footer-center",
      div(
        cls := "flex flex-row space-x-4",
        a(cls := "link link-hover", "About us"),
        a(cls := "link link-hover", "Contact"),
      ),
    )

  val pageBody = div(
    cls := "p-10 mb-auto",
    // client-side router depending on the path in the address bar
    Page.current.map {
      case Page.Home  =>
        div(
          cls := "space-y-4",
          Components.httpApi,
          Components.websocketApi,
        )
      case Page.Page2 =>
        div(
          "Something on Page 2",
        )
    },
  )

  val layout = div(
    cls := "flex flex-col h-screen",
    pageHeader,
    pageBody,
    pageFooter,
  )
}

object Components {
  import example.api.HttpApi

  val httpApi = div(
    // example of rendering an async call directly
    // https://outwatch.github.io/docs/readme.html#rendering-futures
    // https://outwatch.github.io/docs/readme.html#rendering-async-effects
    b("My books: "),
    HttpClient
      .booksListing((HttpApi.BooksFromYear("drama", 2011), 10))
      .map(_.toString),
  )

  val websocketApi = {
    val currentRandomNumber = Subject.behavior[Option[Int]](None)

    VDomModifier(
      div(
        // example of rendering an async call directly
        // https://outwatch.github.io/docs/readme.html#rendering-futures
        // https://outwatch.github.io/docs/readme.html#rendering-async-effects
        b("Get Number: "),
        WsClient.api.getNumber,
      ),
      div(
        // https://outwatch.github.io/docs/readme.html#dynamic-content
        b("Call: "),
        button(
          cls := "btn btn-primary btn-sm",
          "New Random Number",
          onClick.useAsync(WsClient.api.getRandomNumber).map(Some.apply) --> currentRandomNumber,
        ),
        currentRandomNumber,
      ),
      div(
        // incoming events from the websocket
        b("Events:"),
        WsClient.ws.events.map(div(_)).scanToList,
      ),
    )
  }
}
