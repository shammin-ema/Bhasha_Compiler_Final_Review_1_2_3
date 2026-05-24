import java.util.*;

public class PythonCodeGenerator {

    private final SymbolTable symbolTable;
    private final List<String> lines = new ArrayList<>();
    private int indentLevel = 0;

    public PythonCodeGenerator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public String generate(ASTNode program) {
        lines.clear();
        indentLevel = 0;

        emit("# ============================================================");
        emit("# Generated Python target code");
        emit("# Produced by Bhasha Compiler Review 3");
        emit("# Compiler implementation language: Java");
        emit("# Target language: Python");
        emit("# Executable file: output.py");
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
            case IF -> generateIf(node);
            default -> emit("# Unsupported statement in Review 3: " + node.nodeType);
        }
    }

    private void generateIf(ASTNode node) {
        emit("if " + generateCondition(node.condition) + ":");
        indentLevel++;

        if (node.thenBody.isEmpty()) {
            emit("pass");
        } else {
            for (ASTNode statement : node.thenBody) {
                generateStatement(statement);
            }
        }

        indentLevel--;
        emit("else:");
        indentLevel++;

        if (node.elseBody.isEmpty()) {
            emit("pass");
        } else {
            for (ASTNode statement : node.elseBody) {
                generateStatement(statement);
            }
        }

        indentLevel--;
    }

    private String generateCondition(ASTNode node) {
        if (node == null) {
            return "False";
        }

        if (node.nodeType == ASTNode.NodeType.CONDITION) {
            return generateExpression(node.left) + " " + node.value + " " + generateExpression(node.right);
        }

        return generateExpression(node) + " != 0";
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
            case CONDITION -> generateCondition(node);
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
        lines.add("    ".repeat(Math.max(0, indentLevel)) + line);
    }
}
