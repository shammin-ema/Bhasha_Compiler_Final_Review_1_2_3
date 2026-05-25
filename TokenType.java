public enum TokenType {
    // Data types
    SHONKHA,        // সংখ্যা
    BAKKHO,         // বাক্য

    // Output
    DEKHAO,         // দেখাও

    // Review 3 conditional keywords
    JODI,           // যদি
    TAHOLE,         // তাহলে
    NAHLE,          // নাহলে
    SESH,           // শেষ

    // Optional/future loop keyword
    JOTOKKHON,      // যতক্ষণ

    // Literals and identifier
    NUMBER,
    STRING,
    IDENTIFIER,

    // Arithmetic / assignment operators
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    ASSIGN,

    // Review 3 comparison operators
    GREATER,
    LESS,
    GREATER_EQ,
    LESS_EQ,
    EQUALS,
    NOT_EQUALS,

    // Parentheses for arithmetic grouping
    LPAREN,
    RPAREN,

    // Misc
    SEMICOLON,
    NEWLINE,
    EOF
}
