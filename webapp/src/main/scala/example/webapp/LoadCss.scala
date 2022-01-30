package example.webapp

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object LoadCss {
  @js.native
  @JSImport("src/main/css/index.css", JSImport.Namespace)
  object Css extends js.Object

  @js.native
  @JSImport("src/main/css/tailwind.css", JSImport.Namespace)
  object TailwindCss extends js.Object

  def apply(): Unit = {
    TailwindCss
    Css
    ()
  }

}
