import java.util.List;

public class GoofyAhhCompiler {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java GoofyAhhCompiler <file.txt>");
            return;
        }

        // Step 1: Read the file
        String fileName = args[0];
        String sourceCode = FileReader.readFile(fileName);
        if (sourceCode == null) {
            return; // File could not be read
        }

        // Step 2: Lexical Analysis
        Lexer lexer = new Lexer(sourceCode);
        List<Token> tokens;
        try {
            tokens = lexer.tokenize();
        } catch (RuntimeException e) {
            System.err.println("Lexing error: " + e.getMessage());
            return;
        }

        // Step 3: Parsing and Interpretation
        Parser parser = new Parser(tokens);
        try {
            parser.parse();
        } catch (RuntimeException e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }
}
