@echo off
setlocal

:: =========================================
:: Bhasha Compiler Automation Script
:: =========================================

chcp 65001 > nul

echo.
echo =========================================
echo        Bhasha Compiler Review 2
echo =========================================

:: =========================================
:: Step 1 : Compile Java Source Files
:: =========================================

echo.
echo [1/5] Compiling Java source files...

javac -encoding UTF-8 *.java

if errorlevel 1 (
    echo.
    echo [ERROR] Java compilation failed.
    echo Please fix compilation errors and try again.
    pause
    exit /b 1
)

echo [SUCCESS] Java compilation completed.

:: =========================================
:: Step 2 : Run Compiler
:: =========================================

echo.
echo [2/5] Running compiler with sample.bhasha...

java -Dfile.encoding=UTF-8 Main sample.bhasha

if errorlevel 1 (
    echo.
    echo [ERROR] Compiler execution failed.
    pause
    exit /b 1
)

echo [SUCCESS] Compiler execution completed.

:: =========================================
:: Step 3 : Execute Generated Python Program
:: =========================================

echo.
echo [3/5] Running generated Python program...

py -X utf8 output.py

if errorlevel 1 (
    echo.
    echo [INFO] 'py' command unavailable.
    echo Trying fallback python command...

    python output.py

    if errorlevel 1 (
        echo.
        echo [ERROR] Python execution failed.
        pause
        exit /b 1
    )
)

echo [SUCCESS] Python execution completed.

:: =========================================
:: Step 4 : Save Program Output
:: =========================================

echo.
echo [4/5] Saving output to program_output.txt...

py -X utf8 output.py > program_output.txt

if errorlevel 1 (
    python output.py > program_output.txt
)

echo [SUCCESS] Output saved successfully.

:: =========================================
:: Step 5 : Display Program Output
:: =========================================

echo.
echo [5/5] Displaying generated output...

echo -----------------------------------------
type program_output.txt
echo -----------------------------------------

:: =========================================
:: Final Status
:: =========================================

echo.
echo =========================================
echo      Review 2 Demo Completed
echo =========================================

echo.
echo Generated Files:
echo - output.txt
echo - output.py
echo - program_output.txt

echo.
echo Compiler pipeline executed successfully.

pause
endlocal