import java.util.*;

public class TACGenerator {

    private final SymbolTable symbolTable;
    private final List<TACInstruction> instructions = new ArrayList<>();
    private int tempCounter = 0;

    public TACGenerator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public List<TACInstruction> generate(ASTNode program) {
        instructions.clear();
        tempCounter = 0;

        for (ASTNode statement : program.statements) {
            generateStatement(statement);
        }

        return instructions;
    }

    private void generateStatement(ASTNode node) {
        if (node == null) {
            return;
        }

        switch (node.nodeType) {
            case DECL, ASSIGN -> {
                String result = generateExpression(node.right);
                String targetName = symbolTable.javaName(node.value);
                instructions.add(TACInstruction.copy(targetName, result));
            }
            case PRINT -> {
                String result = generateExpression(node.right);
                instructions.add(TACInstruction.print(result));
            }
            default -> {
                // Review 2 excludes if-else. Unsupported statements are ignored here.
            }
        }
    }

    private String generateExpression(ASTNode node) {
        if (node == null) {
            return "0";
        }

        return switch (node.nodeType) {
            case NUMBER -> node.value;
            case STRING -> quoteString(node.value);
            case IDENTIFIER -> symbolTable.javaName(node.value);
            case BINOP -> {
                String left = generateExpression(node.left);
                String right = generateExpression(node.right);
                String temp = newTemp();
                instructions.add(TACInstruction.binop(temp, left, node.value, right));
                yield temp;
            }
            default -> "0";
        };
    }

    private String newTemp() {
        return "t" + tempCounter++;
    }

    private String quoteString(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
