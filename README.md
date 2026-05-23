# Bhasha Compiler Final Review - Ema Part

## Person 2: Ema

This repository contains my assigned part of the Bhasha Compiler final review project.

My main contribution is related to the parser and AST structure of the Bangla compiler.

---

## Included Files

### Source Code Files

- `Parser.java`
- `ASTNode.java`

### Output and Run Files

- `output.txt`
- `run.bat`

### Error Testing Files

- `error_sample.bhasha`
- `error_output.txt`
- `run_error.bat`

### Compiled Java Files

- `Parser.class`
- `Parser$1.class`
- `ASTNode.class`
- `ASTNode$NodeType.class`

---

## My Contribution

### Parser.java

`Parser.java` handles the parsing part of the compiler.

It reads tokens from the lexer and checks whether the program follows the correct grammar rules.

Main tasks of `Parser.java`:

- Parses declaration statements
- Parses assignment statements
- Parses print statements
- Parses arithmetic expressions
- Builds AST nodes
- Reports syntax errors

The parser helps convert tokenized input into a structured form so that the compiler can understand the program properly.

---

### ASTNode.java

`ASTNode.java` defines the structure of the Abstract Syntax Tree, also called AST.

Main tasks of `ASTNode.java`:

- Stores node type
- Stores node value
- Stores data type
- Connects left and right child nodes
- Helps represent the program structure after parsing

The AST is important because it represents the logical structure of the source program after parsing.

---

## Output File

`output.txt` contains the sample output of the compiler after running the project successfully.

It shows the result of token generation, parsing, and compiler output.

---

## Run File

`run.bat` is used to compile and run the Java compiler project easily on Windows.

It helps run the project without writing all commands manually every time.

---

## Error Testing Files

`error_sample.bhasha` contains a sample input file with errors.

`error_output.txt` contains the output generated from the error sample.

`run_error.bat` is used to run the compiler with the error sample and check error handling.

These files are included to show how the compiler detects and reports errors.

---

## Compiled Files

Some `.class` files are also included in this repository.

These files are generated after compiling the Java source code using `javac`.

Example:

```bash
javac -encoding UTF-8 *.java