Global / onChangedBuildSource := IgnoreSourceChanges // not working well with webpack devserver

ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

val versions = new {
  val outwatch = "1.0.0-RC5"
  val colibri  = "0.2.2"
  val funStack = "0.1.6"
  val tapir    = "0.19.0"
  val funPack  = "0.1.4"
}

lazy val commonSettings = Seq(
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
  scalacOptions --= Seq("-Xfatal-warnings"), // overwrite option from https://github.com/DavidGregory084/sbt-tpolecat
)

lazy val jsSettings = Seq(
  webpack / version   := "4.46.0",
  useYarn             := true,
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.1.1",
)

lazy val webapp = project
  .enablePlugins(
    ScalaJSPlugin,
    ScalaJSBundlerPlugin,
    ScalablyTypedConverterPlugin,
  )
  .dependsOn(api)
  .settings(commonSettings, jsSettings)
  .settings(
    libraryDependencies              ++= Seq(
      "io.github.outwatch"   %%% "outwatch"       % versions.outwatch,
      "io.github.fun-stack"  %%% "fun-stack-web"  % versions.funStack,
      "com.github.cornerman" %%% "colibri-router" % versions.colibri,
    ),
    Compile / npmDependencies        ++= Seq(
      "snabbdom" -> "github:outwatch/snabbdom.git#semver:0.7.5", // for outwatch, workaround for: https://github.com/ScalablyTyped/Converter/issues/293
    ),
    stIgnore                         ++= List(
      "snabbdom", // for outwatch, workaround for: https://github.com/ScalablyTyped/Converter/issues/293
    ),
    Compile / npmDevDependencies     ++= Seq(
      "@fun-stack/fun-pack" -> versions.funPack, // sane defaults for webpack development and production, see webpack.config.*.js
      "autoprefixer"        -> "10.2.5",
      "postcss"             -> "8.2.9",
      "postcss-loader"      -> "4.2.0",
      "tailwindcss"         -> "2.1.1",
    ),
    scalaJSUseMainModuleInitializer   := true,
    webpackDevServerPort              := 12345,
    webpackDevServerExtraArgs         := Seq("--color"),
    startWebpackDevServer / version   := "3.11.3",
    fullOptJS / webpackEmitSourceMaps := true,
    fastOptJS / webpackBundlingMode   := BundlingMode.LibraryOnly(),
    fastOptJS / webpackConfigFile     := Some(baseDirectory.value / "webpack.config.dev.js"),
    fullOptJS / webpackConfigFile     := Some(baseDirectory.value / "webpack.config.prod.js"),
  )

// shared project which contains api definitions.
// these definitions are used for type safe implementations
// of client and server
lazy val api = project
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %%% "tapir-core"       % versions.tapir,
      "com.softwaremill.sttp.tapir" %%% "tapir-json-circe" % versions.tapir,
    ),
  )

lazy val lambda = project
  .enablePlugins(
    ScalaJSPlugin,
    ScalaJSBundlerPlugin,
    ScalablyTypedConverterPlugin,
  )
  .dependsOn(api)
  .settings(commonSettings, jsSettings)
  .settings(
    libraryDependencies              ++= Seq(
      "io.github.fun-stack" %%% "fun-stack-lambda-http" % versions.funStack,
    ),
    Compile / npmDevDependencies     ++= Seq(
      "@fun-stack/fun-pack" -> versions.funPack, // sane defaults for webpack development and production, see webpack.config.*.js
    ),
    fullOptJS / webpackEmitSourceMaps := true,
    fastOptJS / webpackConfigFile     := Some(baseDirectory.value / "webpack.config.dev.js"),
    fullOptJS / webpackConfigFile     := Some(baseDirectory.value / "webpack.config.prod.js"),
  )

addCommandAlias("prod", "fullOptJS/webpack")
addCommandAlias("dev", "devInit; devWatchAll; devDestroy")
addCommandAlias("devf", "devInit; devWatchFrontend; devDestroy") // compile only frontend
addCommandAlias("devInit", "; lambda/fastOptJS/webpack; webapp/fastOptJS/startWebpackDevServer")
addCommandAlias("devWatchFrontend", "~; webapp/fastOptJS/webpack")
addCommandAlias("devWatchAll", "~; lambda/fastOptJS; webapp/fastOptJS/webpack")
addCommandAlias("devDestroy", "webapp/fastOptJS/stopWebpackDevServer")
