#!/bin/bash

# Script to check Java installation and run The Guard game

echo "=== The Guard - Java Runtime Checker ==="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ ERROR: Java is not installed or not in PATH"
    echo "Please install Java 8 or higher to run this application."
    echo ""
    echo "To install Java on Ubuntu/Debian:"
    echo "  sudo apt update"
    echo "  sudo apt install openjdk-17-jdk"
    echo ""
    echo "To install Java on macOS (with Homebrew):"
    echo "  brew install openjdk@17"
    echo ""
    echo "To install Java on Windows:"
    echo "  Download from: https://adoptium.net/"
    exit 1
fi

# Get Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1)
echo "✅ Java found: $JAVA_VERSION"

# Check if Java version is compatible (Java 8+)
JAVA_MAJOR_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_MAJOR_VERSION" -lt "8" ] && [ "$JAVA_MAJOR_VERSION" != "1" ]; then
    echo "❌ ERROR: Java version is too old. Please install Java 8 or higher."
    exit 1
fi

# Check if we have Java 1.x format (where 1.8 = Java 8)
if [ "$JAVA_MAJOR_VERSION" = "1" ]; then
    JAVA_MINOR_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f2)
    if [ "$JAVA_MINOR_VERSION" -lt "8" ]; then
        echo "❌ ERROR: Java version is too old. Please install Java 8 or higher."
        exit 1
    fi
fi

echo "✅ Java version is compatible"
echo ""

# Create output directory if it doesn't exist
mkdir -p out

echo "🔧 Compiling Java source files..."
# Compile Java files
if javac -d out src/main/java/com/guard/*.java; then
    echo "✅ Compilation successful"
    echo ""
    echo "🚀 Starting The Guard..."
    echo "----------------------------------------"
    # Run the application
    java -cp out com.guard.TheGuard
else
    echo "❌ Compilation failed"
    exit 1
fi