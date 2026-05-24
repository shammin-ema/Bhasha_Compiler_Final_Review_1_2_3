import java.util.*;

public class ASTNode {

    public enum NodeType {
        PROGRAM,

        // Review 1
        DECL,
        ASSIGN,
        PRINT,
        BINOP,
        NUMBER,
        STRING,
        IDENTIFIER,

        // Review 3
        IF,
        CONDITION,

        // Optional/future
        WHILE
    }

    public NodeType nodeType;
    public String value;
    public String dataType;

    // Line number for better syntax/semantic error messages
    public int line;

    public ASTNode left;
    public ASTNode right;

    // Review 3 if-else support
    public ASTNode condition;
    public List<ASTNode> thenBody = new ArrayList<>();
    public List<ASTNode> elseBody = new ArrayList<>();

    // Optional/future loop support
    public List<ASTNode> loopBody = new ArrayList<>();

    public List<ASTNode> statements = new ArrayList<>();

    public ASTNode(NodeType type) {
        this.nodeType = type;
    }

    public ASTNode(NodeType type, String value) {
        this.nodeType = type;
        this.value = value;
    }
}
