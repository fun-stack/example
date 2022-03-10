#!/usr/bin/env bash
set -Eeuo pipefail # https://vaneyckt.io/posts/safer_bash_scripts_with_set_euxo_pipefail

trap "trap - SIGTERM && kill -- -$$ && echo '\\n\\n'" SIGINT SIGTERM EXIT # kill background jobs on exit

echo "Checking for node..." && node --version
echo "Checking for yarn..." && yarn --version
echo "Checking for sbt..." && sbt --script-version

echo "Checking if ports can be opened..."
nc -z 127.0.0.1  8080 &>/dev/null && (echo "Port 8080 is already in use";  exit 1)
nc -z 127.0.0.1  8081 &>/dev/null && (echo "Port 8081 is already in use";  exit 1)
nc -z 127.0.0.1  8082 &>/dev/null && (echo "Port 8082 is already in use";  exit 1)
nc -z 127.0.0.1 12345 &>/dev/null && (echo "Port 12345 is already in use"; exit 1)


prefix() (
  prefix="$1"
  color="$2"
  colored_prefix="[$(tput setaf "$color")$prefix$(tput sgr0)] "

  # flushing awk: https://unix.stackexchange.com/a/83853
  awk -v prefix="$colored_prefix" '{ print prefix $0; system("") }'
)

yarn install

npx fun-local-env \
    --auth 8082 \
    --ws 8001 \
    --http 8080 \
    --http-api lambda/target/scala-2.13/scalajs-bundler/main/lambda-fastopt.js httpApi \
    --http-rpc lambda/target/scala-2.13/scalajs-bundler/main/lambda-fastopt.js httpRpc \
    --ws-rpc lambda/target/scala-2.13/scalajs-bundler/main/lambda-fastopt.js wsRpc \
    --ws-event-authorizer lambda/target/scala-2.13/scalajs-bundler/main/lambda-fastopt.js wsEventAuth \
    | prefix "BACKEND" 4 &

sbt dev shell
printf "\n"


