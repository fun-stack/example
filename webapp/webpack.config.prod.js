const {webProd} = require("@fun-stack/fun-pack");

// https://github.com/fun-stack/fun-pack
module.exports = webProd({
  entrypoint: "target/scala-2.13/webapp-opt/main.js",
  outputDir: "target/dist/",
  indexHtml: "src/main/html/index.html",
  assetsDir: "assets"
});
