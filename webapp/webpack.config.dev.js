const { webDev } = require("@fun-stack/fun-pack"); // https://github.com/fun-stack/fun-pack/blob/master/src/webpack.config.web.dev.js

module.exports = webDev({
  indexHtml: "src/main/html/index.html",
  assetsDir: "assets",
  extraWatchDirs: [
    "local", // frontend with local backend
    "src/css",
    // "../terraform/.terraform/modules/example/serve/" // frontend with deployed backend
  ],
  extraStaticDirs: [
    "src", // src for source maps
  ],
});
