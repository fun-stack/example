const {lambdaProd} = require('@fun-stack/fun-pack');

// https://github.com/fun-stack/fun-pack
module.exports = lambdaProd({
  entrypoint: "target/scala-2.13/lambda-opt/main.js",
  outputDir: "target/dist/"
});
