#!/bin/sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
BUILD_DIR=$(mktemp -d)
trap 'rm -rf "$BUILD_DIR"' EXIT HUP INT TERM

javac -d "$BUILD_DIR" \
    "$SCRIPT_DIR/Wildfire.java" \
    "$SCRIPT_DIR/driver.java" \
    "$SCRIPT_DIR/WildfireTest.java"

java -ea -cp "$BUILD_DIR" WILDFIRE.WildfireTest
