#!/bin/bash

./gradlew assembleRelease
find . -name '*.apk' | xargs -n 1  dirname | sort | uniq | xargs open
