Project: Bhasha Compiler - Review 3 Ready Version

Main Review 3 statement:
Our compiler is implemented in Java, but it generates executable Python target code in output.py.
The generated Python file is executable and must be run using Python.

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
REVIEW 2 FEATURES KEPT WORKING
============================================================
- Three Address Code (TAC) generation
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
- UTF-8 setup for Bangla terminal/file output

============================================================
REVIEW 3 FEATURES ADDED
============================================================
- Full if-else support end-to-end
- Bangla conditional keywords:
  যদি, তাহলে, নাহলে, শেষ
- Comparison operators:
  >, <, >=, <=, ==, !=
- Parser support for if-else blocks
- AST IF node with condition, thenBody, and elseBody
- Semantic checking for if conditions
- TAC control-flow instructions:
  LABEL, IF_FALSE_GOTO, GOTO
- Python if/else code generation with correct indentation
- output.py contains executable Python if-else code
- sample.bhasha includes true branch and else branch examples
- error_sample.bhasha includes if-else error recovery examples
- Loop is optional, so it is not implemented in this version

============================================================
HOW TO RUN IN VS CODE POWERSHELL
============================================================
Normal Review 3 demo:
.\run.bat

Error Review 3 demo:
.\run_error.bat

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
- sample.bhasha          : valid Review 3 demo source
- error_sample.bhasha    : Review 3 error recovery demo source
- output.txt             : normal compiler log
- error_output.txt       : error compiler log
- output.py              : generated executable Python target file
- program_output.txt     : output of generated Python program
- COMMANDS_REVIEW3.txt   : saved terminal commands
- REVIEW3_STATUS.txt     : Review 3 checklist/status

============================================================
DEMO FLOW
============================================================
sample.bhasha
    -> Java compiler
    -> Lexer + Parser + Symbol Table + Semantic Check
    -> TAC + Python Code Generator
    -> output.py
    -> py -X utf8 output.py
    -> program_output.txt
