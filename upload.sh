#!/usr/bin/env bash

./gradlew :utils:clean :utils:build :utils:bintrayUpload -PdryRun=false
./gradlew :widget:clean :widget:build :widget:bintrayUpload -PdryRun=false
./gradlew :adaptive:clean :adaptive:build :adaptive:bintrayUpload -PdryRun=false
./gradlew :selector:clean :selector:build :selector:bintrayUpload -PdryRun=false
./gradlew :androlua:clean :androlua:build :androlua:bintrayUpload -PdryRun=false
./gradlew :dewdrops:clean :dewdrops:build :dewdrops:bintrayUpload -PdryRun=false
./gradlew :particle:clean :particle:build :particle:bintrayUpload -PdryRun=false
./gradlew :http:clean :http:build :http:bintrayUpload -PdryRun=false
./gradlew :http-monitor:clean :http-monitor:build :http-monitor:bintrayUpload -PdryRun=false
./gradlew :downloader:clean :downloader:build :downloader:bintrayUpload -PdryRun=false