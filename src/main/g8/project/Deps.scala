import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Deps {
  import Def.{setting => dep}

  // testing
  val scalatest = dep("org.scalatest" %%% "scalatest" % "3.2.9")

  // core libraries
  val cats = new {
    val core   = dep("org.typelevel" %%% "cats-core" % "2.7.0")
    val effect = dep("org.typelevel" %%% "cats-effect" % "2.5.4")
  }

  // web app
  val outwatch = new {
    private val version = "1.0.0-RC4"
    val core            = dep("io.github.outwatch" %%% "outwatch" % version)
  }
  val colibri = new {
    private val version = "0.1.2"
    val router          = dep("com.github.cornerman" %%% "colibri-router" % version)
  }
  val portableScala = dep("org.portable-scala" %%% "portable-scala-reflect" % "1.1.1")

  val tapir = new {
    private val version = "0.19.0"
    val core            = dep("com.softwaremill.sttp.tapir" %%% "tapir-core" % version)
    val jsonCirce       = dep("com.softwaremill.sttp.tapir" %%% "tapir-json-circe" % version)
  }
  val funstack = new {
    private val version = "0.1.4"
    val web             = dep("io.github.fun-stack" %%% "fun-stack-web" % version)
    val lambdaHttp      = dep("io.github.fun-stack" %%% "fun-stack-lambda-http" % version)
  }
}

object NpmDeps {
  val snabbdom = "snabbdom" -> "git://github.com/outwatch/snabbdom.git#semver:0.7.5"

  val tailwindForms      = "@tailwindcss/forms"      -> "^0.2.1"
  val tailwindTypography = "@tailwindcss/typography" -> "^0.4.0"

  val nodeFetch       = "node-fetch"               -> "2.6.1"
  val fetchHeaders    = "fetch-headers"            -> "2.0.0"

  val funpack = "@fun-stack/fun-pack" -> "0.1.4"

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
