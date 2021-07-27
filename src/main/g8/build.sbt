import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

Global / onChangedBuildSource := IgnoreSourceChanges

inThisBuild(
  Seq(
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.6",
  ),
)

lazy val commonSettings = Seq(
  addCompilerPlugin(
    "org.typelevel" % "kind-projector" % "0.13.0" cross CrossVersion.full,
  ),
  resolvers ++=
    ("jitpack" at "https://jitpack.io") ::
      Nil,
  libraryDependencies ++=
    Deps.scalatest.value % Test ::
      Nil,

  /* scalacOptions --= Seq("-Xfatal-warnings"), */
)

lazy val jsSettings = Seq(
  useYarn := true,
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  webpack / version := "4.46.0",
  Compile / npmDevDependencies += NpmDeps.funpack,
  Compile / npmDevDependencies ++= NpmDeps.Dev,
)

val funStackVersion = "d5306f5"

lazy val webSettings = Seq(
  scalaJSUseMainModuleInitializer := true,
  Test / requireJsDomEnv := true,
  startWebpackDevServer / version := "3.11.2",
  webpackDevServerExtraArgs := Seq("--color"),
  webpackDevServerPort := 12345,
  fastOptJS / webpackConfigFile := Some(
    baseDirectory.value / "webpack.config.dev.js",
  ),
  fullOptJS / webpackConfigFile := Some(
    baseDirectory.value / "webpack.config.prod.js",
  ),
  fastOptJS / webpackBundlingMode := BundlingMode.LibraryOnly(),
  libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.1.1",
)

lazy val webClient = project
  .enablePlugins(
    ScalaJSPlugin,
    ScalaJSBundlerPlugin,
    ScalablyTypedConverterPlugin,
  )
  .dependsOn(apiHttp.js)
  .in(file("web-client"))
  .settings(commonSettings, jsSettings, webSettings)
  .settings(
    fullOptJS / webpackEmitSourceMaps := false,
    libraryDependencies ++= Seq(
      Deps.outwatch.core.value,
      "com.github.cornerman.fun-stack-scala" %%% "fun-stack-web" % funStackVersion,
      "com.github.cornerman.colibri" %%% "colibri-router" % "f118a37",
    ),
    Compile / npmDependencies ++=
      NpmDeps.tailwindForms ::
        NpmDeps.tailwindTypography ::
        ("snabbdom" -> "git://github.com/outwatch/snabbdom.git#semver:0.7.5") ::
        Nil,
    stIgnore ++=
      "@tailwindcss/forms" ::
        "@tailwindcss/typography" ::
        "snabbdom" ::
        Nil,
  )

lazy val apiHttp = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("api-http"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++=
      "com.softwaremill.sttp.tapir"   %%% "tapir-core"       % "0.18.0-M15" ::
        "com.softwaremill.sttp.tapir" %%% "tapir-json-circe" % "0.18.0-M15" ::
        Nil,
  )

lazy val lambdaHttp = project
  .enablePlugins(
    ScalaJSPlugin,
    ScalablyTypedConverterPlugin,
  )
  .in(file("lambda-http"))
  .settings(commonSettings, jsSettings)
  .dependsOn(apiHttp.js)
  .settings(
    /* scalaJSLinkerConfig ~= { _.withOptimizer(false) }, */
    webpackEmitSourceMaps in fullOptJS := false,
    webpackConfigFile in fullOptJS := Some(
      baseDirectory.value / "webpack.config.prod.js",
    ),
    libraryDependencies ++= Seq(
      "com.github.cornerman.fun-stack-scala" %%% "fun-stack-lambda-http"                % funStackVersion,
    ),
  )

addCommandAlias("dev", "devInit; devWatchAll; devDestroy") // watch all
addCommandAlias("devInit", "webClient/fastOptJS::startWebpackDevServer")
addCommandAlias("devWatchAll", "~; webClient/fastOptJS::webpack")
addCommandAlias("devDestroy", "webClient/fastOptJS::stopWebpackDevServer")
