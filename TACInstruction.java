public class TACInstruction {
    public enum Kind {
        BINOP,
        COPY,
        PRINT,
        LABEL,
        IF_FALSE_GOTO,
        GOTO
    }

    public final Kind kind;
    public final String dest;
    public final String left;
    public final String op;
    public final String right;
    public final String src;
    public final String label;
    public final String condition;

    private TACInstruction(Kind kind, String dest, String left, String op, String right,
                           String src, String label, String condition) {
        this.kind = kind;
        this.dest = dest;
        this.left = left;
        this.op = op;
        this.right = right;
        this.src = src;
        this.label = label;
        this.condition = condition;
    }

    public static TACInstruction binop(String dest, String left, String op, String right) {
        return new TACInstruction(Kind.BINOP, dest, left, op, right, null, null, null);
    }

    public static TACInstruction copy(String dest, String src) {
        return new TACInstruction(Kind.COPY, dest, null, null, null, src, null, null);
    }

    public static TACInstruction print(String src) {
        return new TACInstruction(Kind.PRINT, null, null, null, null, src, null, null);
    }

    public static TACInstruction label(String label) {
        return new TACInstruction(Kind.LABEL, null, null, null, null, null, label, null);
    }

    public static TACInstruction ifFalseGoto(String condition, String label) {
        return new TACInstruction(Kind.IF_FALSE_GOTO, null, null, null, null, null, label, condition);
    }

    public static TACInstruction gotoLabel(String label) {
        return new TACInstruction(Kind.GOTO, null, null, null, null, null, label, null);
    }

    @Override
    public String toString() {
        return switch (kind) {
            case BINOP -> dest + " = " + left + " " + op + " " + right;
            case COPY -> dest + " = " + src;
            case PRINT -> "print " + src;
            case LABEL -> label + ":";
            case IF_FALSE_GOTO -> "ifFalse " + condition + " goto " + label;
            case GOTO -> "goto " + label;
        };
    }
}
