#!/usr/bin/env sh
set -eu
VERSION=8.11.1
BASE="${GRADLE_USER_HOME:-$HOME/.gradle}/soni-wrapper/gradle-$VERSION"
if [ ! -x "$BASE/bin/gradle" ]; then
  mkdir -p "$(dirname "$BASE")"
  TMP="${BASE}.zip"
  echo "Downloading Gradle $VERSION..."
  if command -v curl >/dev/null 2>&1; then curl -fL "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip" -o "$TMP"; else wget -O "$TMP" "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip"; fi
  unzip -q "$TMP" -d "$(dirname "$BASE")"
  rm -f "$TMP"
fi
exec "$BASE/bin/gradle" "$@"
