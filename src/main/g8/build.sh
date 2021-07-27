#/usr/bin/env bash

cd "\$(dirname \$0)"

sbt webClient/fullOptJS::webpack lambdaHttp/fullOptJS::webpack
