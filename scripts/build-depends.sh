#!/bin/bash

set -o errexit
set -o nounset

CURRENT_DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd);
PROJECT_DIR=$(dirname "$CURRENT_DIR")
DEPENDS_DIR="$PROJECT_DIR/depends";

git -C "$PROJECT_DIR" submodule init;
git -C "$PROJECT_DIR" submodule update;

export PACKAGE_PLATFORM=Android;
"$DEPENDS_DIR/Elastos.ORG.Wallet.Lib.C/scripts/package-mobile.sh";

rm -rf "$PROJECT_DIR/lib/libs/Elastos.ORG.Wallet.Lib.C.aar";
mkdir -p "$PROJECT_DIR/lib/libs/";

cp -rv "$DEPENDS_DIR/Elastos.ORG.Wallet.Lib.C/build/package/Elastos.ORG.Wallet.Lib.C.aar" "$PROJECT_DIR/lib/libs/";
