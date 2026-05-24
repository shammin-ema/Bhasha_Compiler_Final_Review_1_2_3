import java.util.*;

public class TACGenerator {

    private final SymbolTable symbolTable;
    private final List<TACInstruction> instructions = new ArrayList<>();
    private int tempCounter = 0;
    private int labelCounter = 0;

    public TACGenerator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public List<TACInstruction> generate(ASTNode program) {
        instructions.clear();
        tempCounter = 0;
        labelCounter = 0;

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
            case IF -> generateIf(node);
            default -> {
                // Unsupported optional/future statements are ignored safely.
            }
        }
    }

    private void generateIf(ASTNode node) {
        String condition = generateCondition(node.condition);
        String elseLabel = newLabel("else");
        String endLabel = newLabel("endif");

        instructions.add(TACInstruction.ifFalseGoto(condition, elseLabel));

        for (ASTNode statement : node.thenBody) {
            generateStatement(statement);
        }

        instructions.add(TACInstruction.gotoLabel(endLabel));
        instructions.add(TACInstruction.label(elseLabel));

        for (ASTNode statement : node.elseBody) {
            generateStatement(statement);
        }

        instructions.add(TACInstruction.label(endLabel));
    }

    private String generateCondition(ASTNode node) {
        if (node == null) {
            return "0 != 0";
        }

        if (node.nodeType == ASTNode.NodeType.CONDITION) {
            String left = generateExpression(node.left);
            String right = generateExpression(node.right);
            return left + " " + node.value + " " + right;
        }

        return generateExpression(node) + " != 0";
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
            case CONDITION -> generateCondition(node);
            default -> "0";
        };
    }

    private String newTemp() {
        return "t" + tempCounter++;
    }

    private String newLabel(String prefix) {
        return prefix + "_" + labelCounter++;
    }

    private String quoteString(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
