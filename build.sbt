Global / onChangedBuildSource := IgnoreSourceChanges // not working well with webpack devserver

ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"

Global / excludeLintKeys += webpackDevServerPort // TODO:

val versions = new {
  val outwatch = "1.0.0-RC17"
  val colibri  = "0.7.8"
  val funStack = "0.8.9"
  val tapir    = "1.8.2"
  val pprint   = "0.8.1"
}

// Uncomment, if you want to use snapshot dependencies from sonatype or jitpack
// ThisBuild / resolvers ++= Seq(
//   "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
//   "Sonatype OSS Snapshots S01" at "https://s01.oss.sonatype.org/content/repositories/snapshots", // https://central.sonatype.org/news/20210223_new-users-on-s01/
//   "Jitpack" at "https://jitpack.io",
// )

val enableFatalWarnings =
  sys.env.get("ENABLE_FATAL_WARNINGS").flatMap(value => scala.util.Try(value.toBoolean).toOption).getOrElse(false)

lazy val commonSettings = Seq(
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),

  // overwrite scalacOptions "-Xfatal-warnings" from https://github.com/DavidGregory084/sbt-tpolecat
  if (enableFatalWarnings) scalacOptions += "-Xfatal-warnings" else scalacOptions -= "-Xfatal-warnings",
  scalacOptions ++= Seq("-Ymacro-annotations", "-Vimplicits", "-Vtype-diffs"),
  scalacOptions --= Seq("-Xcheckinit"), // produces check-and-throw code on every val access
)

lazy val scalaJsSettings = Seq(
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.1.2",
) ++ scalaJsBundlerSettings ++ scalaJsMacrotaskExecutor ++ scalaJsSecureRandom

lazy val scalaJsBundlerSettings = Seq(
  webpack / version               := "5.75.0",
  webpackCliVersion               := "5.0.0",
  startWebpackDevServer / version := "4.11.1",
  useYarn                         := true,
)

lazy val scalaJsMacrotaskExecutor = Seq(
  // https://github.com/scala-js/scala-js-macrotask-executor
  libraryDependencies += "org.scala-js" %%% "scala-js-macrotask-executor" % "1.1.1",
)

lazy val scalaJsSecureRandom = Seq(
  // https://www.scala-js.org/news/2022/04/04/announcing-scalajs-1.10.0
  libraryDependencies += "org.scala-js" %%% "scalajs-java-securerandom" % "1.0.0",
)

def readJsDependencies(baseDirectory: File, field: String): Seq[(String, String)] = {
  val packageJson = ujson.read(IO.read(new File(s"$baseDirectory/package.json")))
  packageJson(field).obj.mapValues(_.str.toString).toSeq
}

lazy val webapp = project
  .enablePlugins(
    ScalaJSPlugin,
    ScalaJSBundlerPlugin,
    ScalablyTypedConverterPlugin,
  )
  .dependsOn(api)
  .settings(commonSettings, scalaJsSettings)
  .settings(
    Test / test := {}, // skip tests, since we don't have any in this subproject. Remove this line, once there are tests
    libraryDependencies ++= Seq(
      "io.github.outwatch"   %%% "outwatch"            % versions.outwatch,
      "io.github.fun-stack"  %%% "fun-stack-web"       % versions.funStack,
      "io.github.fun-stack"  %%% "fun-stack-web-tapir" % versions.funStack, // this pulls in scala-java-time, which will drastically increase the javascript bundle size. Remove if not needed.
      "com.github.cornerman" %%% "colibri-router"      % versions.colibri,
    ),
    Compile / npmDependencies ++= readJsDependencies(baseDirectory.value, "dependencies") ++ Seq(
      "snabbdom"               -> "github:outwatch/snabbdom.git#semver:0.7.5", // for outwatch, workaround for: https://github.com/ScalablyTyped/Converter/issues/293
      "reconnecting-websocket" -> "4.1.10",                                    // for fun-stack websockets, workaround for https://github.com/ScalablyTyped/Converter/issues/293 https://github.com/cornerman/mycelium/blob/6f40aa7018276a3281ce11f7047a6a3b9014bff6/build.sbt#74
      "jwt-decode"             -> "3.1.2",                                     // for fun-stack auth, workaround for https://github.com/ScalablyTyped/Converter/issues/293 https://github.com/cornerman/mycelium/blob/6f40aa7018276a3281ce11f7047a6a3b9014bff6/build.sbt#74
    ),
    stIgnore ++= List(
      "reconnecting-websocket",
      "snabbdom",
      "jwt-decode",
    ),
    Compile / npmDevDependencies   ++= readJsDependencies(baseDirectory.value, "devDependencies"),
    scalaJSUseMainModuleInitializer := true,
    webpackDevServerPort := sys.env
      .get("FRONTEND_PORT")
      .flatMap(port => scala.util.Try(port.toInt).toOption)
      .getOrElse(12345),
    webpackDevServerExtraArgs         := Seq("--color"),
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
    Test / test := {}, // skip tests, since we don't have any in this subproject. Remove this line, once there are tests
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
  .settings(commonSettings, scalaJsSettings, scalaJsBundlerSettings)
  .settings(
    Test / test := {}, // skip tests, since we don't have any in this subproject. Remove this line, once there are tests
    libraryDependencies ++= Seq(
      "io.github.fun-stack" %%% "fun-stack-lambda-ws-event-authorizer" % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-lambda-ws-rpc"              % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-lambda-http-rpc"            % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-lambda-http-api-tapir"      % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-backend"                    % versions.funStack,
      "com.lihaoyi"         %%% "pprint"                               % versions.pprint,
    ),
    Compile / npmDependencies ++= readJsDependencies(baseDirectory.value, "dependencies"),
    stIgnore ++= List(
      "aws-sdk",
    ),
    Compile / npmDevDependencies     ++= readJsDependencies(baseDirectory.value, "devDependencies"),
    fullOptJS / webpackEmitSourceMaps := true,
    fastOptJS / webpackEmitSourceMaps := true,
    fastOptJS / webpackConfigFile     := Some(baseDirectory.value / "webpack.config.dev.js"),
    fullOptJS / webpackConfigFile     := Some(baseDirectory.value / "webpack.config.prod.js"),
  )

addCommandAlias("prod", "; lambda/fullOptJS/webpack; webapp/fullOptJS/webpack")
addCommandAlias("prodf", "webapp/fullOptJS/webpack")
addCommandAlias("prodb", "lambda/fullOptJS/webpack")
addCommandAlias("dev", "devInitAll; devWatchAll; devDestroyFrontend")
addCommandAlias("devf", "devInitFrontend; devWatchFrontend; devDestroyFrontend") // compile only frontend
addCommandAlias("devb", "devInitBackend; devWatchBackend")                       // compile only backend

// devInitBackend needs to execute {...}/fastOptJS/webpack, to prepare all npm dependencies.
// We want to avoid this expensive preparation in the hot-reload process,
// and therefore only watch {...}/fastOptJS, where dependencies can be resolved from the previously prepared
// node_modules folder.
addCommandAlias("devInitBackend", "lambda/fastOptJS/webpack")
addCommandAlias("devInitFrontend", "webapp/fastOptJS/startWebpackDevServer; webapp/fastOptJS/webpack")
addCommandAlias("devInitAll", "devInitFrontend; devInitBackend")
addCommandAlias("devWatchFrontend", "~; webapp/fastOptJS")
addCommandAlias("devWatchBackend", "~; lambda/fastOptJS")
addCommandAlias("devWatchAll", "~; lambda/fastOptJS; webapp/fastOptJS; compile; Test/compile")
addCommandAlias("devDestroyFrontend", "webapp/fastOptJS/stopWebpackDevServer")
