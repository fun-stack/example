import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Deps {
  import Def.{setting => dep}

  // testing
  val scalatest = dep("org.scalatest" %%% "scalatest" % "3.2.9")

  // core libraries
  val cats = new {
    val core   = dep("org.typelevel" %%% "cats-core" % "2.1.1")
    val effect = dep("org.typelevel" %%% "cats-effect" % "2.3.0")
  }

  // web app
  val outwatch = new {
    private val version = "af8a62a6"
    val core            = dep("com.github.cornerman.outwatch" %%% "outwatch" % version)
  }
  val colibri = new {
    private val version = "f118a37"
    val router          = dep("com.github.cornerman.colibri" %%% "colibri-router" % version)
  }
  val portableScala = dep("org.portable-scala" %%% "portable-scala-reflect" % "1.1.1")

  val tapir = new {
    private val version = "0.18.0-M15"
    val core      = dep("com.softwaremill.sttp.tapir"   %%% "tapir-core"       % version)
    val jsonCirce = dep("com.softwaremill.sttp.tapir" %%% "tapir-json-circe" % "0.18.0-M15")
  }
  val funstack = new {
    private val version = "d5306f5" 
    val web = dep("com.github.cornerman.fun-stack-scala" %%% "fun-stack-web" % version)
    val lambdaHttp = dep("com.github.cornerman.fun-stack-scala" %%% "fun-stack-lambda-http" % version)
  }
}

object NpmDeps {
  val snabbdom = "snabbdom" -> "git://github.com/outwatch/snabbdom.git#semver:0.7.5"

  val tailwindForms      = "@tailwindcss/forms"      -> "^0.2.1"
  val tailwindTypography = "@tailwindcss/typography" -> "^0.4.0"

  val nodeFetch       = "node-fetch"               -> "2.6.1"
  val abortController = "abortcontroller-polyfill" -> "1.5.0"

  val funpack = "fun-pack" -> "git://github.com/fun-stack-org/fun-pack#c51221a"

  val Dev =
    "autoprefixer"          -> "10.2.5" ::
      "postcss"             -> "8.2.9" ::
      "postcss-loader"      -> "4.2.0" ::
      "postcss-import"      -> "14.0.1" ::
      "postcss-nesting"     -> "7.0.1" ::
      "postcss-extend-rule" -> "3.0.0" ::
      "tailwindcss"         -> "2.1.1" ::
      Nil
}
