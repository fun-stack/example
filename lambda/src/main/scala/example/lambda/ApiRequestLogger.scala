package example.lambda

import cats.Functor
import cats.data.Kleisli
import sloth.LogHandler

class ApiRequestLogger[X[_] <: Kleisli[X, _, _]: Functor] extends LogHandler[X] {
  implicit val executionContext = org.scalajs.macrotaskexecutor.MacrotaskExecutor
  def logRequest[ARG, T](
    path: List[String],
    argumentObject: ARG,
    result: Kleisli[X, _, T],
  ): Kleisli[X, _, T] = {
    println(s"-> ${fansi.Color.Yellow(path.mkString("."))}(${argumentObject})")
    result.tapWith { (_, res) =>
      print(s"<- ")
      pprint.pprintln(res)
      res
    }
  }
}
