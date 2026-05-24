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
echo ===== Running Bhasha Compiler with sample.bhasha =====
java -Dfile.encoding=UTF-8 Main sample.bhasha

echo.
echo ===== Running Generated Python Program =====
py -X utf8 output.py
if errorlevel 1 (
    echo py command failed. Trying python command...
    python output.py
)

echo.
echo ===== Saving Python Program Output to program_output.txt =====
py -X utf8 output.py > program_output.txt
if errorlevel 1 (
    python output.py > program_output.txt
)

echo.
echo ===== Showing program_output.txt =====
type program_output.txt

echo.
echo ===== Review 2 normal demo complete =====
echo Files created/updated: output.txt, output.py, program_output.txt
pause
