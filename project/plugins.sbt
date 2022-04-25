// scala-js
addSbtPlugin("org.scala-js"  % "sbt-scalajs"         % "1.10.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.20.0")

addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta37")

addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.3.0")
addSbtPlugin("org.scalameta"             % "sbt-scalafmt" % "2.4.6")

libraryDependencies ++= Seq("com.lihaoyi" %% "upickle" % "1.6.0")
