const { webDev } = require("@fun-stack/fun-pack"); // https://github.com/fun-stack/fun-pack/blob/master/src/webpack.config.web.dev.js

module.exports = webDev({
  indexHtml: "src/main/html/index.html",
  assetsDir: "assets",
  extraStatic: [
    {
      publicPath: "/",
      directory: "local",
      watch: true,
    },
    {
      publicPath: "/",
      directory: "src/css",
      watch: true,
    },
    // serve scala sources of all sbt subprojects for source maps
    {
      directory: "../webapp", 
      publicPath: "/",
      watch: false,
    }
  ]
});
