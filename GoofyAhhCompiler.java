import java.util.List;

public class GoofyAhhCompiler {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java GoofyAhhCompiler <file.txt>");
            return;
        }

        String fileName = args[0];
        String sourceCode = FileReader.readFile(fileName);
        if (sourceCode == null) {
            return;
        }

        Lexer lexer = new Lexer(sourceCode);
        List<Token> tokens;
        try {
            tokens = lexer.tokenize();
        } catch (RuntimeException e) {
            System.err.println("Lexing error: " + e.getMessage());
            return;
        }

        Parser parser = new Parser(tokens);
        try {
            parser.parse();
        } catch (RuntimeException e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }
}
