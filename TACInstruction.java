public class TACInstruction {
    public enum Kind {
        BINOP,
        COPY,
        PRINT
    }

    public final Kind kind;
    public final String dest;
    public final String left;
    public final String op;
    public final String right;
    public final String src;

    private TACInstruction(Kind kind, String dest, String left, String op, String right, String src) {
        this.kind = kind;
        this.dest = dest;
        this.left = left;
        this.op = op;
        this.right = right;
        this.src = src;
    }

    public static TACInstruction binop(String dest, String left, String op, String right) {
        return new TACInstruction(Kind.BINOP, dest, left, op, right, null);
    }

    public static TACInstruction copy(String dest, String src) {
        return new TACInstruction(Kind.COPY, dest, null, null, null, src);
    }

    public static TACInstruction print(String src) {
        return new TACInstruction(Kind.PRINT, null, null, null, null, src);
    }

    @Override
    public String toString() {
        return switch (kind) {
            case BINOP -> dest + " = " + left + " " + op + " " + right;
            case COPY -> dest + " = " + src;
            case PRINT -> "print " + src;
        };
    }
}
