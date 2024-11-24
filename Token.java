public class Token {
    enum Type{
        TNI, ELBUOD, LOOB, RAHC, PRIT, FI, ROF, IDENTIFIER,
        NUMBER, STRING, TRUE, FALSE,
        EQUALS, PLUS, MINUS, STAR, SLASH,
        LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON, COMMA,
        LESS, GREATER, LESSEQ, GREATEREQ, EQEQ, NOTEQ,
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
