const {lambdaDev} = require('@fun-stack/fun-pack');

// https://github.com/fun-stack/fun-pack
module.exports = lambdaDev({
  entrypoint: "target/scala-2.13/lambda-fastopt/main.js",
  outputDir: "target/dev/"
});
