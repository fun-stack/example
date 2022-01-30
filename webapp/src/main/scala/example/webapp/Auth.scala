package example.webapp

import funstack.web.Fun
import outwatch.VDomModifier
import outwatch.dsl._

object Auth {
  val blueButton = button(cls := "btn btn-primary")

  val loginControls = Fun.auth
    .fold[VDomModifier](span("no auth configured"))(auth =>
      auth.currentUser.map {
        case Some(user) => blueButton(s"Logout (${user.info.email})", onClick.doAsync(auth.logout))
        case None       => blueButton("Login", onClick.doAsync(auth.login))
      },
    )

}
