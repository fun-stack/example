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
}

object NpmDeps {
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
