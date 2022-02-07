package example.webapp

import colibri.Subject
import colibri.router._

sealed trait Page {
  final def href = outwatch.dsl.href := s"#${Page.toPath(this).pathString}"
}

object Page {
  case object Home  extends Page
  case object Page2 extends Page

  object Paths {
    val Home  = Root
    val Page2 = Root / "page2"
  }

  val fromPath: Path => Page = {
    case Paths.Page2 => Page.Page2
    case _           => Page.Home
  }

  val toPath: Page => Path = {
    case Page.Page2 => Paths.Page2
    case Page.Home  => Paths.Home
  }

  val current: Subject[Page] = Router.path
    .imapSubject[Page](Page.toPath)(Page.fromPath)
}
