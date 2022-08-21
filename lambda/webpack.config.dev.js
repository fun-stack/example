const {lambdaDev} = require("@fun-stack/fun-pack"); // https://github.com/fun-stack/fun-pack/blob/master/src/webpack.config.lambda.dev.js
const webpack = require('webpack');

const config = lambdaDev();

module.exports = config;
