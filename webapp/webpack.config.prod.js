const {webProd} = require("@fun-stack/fun-pack"); // https://github.com/fun-stack/fun-pack/blob/master/src/webpack.config.web.prod.js

// https://github.com/fun-stack/fun-pack
module.exports = webProd({
  indexHtml: "src/main/html/index.html",
  assetsDir: "assets"
});
