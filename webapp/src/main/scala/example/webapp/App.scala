package example.webapp

import outwatch.VModifier
import outwatch.dsl._
import funstack.web.Fun

object App {

  // For styling, we use tailwindcss and daisyui:
  // - https://tailwindcss.com/ - basic styles like p-5, space-x-2, mb-auto, ...
  // - https://daisyui.com/ - based on tailwindcss with components like btn, navbar, footer, ...

  val pageHeader = {
    def link(name: String, page: Page): VModifier = {
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
        link("API", Page.Api),
      ),
      div(
        cls := "ml-auto",
        Fun.auth.currentUser.map {
          case Some(user) => a(cls := "btn btn-primary", s"Logout (${user.info.email})", href := Fun.auth.logoutUrl)
          case None       => a(cls := "btn btn-primary", "Login", href := Fun.auth.loginUrl)
        },
      ),
    )
  }

  val pageFooter =
    footer(
      cls := "p-5 footer bg-base-200 text-base-content footer-center",
      div(
        cls := "flex flex-row space-x-4",
        a(cls := "link link-hover", href := "#", "About us"),
        a(cls := "link link-hover", href := "#", "Contact"),
      ),
    )

  val pageBody = div(
    cls := "p-10 mb-auto",
    // client-side router depending on the path in the address bar
    Page.current.map {
      case Page.Home =>
        div(
          cls := "text-bold",
          "Welcome!",
        )
      case Page.Api  =>
        div(
          cls := "space-y-4",
          Components.httpApi,
          Components.websocketApi,
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
