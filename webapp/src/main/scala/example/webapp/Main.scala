package example.webapp

import cats.effect.{IO, IOApp}
import outwatch.Outwatch

import funstack.client.web.Fun

object Main extends IOApp.Simple {
  LoadCss()

  override def run =
    Fun.wsRpc.start &> Outwatch.renderInto[IO]("#app", App.layout)
}
