addSbtPlugin("org.foundweekends.giter8" %% "sbt-giter8" % "0.13.1")
libraryDependencies += { "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value }

// to resolve the js dependencies for scala-steward
addSbtPlugin("org.scala-js"  % "sbt-scalajs"         % "1.7.1")
