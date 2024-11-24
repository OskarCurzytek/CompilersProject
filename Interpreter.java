import java.util.HashMap;

class Interpreter {
    private HashMap<String, Object> symbolTable = new HashMap<>();

    public Object evaluate(ASTNode node) {
        if (node instanceof LiteralNode) {
            return parseLiteral(((LiteralNode) node).value);
        } else if (node instanceof VariableNode) {
            String name = ((VariableNode) node).name;
            if (!symbolTable.containsKey(name)) {
                throw new RuntimeException("Undefined variable: " + name);
            }
            return symbolTable.get(name);
        } else if (node instanceof BinaryOperationNode) {
            BinaryOperationNode binaryNode = (BinaryOperationNode) node;
            Object left = evaluate(binaryNode.left);
            Object right = evaluate(binaryNode.right);

            switch (binaryNode.operator.type) {
                case PLUS:
                    return ((double) left) + ((double) right);
                case MINUS:
                    return ((double) left) - ((double) right);
                case STAR:
                    return ((double) left) * ((double) right);
                case SLASH:
                    if ((double) right == 0) {
                        throw new RuntimeException("Division by zero");
                    }
                    return ((double) left) / ((double) right);
                default:
                    throw new RuntimeException("Unsupported operator: " + binaryNode.operator.type);
            }
        }
        throw new RuntimeException("Unknown ASTNode type");
    }

    private Object parseLiteral(String value) {
        if (value.matches("\\d+\\.\\d+")) {
            return Double.parseDouble(value);  // Parse doubles
        } else if (value.matches("\\d+")) {
            return (double) Integer.parseInt(value);  // Parse integers as doubles for simplicity
        }
        throw new RuntimeException("Invalid literal: " + value);
    }

    public void assignVariable(String name, Object value) {
        symbolTable.put(name, value);
    }

    public void printSymbolTable() {
        System.out.println("Symbol Table: " + symbolTable);
    }
}
