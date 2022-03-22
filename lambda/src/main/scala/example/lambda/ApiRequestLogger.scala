package example.lambda

import funstack.lambda.http
import sloth.LogHandler

object ApiRequestLogger extends LogHandler[http.rpc.Handler.IOKleisli] {
  implicit val executionContext = org.scalajs.macrotaskexecutor.MacrotaskExecutor
  def logRequest[A, T](
    path: List[String],
    argumentObject: A,
    result: http.rpc.Handler.IOKleisli[T],
  ): http.rpc.Handler.IOKleisli[T] = {
    println(s"-> ${fansi.Color.Yellow(path.mkString("."))}(${argumentObject})")
    result.tapWith { (_, res) =>
      print(s"<- ")
      pprint.pprintln(res)
      res
    }
  }
}
