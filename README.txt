Project: Bhasha Compiler - Review 2 Ready Version

Main Review 2 statement:
Our compiler is implemented in Java, but it generates executable Python target code in output.py.

============================================================
REVIEW 1 FEATURES KEPT WORKING
============================================================
- Bangla keywords: সংখ্যা, বাক্য, দেখাও
- Bangla number support: ১০ becomes 10 internally
- Variable declaration and assignment
- Arithmetic expressions with precedence (+, -, *, /)
- Parentheses support for arithmetic grouping
- Symbol table
- Type checking
- Syntax error handling
- Semantic error detection
- Error recovery to newline or semicolon

============================================================
REVIEW 2 FEATURES ADDED
============================================================
- Three Address Code (TAC) generation for non-if-else features
- Python target code generation
- Generated executable file: output.py
- Code generation for declarations
- Code generation for assignments/reassignment
- Code generation for arithmetic expressions
- Code generation for string and variable print statements
- Normal compiler log saved in output.txt
- Error compiler log saved in error_output.txt
- Final Python program output saved in program_output.txt by run.bat
- Code generation skipped if syntax or semantic errors are found
- Easy run scripts: run.bat and run_error.bat
- Commands saved in COMMANDS_REVIEW2.txt
- UTF-8 setup for Bangla terminal/file output

============================================================
WHY IF-ELSE IS NOT INCLUDED HERE
============================================================
Review 2 requires code generation for all features except if-else.
If-else is planned for Review 3.

============================================================
HOW TO RUN IN VS CODE POWERSHELL
============================================================
Normal demo:
.un.bat

Error demo:
.un_error.bat

Manual normal run:
chcp 65001
javac -encoding UTF-8 *.java
java -Dfile.encoding=UTF-8 Main sample.bhasha
py -X utf8 output.py

Manual error run:
chcp 65001
javac -encoding UTF-8 *.java
java -Dfile.encoding=UTF-8 Main error_sample.bhasha

============================================================
IMPORTANT FILES
============================================================
- sample.bhasha       : valid Review 2 demo source
- error_sample.bhasha : error recovery demo source
- output.txt          : normal compiler log
- error_output.txt    : error compiler log
- output.py           : generated executable Python target file
- program_output.txt  : output of generated Python program
- COMMANDS_REVIEW2.txt: saved terminal commands

============================================================
FILES ADDED FOR REVIEW 2
============================================================
- PythonCodeGenerator.java
- TACInstruction.java
- TACGenerator.java
- TeeOutputStream.java
- COMMANDS_REVIEW2.txt
