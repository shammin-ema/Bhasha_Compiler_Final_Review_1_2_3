import java.util.*;

public class PythonCodeGenerator {

    // =========================================
    // Generator State
    // =========================================

    private final SymbolTable symbolTable;

    private final List<String> lines =
            new ArrayList<>();

    // =========================================
    // Constructor
    // =========================================

    public PythonCodeGenerator(
            SymbolTable symbolTable
    ) {

        this.symbolTable = symbolTable;
    }

    // =========================================
    // Main Python Code Generation
    // =========================================

    public String generate(ASTNode program) {

        lines.clear();

        generateHeader();

        for (ASTNode statementNode
                : program.statements) {

            generateStatement(statementNode);
        }

        return String.join(
                System.lineSeparator(),
                lines
        ) + System.lineSeparator();
    }

    // =========================================
    // Header Generation
    // =========================================

    private void generateHeader() {

        emit("# ============================================================");
        emit("# Generated Python target code");
        emit("# Produced by Bhasha Compiler Review 2");
        emit("# Compiler implementation language: Java");
        emit("# Target language: Python");
        emit("# ============================================================");

        emit("");
    }

    // =========================================
    // Statement Generation
    // =========================================

    private void generateStatement(ASTNode node) {

        if (node == null) {
            return;
        }

        switch (node.nodeType) {

            case DECL -> generateDeclaration(node);

            case ASSIGN -> generateAssignment(node);

            case PRINT -> generatePrint(node);

            default -> emit(
                    "# Unsupported statement in Review 2: "
                            + node.nodeType
            );
        }
    }

    // =========================================
    // Declaration Generation
    // =========================================

    private void generateDeclaration(ASTNode node) {

        String targetName =
                symbolTable.javaName(node.value);

        String expression =
                generateExpression(node.right);

        emit(targetName + " = " + expression);
    }

    // =========================================
    // Assignment Generation
    // =========================================

    private void generateAssignment(ASTNode node) {

        String targetName =
                symbolTable.javaName(node.value);

        String expression =
                generateExpression(node.right);

        emit(targetName + " = " + expression);
    }

    // =========================================
    // Print Statement Generation
    // =========================================

    private void generatePrint(ASTNode node) {

        String expression =
                generateExpression(node.right);

        emit("print(" + expression + ")");
    }

    // =========================================
    // Expression Generation
    // =========================================

    private String generateExpression(ASTNode node) {

        if (node == null) {
            return "None";
        }

        return switch (node.nodeType) {

            case NUMBER -> node.value;

            case STRING ->
                    pythonStringLiteral(node.value);

            case IDENTIFIER ->
                    symbolTable.javaName(node.value);

            case BINOP -> generateBinaryExpression(node);

            default -> "None";
        };
    }

    // =========================================
    // Binary Expression Generation
    // =========================================

    private String generateBinaryExpression(
            ASTNode node
    ) {

        String leftExpression =
                generateExpression(node.left);

        String rightExpression =
                generateExpression(node.right);

        return "("
                + leftExpression
                + " "
                + node.value
                + " "
                + rightExpression
                + ")";
    }

    // =========================================
    // Python String Handling
    // =========================================

    private String pythonStringLiteral(
            String value
    ) {

        if (value == null) {
            return "\"\"";
        }

        String escapedValue = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        return "\"" + escapedValue + "\"";
    }

    // =========================================
    // Output Helpers
    // =========================================

    private void emit(String line) {
        lines.add(line);
    }
}