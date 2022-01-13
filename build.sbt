val versions = new {
  val outwatch = "1.0.0-RC5"
  val colibri  = "0.2.2"
  val funStack = "0.1.6"
  val tapir    = "0.19.3"
  val funPack  = "0.1.4"
}

// This build is for this Giter8 template.
// To test the template run `g8` or `g8Test` from the sbt session.
// See http://www.foundweekends.org/giter8/testing.html#Using+the+Giter8Plugin for more details.
lazy val root = (project in file("."))
  .enablePlugins(ScriptedPlugin)
  .aggregate(scalaStewardUpdater)
  .settings(
    name                := "funstack",
    Test / test         := {
      val _ = (Test / g8Test).toTask("").value
    },
    scriptedLaunchOpts ++= List("-Xms1024m", "-Xmx1024m", "-XX:ReservedCodeCacheSize=128m", "-Xss2m", "-Dfile.encoding=UTF-8"),
    resolvers           += Resolver.url("typesafe", url("https://repo.typesafe.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns),
  )

lazy val scalaStewardUpdater = project
  .enablePlugins(ScalaJSPlugin)
  .disablePlugins(Giter8Plugin, Giter8TemplatePlugin)
  .settings(
    scalaVersion         := "2.13.7",
    // replicate all dependencies here, so scala-steward can update them
    libraryDependencies ++= Seq(
      "io.github.fun-stack"         %%% "fun-stack-web"          % versions.funStack,
      "io.github.fun-stack"         %%% "fun-stack-lambda-http"  % versions.funStack,
      "io.github.outwatch"          %%% "outwatch"               % versions.outwatch,
      "com.github.cornerman"        %%% "colibri-router"         % versions.colibri,
      "com.softwaremill.sttp.tapir" %%% "tapir-core"             % versions.tapir,
      "com.softwaremill.sttp.tapir" %%% "tapir-json-circe"       % versions.tapir,
      "org.portable-scala"          %%% "portable-scala-reflect" % "1.1.1",
    ),
  )
