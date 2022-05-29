const {webDev} = require("@fun-stack/fun-pack");

// https://github.com/fun-stack/fun-pack
module.exports = webDev({
  entrypoint: "target/scala-2.13/webapp-fastopt/main.js",
  outputDir: "target/dev/",
  indexHtml: "src/main/html/index.html",
  assetsDir: "assets",
  extraWatchDirs: [
    "local" // local app config for frontend
  ],
  extraStaticDirs: [
    "src" // src for source maps
  ]
});
