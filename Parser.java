import java.util.ArrayList;
import java.util.List;

class Parser {
    private List<Token> tokens;
    private int current = 0;
    private final Interpreter interpreter;


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.interpreter = new Interpreter();
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

    public void parse() {
        while (!isAtEnd()) {
            statement();
        }
    }

    private void statement() {
        if (match(Token.Type.TNI, Token.Type.ELBUOD, Token.Type.LOOB, Token.Type.RAHC)) {
            varDeclaration();
        } else if (match(Token.Type.PRIT)) {
            printStatement();
        } else {
            throw new RuntimeException("Unexpected statement.");
        }
    }

    private void varDeclaration() {
        Token type = previous();
        Token name = consume(Token.Type.IDENTIFIER, "Expected variable name.");
        consume(Token.Type.EQUALS, "Expected '=' after variable name.");
        ASTNode expr = expression();
        consume(Token.Type.SEMICOLON, "Expected ';' after variable declaration.");

        Object value = interpreter.evaluate(expr);
        interpreter.assignVariable(name.value, value);
    }

    private void printStatement() {
        consume(Token.Type.LPAREN, "Expected '(' after 'tnirp'.");
        ASTNode expr = expression();
        consume(Token.Type.RPAREN, "Expected ')' after expression.");
        consume(Token.Type.SEMICOLON, "Expected ';' after print statement.");

        Object result = interpreter.evaluate(expr);
        System.out.println(result);
    }

    private ASTNode expression() {
        ASTNode left = term();

        while (match(Token.Type.PLUS, Token.Type.MINUS)) {
            Token operator = previous();
            ASTNode right = term();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }

    private ASTNode term() {
        ASTNode left = factor();

        while (match(Token.Type.STAR, Token.Type.SLASH)) {
            Token operator = previous();
            ASTNode right = factor();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }

    private ASTNode factor() {
        if (match(Token.Type.NUMBER)) {
            return new LiteralNode(previous().value);
        } else if (match(Token.Type.IDENTIFIER)) {
            return new VariableNode(previous().value);
        } else if (match(Token.Type.LPAREN)) {
            ASTNode expr = expression();
            consume(Token.Type.RPAREN, "Expected ')' after expression.");
            return expr;
        }
        throw new RuntimeException("Expected a number, variable, or expression.");
    }

    private Token consume(Token.Type type, String errorMessage) {
        if (peek().type == type) return advance();
        throw new RuntimeException(errorMessage);
    }

    private void ifStatement() {
        consume(Token.Type.LPAREN, "Expected '(' after 'fi'.");
        ASTNode condition = expression();
        consume(Token.Type.RPAREN, "Expected ')' after condition.");
        consume(Token.Type.LBRACE, "Expected '{' before 'fi' block.");

        List<Runnable> statements = new ArrayList<>();
        while (!match(Token.Type.RBRACE)) {
            statements.add(() -> statement());
        }

        if ((boolean) interpreter.evaluate(condition)) {
            statements.forEach(Runnable::run);
        }
    }

    private void forStatement() {
        consume(Token.Type.LPAREN, "Expected '(' after 'rof'.");
        varDeclaration();
        ASTNode condition = expression();
        consume(Token.Type.SEMICOLON, "Expected ';' after condition.");
        ASTNode increment = expression();
        consume(Token.Type.RPAREN, "Expected ')' after 'rof' parameters.");
        consume(Token.Type.LBRACE, "Expected '{' before 'rof' block.");

        List<Runnable> statements = new ArrayList<>();
        while (!match(Token.Type.RBRACE)) {
            statements.add(() -> statement());
        }

        while ((boolean) interpreter.evaluate(condition)) {
            statements.forEach(Runnable::run);
            interpreter.evaluate(increment);
        }
    }
}
