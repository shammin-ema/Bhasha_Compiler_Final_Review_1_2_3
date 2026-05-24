@echo off
chcp 65001

echo ===== Cleaning old generated files for error demo =====
del output.py 2>nul
del program_output.txt 2>nul
del error_output.txt 2>nul

echo.
echo ===== Compiling Java Compiler =====
javac -encoding UTF-8 *.java
if errorlevel 1 (
    echo Java compiler build failed.
    pause
    exit /b 1
)

echo.
echo ===== Running Bhasha Compiler Review 3 with error_sample.bhasha =====
java -Dfile.encoding=UTF-8 Main error_sample.bhasha

echo.
echo ===== Removing Python output files because this is an error demo =====
del output.py 2>nul
del program_output.txt 2>nul

echo.
echo ===== Showing Error Output File: error_output.txt =====
type error_output.txt

echo.
echo ===== Review 3 error demo complete =====
echo Code generation should be skipped when syntax/semantic errors are found.
echo Expected file for this demo: error_output.txt only.
echo output.py and program_output.txt should NOT exist after error demo.
pause