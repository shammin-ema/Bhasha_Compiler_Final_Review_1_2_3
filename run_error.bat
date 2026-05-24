@echo off
chcp 65001

echo ===== Compiling Java Compiler =====
javac -encoding UTF-8 *.java
if errorlevel 1 (
    echo Java compiler build failed.
    pause
    exit /b 1
)

echo.
echo ===== Running Bhasha Compiler with error_sample.bhasha =====
java -Dfile.encoding=UTF-8 Main error_sample.bhasha

echo.
echo ===== Showing Error Output File: error_output.txt =====
type error_output.txt

echo.
echo ===== Review 2 error demo complete =====
echo Code generation should be skipped when errors are found.
pause
