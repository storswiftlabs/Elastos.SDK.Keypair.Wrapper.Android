#!/bin/bash - 

set -o errexit
set -o nounset

SCRIPT_DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd);
PROJECT_DIR=$(dirname "${SCRIPT_DIR}");
BUILD_DIR="$PROJECT_DIR/build";

PROJECT_NAME="ElastosOrgWalletLib";
PROJECT_BUILDTYPE="Release";

TARGET_PATH="$BUILD_DIR/${PROJECT_NAME}.aar";

cd "$PROJECT_DIR";
./gradlew :lib:assembleDebug
rm -rf "$TARGET_PATH/";
mkdir -p "$BUILD_DIR/";
cp "$PROJECT_DIR/lib/build/outputs/aar/lib-debug.aar" "$TARGET_PATH";

echo "Done!!!";

