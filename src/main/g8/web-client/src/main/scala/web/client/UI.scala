package $name;format="camel"$.client

import outwatch.dsl._
import $name;format="camel"$.apihttp._
import funstack.web.Fun
import colibri.Subject

object UI {
  val blueButton = button(cls := "bg-blue-500 text-white px-4 py-2 rounded-lg")
  val greyButton = button(cls := "bg-gray-100 text-black px-4 py-2")

  val loginControls = Fun.auth.map(auth =>
    div(
      cls := "mb-2",
      auth.currentUser.map {
        case Some(user) => blueButton(s"Logout (\${user.info.email})", onClick.doAsync(auth.logout))
        case None       => blueButton("Login", onClick.doAsync(auth.login))
      },
    ),
  )

  val httpApiButton = Fun.http.map { http =>
    val httpBooksClient = http.client(Api.booksListing)

    val result = Subject.publish[Either[String, List[Book]]]

    div(
      blueButton(
        "Call Api",
        onClick.useAsync(httpBooksClient((BooksFromYear("drama", 2011), 10))) --> result,
      ),
      result.map(_.toString),
    )
  }

  val menu = div(
    cls := "mb-6",
    greyButton(cls := "mr-4", "Home", onClick.use(Page.Landing) --> Page.current),
    greyButton(cls := "mr-4", "Somewhere", onClick.use(Page.Somewhere) --> Page.current),
  )

  val root = div(
    cls := "p-10",
    menu,
    Page.current.map {
      case Page.Landing   => div(loginControls, httpApiButton)
      case Page.Somewhere => div("Nice to see you")
    },
  )
}
