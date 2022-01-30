package example.webapp

import cats.effect.IO
import outwatch.OutWatch

object Main {
  LoadCss()

  implicit val executionContext = org.scalajs.macrotaskexecutor.MacrotaskExecutor

  def main(args: Array[String]): Unit =
    OutWatch.renderInto[IO]("#app", App.layout).unsafeRunSync()
}
