import java.util.*;

public class Parser {

    // =========================
    // Parser State
    // =========================

    private final List<Token> tokens;
    private int pos = 0;

    private final SymbolTable symbolTable = new SymbolTable();

    public boolean hasError = false;

    // =========================
    // Constructor
    // =========================

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // =========================
    // Public Accessors
    // =========================

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    // =========================
    // Token Navigation Helpers
    // =========================

    private Token peek() {
        return tokens.get(pos);
    }

    private Token peekAt(int offset) {
        int index = pos + offset;

        return index < tokens.size()
                ? tokens.get(index)
                : tokens.get(tokens.size() - 1);
    }

    private Token consume() {
        return tokens.get(pos++);
    }

    private boolean check(TokenType type) {
        return peek().type == type;
    }

    private boolean checkAny(TokenType... types) {
        for (TokenType type : types) {
            if (peek().type == type) {
                return true;
            }
        }

        return false;
    }

    private void skipNewlines() {
        while (check(TokenType.NEWLINE)) {
            consume();
        }
    }

    private boolean isAddOperator() {
        return check(TokenType.PLUS)
                || check(TokenType.MINUS);
    }

    private boolean isMulOperator() {
        return check(TokenType.MULTIPLY)
                || check(TokenType.DIVIDE);
    }

    private boolean isStatementTerminator() {
        return check(TokenType.NEWLINE)
                || check(TokenType.SEMICOLON);
    }

    // =========================
    // Program Parsing
    // =========================

    public ASTNode parseProgram() {

        ASTNode programNode =
                new ASTNode(ASTNode.NodeType.PROGRAM);

        skipNewlines();

        while (!check(TokenType.EOF)) {

            ASTNode statementNode = parseStatement();

            if (statementNode != null) {
                programNode.statements.add(statementNode);
            }

            skipNewlines();
        }

        return programNode;
    }

    // =========================
    // Statement Parsing
    // =========================

    private ASTNode parseStatement() {

        if (check(TokenType.SHONKHA)
                || check(TokenType.BAKKHO)) {

            return parseDeclaration();
        }

        if (check(TokenType.DEKHAO)) {
            return parsePrint();
        }

        if (check(TokenType.IDENTIFIER)
                && peekAt(1).type == TokenType.ASSIGN) {

            return parseAssignment();
        }

        if (check(TokenType.SEMICOLON)
                || check(TokenType.NEWLINE)) {

            consume();
            return null;
        }

        syntaxError("Unexpected token: " + peek().value);

        recoverToNewlineOrSemicolon();

        return null;
    }

    // =========================
    // Declaration Parsing
    // =========================

    private ASTNode parseDeclaration() {

        Token typeToken = consume();

        String dataType = typeToken.value;

        Token nameToken = expect(TokenType.IDENTIFIER);

        expect(TokenType.ASSIGN);

        ASTNode expressionNode = parseExpression();

        String expressionType =
                inferType(expressionNode);

        if (expressionType != null
                && !dataType.equals(expressionType)) {

            semanticError(
                    "Type mismatch: variable '"
                            + nameToken.value
                            + "' is "
                            + dataType
                            + " but value is "
                            + expressionType,

                    nameToken.line
            );
        }

        symbolTable.declare(
                nameToken.value,
                dataType,
                nameToken.line
        );

        ASTNode declarationNode =
                new ASTNode(
                        ASTNode.NodeType.DECL,
                        nameToken.value
                );

        declarationNode.dataType = dataType;
        declarationNode.line = nameToken.line;
        declarationNode.right = expressionNode;

        return declarationNode;
    }

    // =========================
    // Assignment Parsing
    // =========================

    private ASTNode parseAssignment() {

        Token nameToken = consume();

        if (!symbolTable.isDeclared(nameToken.value)) {

            semanticError(
                    "Undeclared variable: "
                            + nameToken.value,

                    nameToken.line
            );
        }

        expect(TokenType.ASSIGN);

        ASTNode expressionNode = parseExpression();

        String variableType =
                symbolTable.typeOf(nameToken.value);

        String expressionType =
                inferType(expressionNode);

        if (variableType != null
                && expressionType != null
                && !variableType.equals(expressionType)) {

            semanticError(
                    "Type mismatch in assignment: "
                            + nameToken.value,

                    nameToken.line
            );
        }

        ASTNode assignmentNode =
                new ASTNode(
                        ASTNode.NodeType.ASSIGN,
                        nameToken.value
                );

        assignmentNode.line = nameToken.line;
        assignmentNode.right = expressionNode;

        return assignmentNode;
    }

    // =========================
    // Print Statement Parsing
    // =========================

    private ASTNode parsePrint() {

        Token printToken = consume();

        ASTNode printNode =
                new ASTNode(ASTNode.NodeType.PRINT);

        printNode.line = printToken.line;

        printNode.right = parseExpression();

        return printNode;
    }

    // =========================
    // Expression Parsing
    // =========================

    private ASTNode parseExpression() {

        ASTNode leftNode = parseTerm();

        while (isAddOperator()) {

            Token operatorToken = consume();

            String operator = operatorToken.value;

            ASTNode rightNode = parseTerm();

            leftNode = createBinaryNode(
                    operator,
                    leftNode,
                    rightNode,
                    operatorToken.line
            );
        }

        return leftNode;
    }

    private ASTNode parseTerm() {

        ASTNode leftNode = parsePrimary();

        while (isMulOperator()) {

            Token operatorToken = consume();

            String operator = operatorToken.value;

            ASTNode rightNode = parsePrimary();

            leftNode = createBinaryNode(
                    operator,
                    leftNode,
                    rightNode,
                    operatorToken.line
            );
        }

        return leftNode;
    }

    // =========================
    // Primary Expression Parsing
    // =========================

    private ASTNode parsePrimary() {

        Token currentToken = peek();

        if (check(TokenType.NUMBER)) {

            consume();

            ASTNode numberNode =
                    new ASTNode(
                            ASTNode.NodeType.NUMBER,
                            currentToken.value
                    );

            numberNode.line = currentToken.line;

            return numberNode;
        }

        if (check(TokenType.STRING)) {

            consume();

            ASTNode stringNode =
                    new ASTNode(
                            ASTNode.NodeType.STRING,
                            currentToken.value
                    );

            stringNode.line = currentToken.line;

            return stringNode;
        }

        if (check(TokenType.IDENTIFIER)) {

            consume();

            if (!symbolTable.isDeclared(currentToken.value)) {

                semanticError(
                        "Undeclared variable: "
                                + currentToken.value,

                        currentToken.line
                );
            }

            ASTNode identifierNode =
                    new ASTNode(
                            ASTNode.NodeType.IDENTIFIER,
                            currentToken.value
                    );

            identifierNode.line = currentToken.line;

            return identifierNode;
        }

        if (check(TokenType.LPAREN)) {

            consume();

            ASTNode expressionNode = parseExpression();

            expect(TokenType.RPAREN);

            return expressionNode;
        }

        syntaxError(
                "Expected value but found: "
                        + currentToken.value
        );

        recoverToNewlineOrSemicolon();

        ASTNode dummyNode =
                new ASTNode(
                        ASTNode.NodeType.NUMBER,
                        "0"
                );

        dummyNode.line = currentToken.line;

        return dummyNode;
    }

    // =========================
    // AST Helper Methods
    // =========================

    private ASTNode createBinaryNode(
            String operator,
            ASTNode leftNode,
            ASTNode rightNode,
            int line
    ) {

        ASTNode binaryNode =
                new ASTNode(
                        ASTNode.NodeType.BINOP,
                        operator
                );

        binaryNode.line = line;
        binaryNode.left = leftNode;
        binaryNode.right = rightNode;

        return binaryNode;
    }

    // =========================
    // Type Inference
    // =========================

    private String inferType(ASTNode node) {

        if (node == null) {
            return null;
        }

        return switch (node.nodeType) {

            case NUMBER -> "সংখ্যা";

            case STRING -> "বাক্য";

            case IDENTIFIER ->
                    symbolTable.typeOf(node.value);

            case BINOP -> {

                String leftType =
                        inferType(node.left);

                String rightType =
                        inferType(node.right);

                if (!"সংখ্যা".equals(leftType)
                        || !"সংখ্যা".equals(rightType)) {

                    semanticError(
                            "Arithmetic operator '"
                                    + node.value
                                    + "' requires সংখ্যা operands",

                            node.line
                    );

                    yield null;
                }

                if ("/".equals(node.value)
                        && node.right != null
                        && node.right.nodeType
                        == ASTNode.NodeType.NUMBER
                        && "0".equals(node.right.value)) {

                    semanticError(
                            "Division by zero",
                            node.line
                    );

                    yield null;
                }

                yield "সংখ্যা";
            }

            default -> null;
        };
    }

    // =========================
    // Token Validation
    // =========================

    private Token expect(TokenType expected) {

        if (check(expected)) {
            return consume();
        }

        syntaxError(
                "Expected "
                        + expected
                        + " but found "
                        + peek().type
        );

        recoverToNewlineOrSemicolon();

        return new Token(
                expected,
                "",
                peek().line
        );
    }

    // =========================
    // Error Recovery
    // =========================

    private void recoverToNewlineOrSemicolon() {

        while (!checkAny(
                TokenType.NEWLINE,
                TokenType.SEMICOLON,
                TokenType.EOF)) {

            consume();
        }

        if (isStatementTerminator()) {
            consume();
        }
    }

    // =========================
    // Error Reporting
    // =========================

    private void reportError(
            String errorType,
            String message,
            int line
    ) {

        System.err.println(
                "[" + errorType + "] line "
                        + line
                        + ": "
                        + message
        );

        hasError = true;
    }

    private void syntaxError(String message) {
        reportError(
                "Syntax Error",
                message,
                peek().line
        );
    }

    private void semanticError(
            String message,
            int line
    ) {

        reportError(
                "Semantic Error",
                message,
                line
        );
    }
}