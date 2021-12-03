// scala-js
addSbtPlugin("org.scala-js"                % "sbt-scalajs"              % "1.7.1")
addSbtPlugin("ch.epfl.scala"               % "sbt-scalajs-bundler"      % "0.20.0")
addSbtPlugin("org.portable-scala"          % "sbt-scalajs-crossproject" % "1.1.0")

resolvers += MavenRepository("sonatype-s01-snapshots", "https://s01.oss.sonatype.org/content/repositories/snapshots")
addSbtPlugin("org.scalablytyped.converter" % "sbt-converter"            % "1.0.0-beta36+29-f3b7dd30-SNAPSHOT")

addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.19")
