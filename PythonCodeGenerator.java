import java.util.*;

public class PythonCodeGenerator {

    private final SymbolTable symbolTable;
    private final List<String> lines = new ArrayList<>();

    public PythonCodeGenerator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public String generate(ASTNode program) {
        lines.clear();
        emit("# ============================================================");
        emit("# Generated Python target code");
        emit("# Produced by Bhasha Compiler Review 2");
        emit("# Compiler implementation language: Java");
        emit("# Target language: Python");
        emit("# ============================================================");
        emit("");

        for (ASTNode statement : program.statements) {
            generateStatement(statement);
        }

        return String.join(System.lineSeparator(), lines) + System.lineSeparator();
    }

    private void generateStatement(ASTNode node) {
        if (node == null) {
            return;
        }

        switch (node.nodeType) {
            case DECL -> {
                String targetName = symbolTable.javaName(node.value);
                emit(targetName + " = " + generateExpression(node.right));
            }
            case ASSIGN -> {
                String targetName = symbolTable.javaName(node.value);
                emit(targetName + " = " + generateExpression(node.right));
            }
            case PRINT -> emit("print(" + generateExpression(node.right) + ")");
            default -> emit("# Unsupported statement in Review 2: " + node.nodeType);
        }
    }

    private String generateExpression(ASTNode node) {
        if (node == null) {
            return "None";
        }

        return switch (node.nodeType) {
            case NUMBER -> node.value;
            case STRING -> pythonStringLiteral(node.value);
            case IDENTIFIER -> symbolTable.javaName(node.value);
            case BINOP -> "(" + generateExpression(node.left) + " " + node.value + " " + generateExpression(node.right) + ")";
            default -> "None";
        };
    }

    private String pythonStringLiteral(String value) {
        if (value == null) {
            return "\"\"";
        }

        String escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        return "\"" + escaped + "\"";
    }

    private void emit(String line) {
        lines.add(line);
    }
}
