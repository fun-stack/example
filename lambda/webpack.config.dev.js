const {commonConfig} = require('./webpack.config.common.js');
const {webDev} = require("@fun-stack/fun-pack");
const {merge} = require("webpack-merge");

module.exports = merge(
  // https://github.com/fun-stack/fun-pack
  webDev({
    // commented out because CopyWebpackPlugin is defined here to exclude the sprite files.
    // indexHtml: "src/main/html/index.html",
  assetsDir: "assets",
  extraWatchDirs: [
    "local", // frontend with local backend
    "src/css",
    // "../terraform/.terraform/modules/nocode/serve/" // frontend with deployed backend
  ],
  extraStaticDirs: [
    "src" // src for source maps
  ]
  }),
  commonConfig
);
