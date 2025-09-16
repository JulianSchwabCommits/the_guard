@echo off

REM Script to check Java installation and run The Guard game

echo === The Guard - Java Runtime Checker ===

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ ERROR: Java is not installed or not in PATH
    echo Please install Java 8 or higher to run this application.
    echo.
    echo To install Java on Windows:
    echo   Download from: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Get Java version
echo ✅ Java found
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
    set JAVA_VERSION=!JAVA_VERSION:"=!
    echo Java version: !JAVA_VERSION!
)

echo ✅ Java version is compatible
echo.

REM Create output directory if it doesn't exist
if not exist "out" mkdir out

echo 🔧 Compiling Java source files...
REM Compile Java files
javac -d out src\main\java\com\guard\*.java
if %errorlevel% neq 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)

echo ✅ Compilation successful
echo.
echo 🚀 Starting The Guard...
echo ----------------------------------------
REM Run the application
java -cp out com.guard.TheGuard

pause