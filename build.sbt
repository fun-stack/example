Global / onChangedBuildSource := IgnoreSourceChanges // not working well with webpack devserver

ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

val versions = new {
  val outwatch  = "1.0.0-RC7"
  val colibri   = "0.5.0"
  val funStack  = "0.6.1"
  val tapir     = "1.0.0-M7"
  val boopickle = "1.4.0"
  val pprint    = "0.7.3"
}

lazy val commonSettings = Seq(
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),

  // overwrite option from https://github.com/DavidGregory084/sbt-tpolecat
  scalacOptions --= Seq("-Xfatal-warnings"),
  scalacOptions --= Seq("-Xcheckinit"), // produces check-and-throw code on every val access
)

lazy val jsSettings = Seq(
  scalaJSLinkerConfig ~= { _.withOptimizer(false) },
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.1.2",
  externalNpm         := {
    sys.process.Process("yarn", baseDirectory.value).!
    baseDirectory.value
  },
)

lazy val webapp = project
  .enablePlugins(
    ScalaJSPlugin,
    ScalablyTypedConverterExternalNpmPlugin,
  )
  .dependsOn(api)
  .settings(commonSettings, jsSettings)
  .settings(
    libraryDependencies            ++= Seq(
      "io.github.outwatch"   %%% "outwatch"            % versions.outwatch,
      "io.github.fun-stack"  %%% "fun-stack-web"       % versions.funStack,
      "io.github.fun-stack"  %%% "fun-stack-web-tapir" % versions.funStack, // this pulls in scala-java-time, which will drastically increase the javascript bundle size. Remove if not needed.
      "com.github.cornerman" %%% "colibri-router"      % versions.colibri,
      "io.suzaku"            %%% "boopickle"           % versions.boopickle,
    ),
    stIgnore                       ++= List(
      "reconnecting-websocket",
      "snabbdom",
      "jwt-decode",
    ),
    scalaJSUseMainModuleInitializer := true,
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
    ScalablyTypedConverterExternalNpmPlugin,
  )
  .dependsOn(api)
  .settings(commonSettings, jsSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.github.fun-stack" %%% "fun-stack-lambda-ws-event-authorizer" % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-lambda-ws-rpc"              % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-lambda-http-rpc"            % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-lambda-http-api-tapir"      % versions.funStack,
      "io.github.fun-stack" %%% "fun-stack-backend"                    % versions.funStack,
      "io.suzaku"           %%% "boopickle"                            % versions.boopickle,
      "com.lihaoyi"         %%% "pprint"                               % versions.pprint,
    ),
    stIgnore            ++= List(
      "aws-sdk",
    ),
  )

addCommandAlias("prod", "fullOptJS")
addCommandAlias("prodf", "webapp/fullOptJS")
addCommandAlias("prodb", "lambda/fullOptJS")
addCommandAlias("dev", "~; lambda/fastOptJS; webapp/fastOptJS")
addCommandAlias("devf", "~; webapp/fastOptJS")
addCommandAlias("devb", "~; lambda/fastOptJS")
