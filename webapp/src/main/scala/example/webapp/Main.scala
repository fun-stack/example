package example.webapp

import cats.implicits._
import cats.effect.{IO, IOApp}
import outwatch.OutWatch

import funstack.web.Fun

object Main extends IOApp.Simple {
  LoadCss()

  override def run =
    Fun.ws.start *> OutWatch.renderInto[IO]("#app", App.layout)
}
