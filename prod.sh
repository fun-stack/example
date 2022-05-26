#!/usr/bin/env bash
set -Eeuo pipefail # https://vaneyckt.io/posts/safer_bash_scripts_with_set_euxo_pipefail

echo "Checking for node..." && node --version
echo "Checking for yarn..." && yarn --version
echo "Checking for sbt..." && sbt --script-version

sbt prod

(cd lambda && yarn install && npx webpack \
    bundle \
    --config webpack.config.prod.js \
)

(cd webapp && yarn install && npx webpack \
    bundle \
    --config webpack.config.prod.js \
)
