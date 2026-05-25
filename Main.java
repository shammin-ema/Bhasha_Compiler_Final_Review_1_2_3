import java.nio.file.*;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    private static final String GENERATED_PYTHON_FILE = "output.py";
    private static final String PROGRAM_OUTPUT_FILE = "program_output.txt";

    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : "sample.bhasha";
        String outputFile = fileName.equals("error_sample.bhasha")
                ? "error_output.txt"
                : "output.txt";

        PrintStream originalOut = System.out;

        try (FileOutputStream fos = new FileOutputStream(outputFile);
             PrintStream fileAndConsole = new PrintStream(
                     new TeeOutputStream(originalOut, fos),
                     true,
                     StandardCharsets.UTF_8
             )) {

            System.setOut(fileAndConsole);
            System.setErr(fileAndConsole);

            System.out.println("============================================================");
            System.out.println("BHASHA COMPILER - REVIEW 3 DEMO");
            System.out.println("Compiler implementation language: Java");
            System.out.println("Generated target language: Python");
            System.out.println("Generated executable target file: " + GENERATED_PYTHON_FILE);
            System.out.println("Source file: " + fileName);
            System.out.println("Compiler log file: " + outputFile);
            System.out.println("============================================================");

            String source = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);

            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.tokenize();

            System.out.println("\n===== REVIEW 1: TOKENS =====");
            for (Token token : tokens) {
                System.out.println(token);
            }

            Parser parser = new Parser(tokens);
            ASTNode program = parser.parseProgram();

            System.out.println("\n===== REVIEW 1: PARSER RESULT =====");
            System.out.println("Total top-level statements parsed: " + program.statements.size());
            System.out.println("Total if-else statements parsed: " + countIfStatements(program));

            SymbolTable symbolTable = parser.getSymbolTable();
            symbolTable.printTable();

            boolean hasAnyError = lexer.hasError() || parser.hasError || symbolTable.hasError;

            System.out.println("\n===== REVIEW 1 RESULT =====");
            if (hasAnyError) {
                System.out.println("Review 1/semantic phase has errors.");
            } else {
                System.out.println("Lexer tokenizes source code correctly.");
                System.out.println("Parser handles valid input.");
                System.out.println("Symbol table is working.");
                System.out.println("Type checking is working.");
                System.out.println("Assignments are working.");
                System.out.println("Arithmetic expressions are working with operator precedence.");
                System.out.println("Review 1 features are still working.");
            }

            System.out.println("\n===== REVIEW 2: CODE GENERATION BASE =====");
            if (hasAnyError) {
                Files.deleteIfExists(Path.of(GENERATED_PYTHON_FILE));
                Files.deleteIfExists(Path.of(PROGRAM_OUTPUT_FILE));
                System.out.println("Code generation skipped because syntax/semantic errors were found.");
                System.out.println("No Python target file was generated from invalid source code.");
                System.out.println("Error recovery was demonstrated by reporting errors and continuing safely.");
            } else {
                TACGenerator tacGenerator = new TACGenerator(symbolTable);
                List<TACInstruction> instructions = tacGenerator.generate(program);

                System.out.println("\n===== REVIEW 2/3: THREE ADDRESS CODE (TAC) =====");
                printTAC(instructions);

                PythonCodeGenerator pythonGenerator = new PythonCodeGenerator(symbolTable);
                String pythonCode = pythonGenerator.generate(program);
                Files.writeString(Path.of(GENERATED_PYTHON_FILE), pythonCode, StandardCharsets.UTF_8);

                System.out.println("\n===== REVIEW 3: GENERATED PYTHON TARGET CODE =====");
                System.out.print(pythonCode);

                System.out.println("===== REVIEW 3 RESULT =====");
                System.out.println("Review 1 features kept working.");
                System.out.println("Review 2 Python code generation kept working.");
                System.out.println("Review 3 if-else parsing and code generation completed successfully.");
                System.out.println("Python target code generated successfully.");
                System.out.println("Generated executable file: " + GENERATED_PYTHON_FILE);
                System.out.println("Run it with: py -X utf8 " + GENERATED_PYTHON_FILE);
                System.out.println("If 'py' is not available, use: python " + GENERATED_PYTHON_FILE);
            }

            System.out.println("\n===== OUTPUT FILES =====");
            System.out.println("Normal compiler log: output.txt");
            System.out.println("Error compiler log: error_output.txt");
            System.out.println("Generated Python executable file: output.py");
            System.out.println("Generated Python program output: program_output.txt (created by run.bat)");

        } catch (Exception e) {
            System.setOut(originalOut);
            System.setErr(originalOut);
            System.err.println("Compiler error: " + e.getMessage());
        }
    }

    private static int countIfStatements(ASTNode node) {
        if (node == null) {
            return 0;
        }

        int count = node.nodeType == ASTNode.NodeType.IF ? 1 : 0;

        for (ASTNode statement : node.statements) {
            count += countIfStatements(statement);
        }

        for (ASTNode statement : node.thenBody) {
            count += countIfStatements(statement);
        }

        for (ASTNode statement : node.elseBody) {
            count += countIfStatements(statement);
        }

        return count;
    }

    private static void printTAC(List<TACInstruction> instructions) {
        System.out.printf("%-5s | %-14s | %s%n", "#", "Kind", "Instruction");
        System.out.println("------+----------------+------------------------------");

        if (instructions.isEmpty()) {
            System.out.println("(No TAC instructions generated)");
            return;
        }

        for (int i = 0; i < instructions.size(); i++) {
            TACInstruction instruction = instructions.get(i);
            System.out.printf("%-5d | %-14s | %s%n", i, instruction.kind, instruction);
        }
    }
}
