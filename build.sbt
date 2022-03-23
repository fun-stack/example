import scalajsbundler.NpmDependencies

Global / onChangedBuildSource := IgnoreSourceChanges // not working well with webpack devserver

ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

val versions = new {
  val outwatch  = "1.0.0-RC6"
  val colibri   = "0.2.6"
  val funStack  = "0.5.2"
  val tapir     = "1.0.0-M1"
  val funPack   = "0.2.0"
  val boopickle = "1.4.0"
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

lazy val scalaJsMacrotaskExecutor = Seq(
  // https://github.com/scala-js/scala-js-macrotask-executor
  libraryDependencies       += "org.scala-js" %%% "scala-js-macrotask-executor" % "1.0.0",
  Compile / npmDependencies += "setimmediate"  -> "1.0.5", // polyfill
  stIgnore                  += "setimmediate",
)

lazy val webapp = project
  .enablePlugins(
    ScalaJSPlugin,
    ScalaJSBundlerPlugin,
    ScalablyTypedConverterPlugin,
  )
  .dependsOn(api)
  .settings(commonSettings, jsSettings, scalaJsMacrotaskExecutor)
  .settings(
    libraryDependencies              ++= Seq(
      "io.github.outwatch"   %%% "outwatch"            % versions.outwatch,
      "io.github.fun-stack"  %%% "fun-stack-web"       % versions.funStack,
      "io.github.fun-stack"  %%% "fun-stack-web-tapir" % versions.funStack,
      "com.github.cornerman" %%% "colibri-router"      % versions.colibri,
      "io.suzaku"            %%% "boopickle"           % versions.boopickle,
    ),
    Compile / npmDependencies        ++= Seq(
      "snabbdom"               -> "github:outwatch/snabbdom.git#semver:0.7.5", // for outwatch, workaround for: https://github.com/ScalablyTyped/Converter/issues/293
      "reconnecting-websocket" -> "4.1.10",                                    // for fun-stack websockets, workaround for https://github.com/ScalablyTyped/Converter/issues/293 https://github.com/cornerman/mycelium/blob/6f40aa7018276a3281ce11f7047a6a3b9014bff6/build.sbt#74
      "jwt-decode"             -> "3.1.2",                                     // for fun-stack auth, workaround for https://github.com/ScalablyTyped/Converter/issues/293 https://github.com/cornerman/mycelium/blob/6f40aa7018276a3281ce11f7047a6a3b9014bff6/build.sbt#74
    ),
    stIgnore                         ++= List(
      "reconnecting-websocket",
      "snabbdom",
      "jwt-decode",
    ),
    Compile / npmDevDependencies     ++= Seq(
      "@fun-stack/fun-pack" -> versions.funPack, // sane defaults for webpack development and production, see webpack.config.*.js
      "autoprefixer"        -> "^10.2.5",
      "postcss"             -> "^8.4.5",
      "tailwindcss"         -> "^3.0.10",
      "daisyui"             -> "^1.25.4",
    ),
    scalaJSUseMainModuleInitializer   := true,
    webpackDevServerPort              := 12345,
    webpackDevServerExtraArgs         := Seq("--color"),
    startWebpackDevServer / version   := "3.11.3",
    fullOptJS / webpackEmitSourceMaps := true,
    fastOptJS / webpackEmitSourceMaps := true,
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
  .settings(commonSettings, jsSettings, scalaJsMacrotaskExecutor)
  .settings(
    libraryDependencies              ++= Seq(
      "io.github.fun-stack" %%% "fun-stack-lambda-ws-event-authorizer" % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-lambda-ws-rpc"              % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-lambda-http-rpc"            % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-lambda-http-api-tapir"      % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-backend"                    % versions.funStack,
      "io.suzaku"           %%% "boopickle"                            % versions.boopickle,
    ),
    Compile / npmDependencies        ++= Seq(
      "aws-sdk" -> "2.892.0",
    ),
    stIgnore                         ++= List(
      "aws-sdk",
    ),
    Compile / npmDevDependencies     ++= Seq(
      "@fun-stack/fun-pack" -> versions.funPack, // sane defaults for webpack development and production, see webpack.config.*.js
    ),
    fullOptJS / webpackEmitSourceMaps := true,
    fastOptJS / webpackEmitSourceMaps := true,
    fastOptJS / webpackConfigFile     := Some(baseDirectory.value / "webpack.config.dev.js"),
    fullOptJS / webpackConfigFile     := Some(baseDirectory.value / "webpack.config.prod.js"),
  )

addCommandAlias("prod", "fullOptJS/webpack")
addCommandAlias("prodf", "webapp/fullOptJS/webpack")
addCommandAlias("prodb", "lambda/fullOptJS/webpack")
addCommandAlias("dev", "devInitAll; devWatchAll; devDestroyFrontend")
addCommandAlias("devf", "devInitFrontend; devWatchFrontend; devDestroyFrontend") // compile only frontend
addCommandAlias("devb", "devInitBackend; devWatchBackend")                       // compile only backend
addCommandAlias("devInitBackend", "lambda/fastOptJS/webpack")
addCommandAlias("devInitFrontend", "webapp/fastOptJS/startWebpackDevServer; webapp/fastOptJS/webpack")
addCommandAlias("devInitAll", "devInitFrontend; devInitBackend")
addCommandAlias("devWatchFrontend", "~; webapp/fastOptJS")
addCommandAlias("devWatchBackend", "~; lambda/fastOptJS")
addCommandAlias("devWatchAll", "~; lambda/fastOptJS; webapp/fastOptJS")
addCommandAlias("devDestroyFrontend", "webapp/fastOptJS/stopWebpackDevServer")
