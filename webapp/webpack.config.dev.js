const {webDev} = require("@fun-stack/fun-pack");

// https://github.com/fun-stack/fun-pack
module.exports = webDev({
  indexHtml: "src/main/html/index.html",
  assetsDir: "assets",
  extraWatchDirs: [
    "local" // frontend with local backend
    // "../terraform/.terraform/modules/example/serve/" // frontend with deployed backend
  ],
  extraStaticDirs: [
    "src" // src for source maps
  ]
});
