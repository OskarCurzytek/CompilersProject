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
        //System.out.println("Parsing statement, token: " + peek());
        if (match(Token.Type.TNI, Token.Type.ELBUOD, Token.Type.LOOB, Token.Type.RAHC)) {
            varDeclaration();
        } else if (match(Token.Type.PRIT)) {
            printStatement();
        } else if (match(Token.Type.FI)) {
            ifStatement();
        } else if (match(Token.Type.ROF)) {
            forStatement();
        } else {
            throw new RuntimeException("Unexpected statement.");
        }
    }

    private void varDeclaration() {
        Token type = previous();
        Token name = consume(Token.Type.IDENTIFIER, "Expected variable name.");
        //System.out.println("variable declaration: type =" + type + "name=" + name.value);

        consume(Token.Type.EQUALS, "Expected '=' after variable name.");
        ASTNode expr = expression();
        //System.out.println("Variable initialization expression: " + expr);
        consume(Token.Type.SEMICOLON, "Expected ';' after variable declaration.");

        // Evaluate the expression and store in the interpreter
        Object value = interpreter.evaluate(expr);
        //System.out.println("Variable assigned: " + name.value + " = " + value);
        interpreter.assignVariable(name.value, value);
    }

    private void printStatement() {
        consume(Token.Type.LPAREN, "Expected '(' after 'prit'.");
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

        while (match(Token.Type.AND, Token.Type.OR)) {
            Token operator = previous();
            ASTNode right = term();
            left = new BinaryOperationNode(left, operator, right);
        }

        while (match(Token.Type.LESS, Token.Type.GREATER, Token.Type.LESSEQ, Token.Type.GREATEREQ, Token.Type.EQEQ, Token.Type.NOTEQ)) {
            Token operator = previous();
            ASTNode right = term();
            left = new BinaryOperationNode(left, operator, right);
        }
        return left;
    }

    private ASTNode term() {
        ASTNode left = factor();  // Parse the first factor

        while (match(Token.Type.STAR, Token.Type.SLASH)) {
            Token operator = previous();
            ASTNode right = factor();  // Parse the next factor
            left = new BinaryOperationNode(left, operator, right); // Combine into AST
        }
        return left;
    }

    private ASTNode factor() {
        if (match(Token.Type.NUMBER)) {
            return new LiteralNode(previous().value);
        } else if (match(Token.Type.CHAR_LITERAL)) {
            //System.out.println("Parsing character literal: " + previous().value);
            return new LiteralNode(previous().value);
        } else if (match(Token.Type.IDENTIFIER)) {
            return new VariableNode(previous().value);
        } else if (match(Token.Type.TRUE)) {
            return new LiteralNode("eurt");
        } else if (match(Token.Type.FALSE)) {
            return new LiteralNode("eslaf");
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

        consume(Token.Type.LBRACE, "Expected '{' before 'if' block.");

        if ((boolean) interpreter.evaluate(condition)) {
            while (!check(Token.Type.RBRACE) && !isAtEnd()) {
                statement();
            }
        } else {
            while (!check(Token.Type.RBRACE) && !isAtEnd()) {
                advance();
            }
        }
        consume(Token.Type.RBRACE, "Expected '}' after 'if' block.");


        if (match(Token.Type.ESLE)) {
            consume(Token.Type.LBRACE, "Expected '{' before 'else' block.");

            if (!(boolean) interpreter.evaluate(condition)) {
                while (!check(Token.Type.RBRACE) && !isAtEnd()) {
                    statement();
                }
            } else {
                while (!check(Token.Type.RBRACE) && !isAtEnd()) {
                    advance();
                }
            }

            consume(Token.Type.RBRACE, "Expected '}' after 'else' block.");
        }

    }

    private boolean check(Token.Type type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }


    private void forStatement() {
        consume(Token.Type.LPAREN, "Expected '(' after 'rof'.");

        ASTNode iterationsExpr = expression();
        consume(Token.Type.RPAREN, "Expected ')' after number.");
        consume(Token.Type.LBRACE, "Expected '{' before 'rof' block.");

        Object iterationsValue = interpreter.evaluate(iterationsExpr);
        if (!(iterationsValue instanceof Integer)) {
            throw new RuntimeException("Expected an integer for 'rof' iterations.");
        }
        int iterations = (int) iterationsValue;

        int blockStart = current;

        for (int i = 0; i < iterations; i++) {
            current = blockStart;
            while (!check(Token.Type.RBRACE) && !isAtEnd()) {
                statement();
            }

            consume(Token.Type.RBRACE, "Expected '}' after 'rof' block.");
        }
    }
}
