import java.util.*;

public class TACGenerator {

    // =========================================
    // TAC Generator State
    // =========================================

    private final SymbolTable symbolTable;

    private final List<TACInstruction> instructions =
            new ArrayList<>();

    private int tempCounter = 0;

    // =========================================
    // Constructor
    // =========================================

    public TACGenerator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    // =========================================
    // Main TAC Generation
    // =========================================

    public List<TACInstruction> generate(
            ASTNode program
    ) {

        instructions.clear();

        resetTemporaryCounter();

        for (ASTNode statementNode
                : program.statements) {

            generateStatement(statementNode);
        }

        return instructions;
    }

    // =========================================
    // Statement Generation
    // =========================================

    private void generateStatement(ASTNode node) {

        if (node == null) {
            return;
        }

        switch (node.nodeType) {

            case DECL ->
                    generateDeclaration(node);

            case ASSIGN ->
                    generateAssignment(node);

            case PRINT ->
                    generatePrint(node);

            default -> {
                // Review 2 excludes if-else support.
                // Unsupported statements are skipped.
            }
        }
    }

    // =========================================
    // Declaration TAC Generation
    // =========================================

    private void generateDeclaration(
            ASTNode node
    ) {

        String result =
                generateExpression(node.right);

        String targetName =
                symbolTable.javaName(node.value);

        instructions.add(
                TACInstruction.copy(
                        targetName,
                        result
                )
        );
    }

    // =========================================
    // Assignment TAC Generation
    // =========================================

    private void generateAssignment(
            ASTNode node
    ) {

        String result =
                generateExpression(node.right);

        String targetName =
                symbolTable.javaName(node.value);

        instructions.add(
                TACInstruction.copy(
                        targetName,
                        result
                )
        );
    }

    // =========================================
    // Print TAC Generation
    // =========================================

    private void generatePrint(
            ASTNode node
    ) {

        String result =
                generateExpression(node.right);

        instructions.add(
                TACInstruction.print(result)
        );
    }

    // =========================================
    // Expression TAC Generation
    // =========================================

    private String generateExpression(
            ASTNode node
    ) {

        if (node == null) {
            return "0";
        }

        return switch (node.nodeType) {

            case NUMBER -> node.value;

            case STRING ->
                    quoteString(node.value);

            case IDENTIFIER ->
                    symbolTable.javaName(node.value);

            case BINOP ->
                    generateBinaryOperation(node);

            default -> "0";
        };
    }

    // =========================================
    // Binary Operation TAC Generation
    // =========================================

    private String generateBinaryOperation(
            ASTNode node
    ) {

        String leftOperand =
                generateExpression(node.left);

        String rightOperand =
                generateExpression(node.right);

        String temporaryVariable =
                createTemporaryVariable();

        instructions.add(
                TACInstruction.binop(
                        temporaryVariable,
                        leftOperand,
                        node.value,
                        rightOperand
                )
        );

        return temporaryVariable;
    }

    // =========================================
    // Temporary Variable Management
    // =========================================

    private void resetTemporaryCounter() {
        tempCounter = 0;
    }

    private String createTemporaryVariable() {
        return "t" + tempCounter++;
    }

    // =========================================
    // String Formatting Helpers
    // =========================================

    private String quoteString(String value) {

        if (value == null) {
            return "\"\"";
        }

        String escapedValue = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");

        return "\"" + escapedValue + "\"";
    }
}