import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private String input;
    private int pos = 0;

    private static final Pattern tokenPatterns = Pattern.compile(
            "\\s*(?:(tni|elbuod|loob|rahc)|" +      // Reversed types
                    "(prit|fi|rof|esle)|" +                    // Other reversed keywords
                    "(eurt|eslaf)|" +                     // Boolean literals
                    "(ro|dna)|" +                         // Boolean operators
                    "([a-zA-Z_][a-zA-Z0-9_]*)|" +         // Identifiers
                    "(\\d+\\.\\d+|\\d+)|" +               // Numbers
                    "'(\\\\\\\\.|[^\\\\\\\\'])'|" +               // Character literals
                    "(==|!=|<=|>=|<|>)|" +                // Comparison operators
                    "(\\+|\\-|\\*|\\/|=|;|,|\\(|\\)|\\{|\\}))" // Symbols
    );

    public Lexer(String input_){
        this.input = input_;
    }

    public List<Token> tokenize(){
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = tokenPatterns.matcher(input);

        while(pos < input.length()){
            matcher.region(pos, input.length());
                if(matcher.lookingAt()){
                    String group = matcher.group();
                    if(matcher.group(1) != null){
                        switch (matcher.group(1)){
                            case "tni": tokens.add(new Token(Token.Type.TNI, "tni")); break;
                            case "elbuod": tokens.add(new Token(Token.Type.ELBUOD, "elbuod")); break;
                            case "loob": tokens.add(new Token(Token.Type.LOOB, "loob")); break;
                            case "rahc": tokens.add(new Token(Token.Type.RAHC, "rahc")); break;
                        }
                    }else if(matcher.group(2) != null){
                        switch (matcher.group(2)){
                            case "prit": tokens.add(new Token(Token.Type.PRIT, "prit")); break;
                            case "fi": tokens.add(new Token(Token.Type.FI, "fi")); break;
                            case "esle": tokens.add(new Token(Token.Type.ESLE, "esle")); break;
                            case "rof": tokens.add(new Token(Token.Type.ROF, "rof")); break;
                        }
                    }else if(matcher.group(3) != null){
                        switch (matcher.group(3)){
                            case "eurt": tokens.add(new Token(Token.Type.TRUE, "eurt")); break;
                            case "eslaf": tokens.add(new Token(Token.Type.FALSE, "eslaf")); break;
                        }
                    }else if(matcher.group(4) != null){
                        switch (matcher.group(4)){
                            case "ro": tokens.add(new Token(Token.Type.OR, "or")); break;
                            case "dna": tokens.add(new Token(Token.Type.AND, "and")); break;
                        }
                    }else if (matcher.group(5) != null) {
                        tokens.add(new Token(Token.Type.IDENTIFIER, matcher.group(5)));
                    }else if (matcher.group(6) != null) {
                        tokens.add(new Token(Token.Type.NUMBER, matcher.group(6)));
                    }else if(matcher.group(7) != null){
                        //System.out.println("Lexer matched CHAR_LITERAL: " + matcher.group(6));
                        tokens.add(new Token(Token.Type.CHAR_LITERAL, matcher.group(7)));
                    }else if (matcher.group(8) != null) {
                        switch (matcher.group(8)) {
                            case "==": tokens.add(new Token(Token.Type.EQEQ, "==")); break;
                            case "!=": tokens.add(new Token(Token.Type.NOTEQ, "!=")); break;
                            case "<=": tokens.add(new Token(Token.Type.LESSEQ, "<=")); break;
                            case ">=": tokens.add(new Token(Token.Type.GREATEREQ, ">=")); break;
                            case "<": tokens.add(new Token(Token.Type.LESS, "<")); break;
                            case ">": tokens.add(new Token(Token.Type.GREATER, ">")); break;
                        }
                    }else if (matcher.group(9) != null) {
                        switch (matcher.group(9)) {
                            case "+": tokens.add(new Token(Token.Type.PLUS, "+")); break;
                            case "-": tokens.add(new Token(Token.Type.MINUS, "-")); break;
                            case "*": tokens.add(new Token(Token.Type.STAR, "*")); break;
                            case "/": tokens.add(new Token(Token.Type.SLASH, "/")); break;
                            case "=": tokens.add(new Token(Token.Type.EQUALS, "=")); break;
                            case ";": tokens.add(new Token(Token.Type.SEMICOLON, ";")); break;
                            case ",": tokens.add(new Token(Token.Type.COMMA, ",")); break;
                            case "(": tokens.add(new Token(Token.Type.LPAREN, "(")); break;
                            case ")": tokens.add(new Token(Token.Type.RPAREN, ")")); break;
                            case "{": tokens.add(new Token(Token.Type.LBRACE, "{")); break;
                            case "}": tokens.add(new Token(Token.Type.RBRACE, "}")); break;
                        }
                    }
                    pos += group.length();
                }else{
                    throw new RuntimeException("Unexpected character at position " + pos);
                }

        }
        tokens.add(new Token(Token.Type.EOF, null));
//        for(Token token : tokens){
//            System.out.println(token);
//        }
        return tokens;
    }
}
