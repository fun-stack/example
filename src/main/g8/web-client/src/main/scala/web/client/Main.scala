package $name;format="camel"$.client

import outwatch._
import cats.effect.IO
import cats.effect.ExitCode
import cats.effect.IOApp

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSImport("../../../../src/main/css/index.css", JSImport.Namespace)
object Css extends js.Object

@js.native
@JSImport("../../../../src/main/css/tailwind.css", JSImport.Namespace)
object TailwindCss extends js.Object

object Main extends IOApp {
  TailwindCss
  Css // load css

  def run(args: List[String]) = {
    Outwatch.renderInto[IO]("#app", UI.root).as(ExitCode.Success)
  }
}
