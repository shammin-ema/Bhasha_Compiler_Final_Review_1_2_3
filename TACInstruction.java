public class TACInstruction {

    // =========================================
    // TAC Instruction Types
    // =========================================

    public enum Kind {

        BINOP,
        COPY,
        PRINT,

        LABEL,
        IF_FALSE_GOTO,
        GOTO
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

    public final String label;

    public final String condition;

    // =========================================
    // Constructor
    // =========================================

    private TACInstruction(
            Kind kind,
            String dest,
            String left,
            String op,
            String right,
            String src,
            String label,
            String condition
    ) {

        this.kind = kind;

        this.dest = dest;

        this.left = left;

        this.op = op;

        this.right = right;

        this.src = src;

        this.label = label;

        this.condition = condition;
    }

    // =========================================
    // Arithmetic TAC Instructions
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
                null,
                null,
                null
        );
    }

    // =========================================
    // Copy Instruction
    // =========================================

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
                src,
                null,
                null
        );
    }

    // =========================================
    // Print Instruction
    // =========================================

    public static TACInstruction print(
            String src
    ) {

        return new TACInstruction(
                Kind.PRINT,
                null,
                null,
                null,
                null,
                src,
                null,
                null
        );
    }

    // =========================================
    // Label Instruction
    // =========================================

    public static TACInstruction label(
            String label
    ) {

        return new TACInstruction(
                Kind.LABEL,
                null,
                null,
                null,
                null,
                null,
                label,
                null
        );
    }

    // =========================================
    // Conditional Jump Instruction
    // =========================================

    public static TACInstruction ifFalseGoto(
            String condition,
            String label
    ) {

        return new TACInstruction(
                Kind.IF_FALSE_GOTO,
                null,
                null,
                null,
                null,
                null,
                label,
                condition
        );
    }

    // =========================================
    // Unconditional Jump Instruction
    // =========================================

    public static TACInstruction gotoLabel(
            String label
    ) {

        return new TACInstruction(
                Kind.GOTO,
                null,
                null,
                null,
                null,
                null,
                label,
                null
        );
    }

    // =========================================
    // TAC Display Formatting
    // =========================================

    @Override
    public String toString() {

        return switch (kind) {

            case BINOP ->
                    formatBinaryInstruction();

            case COPY ->
                    formatCopyInstruction();

            case PRINT ->
                    formatPrintInstruction();

            case LABEL ->
                    formatLabelInstruction();

            case IF_FALSE_GOTO ->
                    formatConditionalJump();

            case GOTO ->
                    formatGotoInstruction();
        };
    }

    // =========================================
    // Formatting Helper Methods
    // =========================================

    private String formatBinaryInstruction() {

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

    private String formatLabelInstruction() {

        return label + ":";
    }

    private String formatConditionalJump() {

        return "ifFalse "
                + condition
                + " goto "
                + label;
    }

    private String formatGotoInstruction() {

        return "goto " + label;
    }
}