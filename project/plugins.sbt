addSbtPlugin("org.foundweekends.giter8" %% "sbt-giter8" % "0.13.1")
libraryDependencies += { "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value }

// replicating all template plugins, so scala-steward can update them:
// scala-js
addSbtPlugin("org.scala-js"  % "sbt-scalajs"         % "1.8.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.20.0")

// https://github.com/ScalablyTyped/Converter/pull/377
resolvers                                 += MavenRepository("sonatype-s01-snapshots", "https://s01.oss.sonatype.org/content/repositories/snapshots")
addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta36+29-f3b7dd30-SNAPSHOT")

addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.20")
