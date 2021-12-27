addSbtPlugin("org.foundweekends.giter8" %% "sbt-giter8"     % "0.13.1")
libraryDependencies                     += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

// replicating all template plugins, so scala-steward can update them:
// scala-js
addSbtPlugin("org.scala-js"  % "sbt-scalajs"         % "1.8.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.20.0")

addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta37")

addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.20")
