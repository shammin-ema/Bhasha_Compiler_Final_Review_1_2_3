public class TACInstruction {

    // =========================================
    // TAC Instruction Categories
    // =========================================

    public enum Kind {

        BINOP,
        COPY,
        PRINT
    }

    // =========================================
    // Instruction Fields
    // =========================================

    public final Kind kind;

    public final String dest;

    public final String left;

    public final String op;

    public final String right;

    public final String src;

    // =========================================
    // Constructor
    // =========================================

    private TACInstruction(
            Kind kind,
            String dest,
            String left,
            String op,
            String right,
            String src
    ) {

        this.kind = kind;

        this.dest = dest;

        this.left = left;

        this.op = op;

        this.right = right;

        this.src = src;
    }

    // =========================================
    // Factory Methods
    // =========================================

    public static TACInstruction binop(
            String dest,
            String left,
            String op,
            String right
    ) {

        return new TACInstruction(
                Kind.BINOP,
                dest,
                left,
                op,
                right,
                null
        );
    }

    public static TACInstruction copy(
            String dest,
            String src
    ) {

        return new TACInstruction(
                Kind.COPY,
                dest,
                null,
                null,
                null,
                src
        );
    }

    public static TACInstruction print(
            String src
    ) {

        return new TACInstruction(
                Kind.PRINT,
                null,
                null,
                null,
                null,
                src
        );
    }

    // =========================================
    // TAC Display Formatting
    // =========================================

    @Override
    public String toString() {

        return switch (kind) {

            case BINOP ->
                    formatBinaryOperation();

            case COPY ->
                    formatCopyInstruction();

            case PRINT ->
                    formatPrintInstruction();
        };
    }

    // =========================================
    // Formatting Helpers
    // =========================================

    private String formatBinaryOperation() {

        return dest
                + " = "
                + left
                + " "
                + op
                + " "
                + right;
    }

    private String formatCopyInstruction() {

        return dest
                + " = "
                + src;
    }

    private String formatPrintInstruction() {

        return "print " + src;
    }
}