import java.util.*;

public class Parser {

    private final List<Token> tokens;
    private int pos = 0;
    private final SymbolTable symbolTable = new SymbolTable();
    public boolean hasError = false;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token peekAt(int offset) {
        int index = pos + offset;
        return index < tokens.size() ? tokens.get(index) : tokens.get(tokens.size() - 1);
    }

    private Token consume() {
        if (pos < tokens.size()) {
            return tokens.get(pos++);
        }
        return tokens.get(tokens.size() - 1);
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
        while (check(TokenType.NEWLINE) || check(TokenType.SEMICOLON)) {
            consume();
        }
    }

    public ASTNode parseProgram() {
        ASTNode program = new ASTNode(ASTNode.NodeType.PROGRAM);

        skipNewlines();

        while (!check(TokenType.EOF)) {
            ASTNode statement = parseStatement();

            if (statement != null) {
                program.statements.add(statement);
            }

            skipNewlines();
        }

        return program;
    }

    private ASTNode parseStatement() {
        if (check(TokenType.SHONKHA) || check(TokenType.BAKKHO)) {
            return parseDeclaration();
        }

        if (check(TokenType.DEKHAO)) {
            return parsePrint();
        }

        if (check(TokenType.JODI)) {
            return parseIfStatement();
        }

        if (check(TokenType.IDENTIFIER) && peekAt(1).type == TokenType.ASSIGN) {
            return parseAssignment();
        }

        if (check(TokenType.SEMICOLON) || check(TokenType.NEWLINE)) {
            consume();
            return null;
        }

        if (check(TokenType.NAHLE) || check(TokenType.SESH)) {
            syntaxError("Unexpected block closing keyword: " + peek().value);
            consume();
            return null;
        }

        syntaxError("Unexpected token: " + peek().value);
        recoverToNewlineOrSemicolon();
        return null;
    }

    private ASTNode parseDeclaration() {
        Token typeToken = consume();
        String dataType = typeToken.value;

        Token nameToken = expect(TokenType.IDENTIFIER);
        expect(TokenType.ASSIGN);

        ASTNode expression = parseExpression();

        String expressionType = inferType(expression);

        if (expressionType != null && !dataType.equals(expressionType)) {
            semanticError("Type mismatch: variable '" + nameToken.value
                    + "' is " + dataType + " but value is " + expressionType, nameToken.line);
        }

        symbolTable.declare(nameToken.value, dataType, nameToken.line);

        ASTNode node = new ASTNode(ASTNode.NodeType.DECL, nameToken.value);
        node.dataType = dataType;
        node.line = nameToken.line;
        node.right = expression;

        return node;
    }

    private ASTNode parseAssignment() {
        Token nameToken = consume();

        if (!symbolTable.isDeclared(nameToken.value)) {
            semanticError("Undeclared variable: " + nameToken.value, nameToken.line);
        }

        expect(TokenType.ASSIGN);

        ASTNode expression = parseExpression();

        String variableType = symbolTable.typeOf(nameToken.value);
        String expressionType = inferType(expression);

        if (variableType != null && expressionType != null && !variableType.equals(expressionType)) {
            semanticError("Type mismatch in assignment: " + nameToken.value, nameToken.line);
        }

        ASTNode node = new ASTNode(ASTNode.NodeType.ASSIGN, nameToken.value);
        node.line = nameToken.line;
        node.right = expression;

        return node;
    }

    private ASTNode parsePrint() {
        Token printToken = consume();

        ASTNode node = new ASTNode(ASTNode.NodeType.PRINT);
        node.line = printToken.line;
        node.right = parseExpression();

        return node;
    }

    private ASTNode parseIfStatement() {
        Token ifToken = consume(); // যদি

        ASTNode node = new ASTNode(ASTNode.NodeType.IF);
        node.line = ifToken.line;
        node.condition = parseCondition();

        expect(TokenType.TAHOLE);
        skipNewlines();

        while (!checkAny(TokenType.NAHLE, TokenType.SESH, TokenType.EOF)) {
            ASTNode statement = parseStatement();
            if (statement != null) {
                node.thenBody.add(statement);
            }
            skipNewlines();
        }

        if (check(TokenType.NAHLE)) {
            consume();
            skipNewlines();

            while (!checkAny(TokenType.SESH, TokenType.EOF)) {
                ASTNode statement = parseStatement();
                if (statement != null) {
                    node.elseBody.add(statement);
                }
                skipNewlines();
            }
        }

        if (check(TokenType.SESH)) {
            consume();
        } else {
            syntaxError("Expected SESH/শেষ to close if-else block");
        }

        return node;
    }

    private ASTNode parseCondition() {
        ASTNode left = parseExpression();
        Token opToken = peek();

        if (!isComparisonOperator(opToken.type)) {
            syntaxError("Expected comparison operator in if condition");
            ASTNode condition = new ASTNode(ASTNode.NodeType.CONDITION, "!=");
            condition.line = opToken.line;
            condition.left = left;
            condition.right = new ASTNode(ASTNode.NodeType.NUMBER, "0");
            condition.right.line = opToken.line;
            inferType(condition);
            return condition;
        }

        consume();

        ASTNode right = parseExpression();

        ASTNode condition = new ASTNode(ASTNode.NodeType.CONDITION, opToken.value);
        condition.line = opToken.line;
        condition.left = left;
        condition.right = right;

        inferType(condition);
        return condition;
    }

    private boolean isComparisonOperator(TokenType type) {
        return type == TokenType.GREATER
                || type == TokenType.LESS
                || type == TokenType.GREATER_EQ
                || type == TokenType.LESS_EQ
                || type == TokenType.EQUALS
                || type == TokenType.NOT_EQUALS;
    }

    private ASTNode parseExpression() {
        ASTNode left = parseTerm();

        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            Token opToken = consume();
            String operator = opToken.value;

            ASTNode right = parseTerm();

            ASTNode node = new ASTNode(ASTNode.NodeType.BINOP, operator);
            node.line = opToken.line;
            node.left = left;
            node.right = right;

            left = node;
        }

        return left;
    }

    private ASTNode parseTerm() {
        ASTNode left = parsePrimary();

        while (check(TokenType.MULTIPLY) || check(TokenType.DIVIDE)) {
            Token opToken = consume();
            String operator = opToken.value;

            ASTNode right = parsePrimary();

            ASTNode node = new ASTNode(ASTNode.NodeType.BINOP, operator);
            node.line = opToken.line;
            node.left = left;
            node.right = right;

            left = node;
        }

        return left;
    }

    private ASTNode parsePrimary() {
        Token token = peek();

        if (check(TokenType.NUMBER)) {
            consume();

            ASTNode node = new ASTNode(ASTNode.NodeType.NUMBER, token.value);
            node.line = token.line;
            return node;
        }

        if (check(TokenType.STRING)) {
            consume();

            ASTNode node = new ASTNode(ASTNode.NodeType.STRING, token.value);
            node.line = token.line;
            return node;
        }

        if (check(TokenType.IDENTIFIER)) {
            consume();

            if (!symbolTable.isDeclared(token.value)) {
                semanticError("Undeclared variable: " + token.value, token.line);
            }

            ASTNode node = new ASTNode(ASTNode.NodeType.IDENTIFIER, token.value);
            node.line = token.line;
            return node;
        }

        if (check(TokenType.LPAREN)) {
            consume();
            ASTNode expression = parseExpression();
            expect(TokenType.RPAREN);
            return expression;
        }

        syntaxError("Expected value but found: " + token.value);
        recoverToNewlineOrSemicolon();

        ASTNode dummy = new ASTNode(ASTNode.NodeType.NUMBER, "0");
        dummy.line = token.line;
        return dummy;
    }

    private String inferType(ASTNode node) {
        if (node == null) {
            return null;
        }

        return switch (node.nodeType) {
            case NUMBER -> "সংখ্যা";

            case STRING -> "বাক্য";

            case IDENTIFIER -> symbolTable.typeOf(node.value);

            case BINOP -> {
                String leftType = inferType(node.left);
                String rightType = inferType(node.right);

                if (!"সংখ্যা".equals(leftType) || !"সংখ্যা".equals(rightType)) {
                    semanticError("Arithmetic operator '" + node.value
                            + "' requires সংখ্যা operands", node.line);
                    yield null;
                }

                if ("/".equals(node.value)
                        && node.right != null
                        && node.right.nodeType == ASTNode.NodeType.NUMBER
                        && "0".equals(node.right.value)) {
                    semanticError("Division by zero", node.line);
                    yield null;
                }

                yield "সংখ্যা";
            }

            case CONDITION -> {
                String leftType = inferType(node.left);
                String rightType = inferType(node.right);

                if (leftType == null || rightType == null) {
                    semanticError("Condition operator '" + node.value
                            + "' requires declared operands", node.line);
                    yield null;
                }

                if (">".equals(node.value) || "<".equals(node.value)
                        || ">=".equals(node.value) || "<=".equals(node.value)) {
                    if (!"সংখ্যা".equals(leftType) || !"সংখ্যা".equals(rightType)) {
                        semanticError("Condition operator '" + node.value
                                + "' requires সংখ্যা operands", node.line);
                        yield null;
                    }
                } else if (!leftType.equals(rightType)) {
                    semanticError("Condition operator '" + node.value
                            + "' requires operands of same type", node.line);
                    yield null;
                }

                yield "শর্ত";
            }

            case IF -> {
                inferType(node.condition);
                for (ASTNode stmt : node.thenBody) {
                    inferType(stmt);
                }
                for (ASTNode stmt : node.elseBody) {
                    inferType(stmt);
                }
                yield null;
            }

            case DECL, ASSIGN, PRINT -> {
                inferType(node.right);
                yield null;
            }

            default -> null;
        };
    }

    private Token expect(TokenType expected) {
        if (check(expected)) {
            return consume();
        }

        syntaxError("Expected " + expected + " but found " + peek().type);
        return new Token(expected, "", peek().line);
    }

    private void recoverToNewlineOrSemicolon() {
        while (!checkAny(TokenType.NEWLINE, TokenType.SEMICOLON, TokenType.EOF)) {
            consume();
        }

        if (check(TokenType.NEWLINE) || check(TokenType.SEMICOLON)) {
            consume();
        }
    }

    private void syntaxError(String message) {
        System.err.println("[Syntax Error] line " + peek().line + ": " + message);
        hasError = true;
    }

    private void semanticError(String message, int line) {
        System.err.println("[Semantic Error] line " + line + ": " + message);
        hasError = true;
    }
}
