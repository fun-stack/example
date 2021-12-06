const {webDev} = require("@fun-stack/fun-pack");

module.exports = webDev({
  indexHtml: "src/main/html/index.html",
  assetsDir: "assets",
  extraWatchDirs: [
    "local" // frontend with local backend
    // "../terraform/.terraform/modules/overtime-hivemind/serve/" // frontend with deployed backend
  ]
});
