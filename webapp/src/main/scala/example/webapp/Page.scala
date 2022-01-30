package example.webapp

import colibri.Subject
import colibri.router._

sealed trait Page {
  final def href = outwatch.dsl.href := s"#${Page.toPath(this).pathString}"
}

object Page {
  case object Home  extends Page
  case object Page2 extends Page { val str = "page2" }

  val fromPath: Path => Page = {
    case Root / Page2.str => Page.Page2
    case _                => Page.Home
  }

  val toPath: Page => Path = {
    case Page.Page2 => Root / Page2.str
    case Page.Home  => Root
  }

  val current: Subject[Page] = Router.path
    .imapSubject[Page](Page.toPath)(Page.fromPath)
}
