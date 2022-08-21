const { webDev } = require("@fun-stack/fun-pack"); // https://github.com/fun-stack/fun-pack/blob/master/src/webpack.config.web.dev.js

// https://github.com/fun-stack/fun-pack
module.exports = webDev({
  // commented out because CopyWebpackPlugin is defined here to exclude the sprite files.
  // indexHtml: "src/main/html/index.html",
  assetsDir: "assets",
  extraWatchDirs: [
    "local", // frontend with local backend
    "src/css",
    // "../terraform/.terraform/modules/nocode/serve/" // frontend with deployed backend
  ],
  extraStaticDirs: [
    "src", // src for source maps
  ],
});
