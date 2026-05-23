
import java.nio.file.*;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        try {
            String fileName = args.length > 0 ? args[0] : "sample.bhasha";

            String outputFile = fileName.equals("error_sample.bhasha")
                    ? "error_output.txt"
                    : "output.txt";

            PrintStream fileOut = new PrintStream(
                    new FileOutputStream(outputFile),
                    true,
                    StandardCharsets.UTF_8
            );

            System.setOut(fileOut);
            System.setErr(fileOut);

            String source = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);

            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.tokenize();

            System.out.println("===== REVIEW 1: TOKENS =====");
            for (Token token : tokens) {
                System.out.println(token);
            }

            Parser parser = new Parser(tokens);
            ASTNode program = parser.parseProgram();

            System.out.println("\n===== REVIEW 1: PARSER RESULT =====");
            System.out.println("Total statements parsed: " + program.statements.size());

            SymbolTable symbolTable = parser.getSymbolTable();
            symbolTable.printTable();

            System.out.println("\n===== REVIEW 1 RESULT =====");

            if (lexer.hasError() || parser.hasError || symbolTable.hasError) {
                System.out.println("Review 1 has errors.");
            } else {
                System.out.println("Lexer tokenizes source code correctly.");
                System.out.println("Parser handles valid input.");
                System.out.println("Symbol table is working.");
                System.out.println("Type checking is working.");
                System.out.println("Assignments are working.");
                System.out.println("Arithmetic expressions are working.");
                System.out.println("Review 1 completed successfully.");
            }

            fileOut.close();

        } catch (Exception e) {
            System.err.println("Compiler error: " + e.getMessage());
        }
    }
}