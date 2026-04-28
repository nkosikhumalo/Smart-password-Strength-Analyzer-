#!/bin/bash
# Build and run the Smart Password Strength Analyzer

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR" || exit 1

SRC_DIR="src"
OUT_DIR="out"

mkdir -p "$OUT_DIR"

echo "Compiling..."
javac -d "$OUT_DIR" "$SRC_DIR"/dsa/*.java "$SRC_DIR"/recommendation/*.java "$SRC_DIR"/PasswordAnalyzerUI.java

if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Launching..."
java -cp "$OUT_DIR" PasswordAnalyzerUI
