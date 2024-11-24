import java.util.List;

class Parser {
    private List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().type == Token.Type.EOF;
    }

    private boolean match(Token.Type... types) {
        for (Token.Type type : types) {
            if (peek().type == type) {
                advance();
                return true;
            }
        }
        return false;
    }

    // Example parse method
    public void parse() {
        while (!isAtEnd()) {
            statement();
        }
    }

    private void statement() {
        if (match(Token.Type.TNI, Token.Type.ELBUOD, Token.Type.LOOB, Token.Type.RAHC)) {
            varDeclaration();
        } else if (match(Token.Type.TNIRP)) {
            printStatement();
        } else {
            throw new RuntimeException("Unexpected statement.");
        }
    }

    private void varDeclaration() {
        Token type = previous();
        Token name = consume(Token.Type.IDENTIFIER, "Expected variable name.");
        consume(Token.Type.EQUALS, "Expected '=' after variable name.");
        expression();
        consume(Token.Type.SEMICOLON, "Expected ';' after variable declaration.");
    }

    private void printStatement() {
        consume(Token.Type.LPAREN, "Expected '(' after 'tnirp'.");
        expression();
        consume(Token.Type.RPAREN, "Expected ')' after expression.");
        consume(Token.Type.SEMICOLON, "Expected ';' after print statement.");
    }

    private void expression() {
        // Handle arithmetic and literals here
    }

    private Token consume(Token.Type type, String errorMessage) {
        if (peek().type == type) return advance();
        throw new RuntimeException(errorMessage);
    }
}
