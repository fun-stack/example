package example.lambda

import funstack.lambda.{http, ws}
import sloth.LogHandler

import scala.scalajs.js

//class ApiRequestLogger[X[_]](implicit ev: X[*] =:= Kleisli[IO, RequestOf[Any], *]) extends LogHandler[X] {
//  implicit val executionContext = org.scalajs.macrotaskexecutor.MacrotaskExecutor
//  def logRequest[ARG, RES](
//    path: List[String],
//    argumentObject: ARG,
//    result: X[RES],
//  ): X[RES] = {
//    println(s"-> ${fansi.Color.Yellow(path.mkString("."))}(${argumentObject})")
//    result.tapWith { (_, res) =>
//      print(s"<- ")
//      pprint.pprintln(res)
//      res
//    }
//  }
//}

object HttpApiRequestLogger extends LogHandler[http.rpc.Handler.IOKleisli] {
  def logRequest[ARG, RES](
    path: List[String],
    argumentObject: ARG,
    result: http.rpc.Handler.IOKleisli[RES],
  ): http.rpc.Handler.IOKleisli[RES] = {
    val args = if (argumentObject.asInstanceOf[js.UndefOr[_]] == js.undefined) "" else argumentObject
    println(s"-> ${fansi.Color.Yellow(path.mkString("."))}($args)")
    result.tapWith { (_, res) =>
      print(s"<- ")
      pprint.pprintln(res)
      res
    }
  }
}

object WsApiRequestLogger extends LogHandler[ws.rpc.Handler.IOKleisli] {
  def logRequest[ARG, RES](
    path: List[String],
    argumentObject: ARG,
    result: ws.rpc.Handler.IOKleisli[RES],
  ): ws.rpc.Handler.IOKleisli[RES] = {
    val args = if (argumentObject.asInstanceOf[js.UndefOr[_]] == js.undefined) "" else argumentObject
    println(s"-> ${fansi.Color.Yellow(path.mkString("."))}($args)")
    result.tapWith { (_, res) =>
      print(s"<- ")
      pprint.pprintln(res)
      res
    }
  }
}
