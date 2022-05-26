package example.webapp

import cats.effect.{IO, IOApp}
import outwatch.Outwatch

import funstack.web.Fun
import scala.scalajs.js

object Main extends IOApp.Simple {
  js.`import`("src/main/css/index.css")

  override def run =
    Fun.ws.start &> Outwatch.renderInto[IO]("#app", App.layout)
}
