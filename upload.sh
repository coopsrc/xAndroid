#!/usr/bin/env bash

./gradlew :utils:clean :utils:build :utils:bintrayUpload -PdryRun=false
./gradlew :dewdrops-blur:clean :dewdrops-blur:build :dewdrops-blur:bintrayUpload -PdryRun=false
./gradlew :adaptive:clean :adaptive:build :adaptive:bintrayUpload -PdryRun=false
./gradlew :http-monitor:clean :http-monitor:build :http-monitor:bintrayUpload -PdryRun=false
./gradlew :http:clean :http:build :http:bintrayUpload -PdryRun=false
./gradlew :downloader:clean :downloader:build :downloader:bintrayUpload -PdryRun=false