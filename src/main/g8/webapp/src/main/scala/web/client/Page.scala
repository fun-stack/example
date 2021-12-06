package example.client

import colibri.Subject
import colibri.router._

sealed trait Page
object Page {
  case object Landing   extends Page
  case object Somewhere extends Page

  val current: Subject[Page] = Router.path
    .imapSubject[Page] {
      case Page.Somewhere => Root / "somewhere"
      case Page.Landing   => Root
    } {
      case Root / "somewhere" => Page.Somewhere
      case _                  => Page.Landing
    }
}
