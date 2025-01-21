public class Token {
    enum Type{
        TNI, ELBUOD, LOOB, RAHC, PRIT, FI, ESLE, ROF, IDENTIFIER,
        NUMBER, TRUE, FALSE, CHAR_LITERAL,
        EQUALS, PLUS, MINUS, STAR, SLASH,
        LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON, COMMA,
        LESS, GREATER, LESSEQ, GREATEREQ, EQEQ, NOTEQ, OR, AND,
        EOF
    }

    Type type;
    String value;

    Token(Type type_, String value_){
        this.type = type_;
        this.value = value_;
    }

    public String toString(){
        return type + " " + (value != null ? value : "");
    }
}
