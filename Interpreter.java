import java.util.HashMap;

class Interpreter {
    private HashMap<String, Object> symbolTable = new HashMap<>();

    public Object evaluate(ASTNode node) {
        if (node instanceof LiteralNode) {
            //System.out.println("Evaluating LiteralNode: " + ((LiteralNode) node).value); // Debug
            return parseLiteral(((LiteralNode) node).value);
        } else if (node instanceof VariableNode) {
            //System.out.println("Evaluating VariableNode: " + ((VariableNode) node).name); // Debug
            String name = ((VariableNode) node).name;
            if (!symbolTable.containsKey(name)) {
                throw new RuntimeException("Undefined variable: " + name);
            }
            return symbolTable.get(name);
        } else if (node instanceof BinaryOperationNode) {
            BinaryOperationNode binaryNode = (BinaryOperationNode) node;
            //System.out.println("Evaluating BinaryOperationNode: " + binaryNode.operator.type); // Debug
            Object left = evaluate(binaryNode.left);
            Object right = evaluate(binaryNode.right);
            //System.out.println("Left: " + left + ", Right: " + right); // Debug
            switch (binaryNode.operator.type) {
                case PLUS:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left + (Integer) right; // Integer addition
                    } else {
                        return ((Number) left).doubleValue() + ((Number) right).doubleValue(); // Convert to double for addition
                    }
                case MINUS:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left - (Integer) right; // Integer subtraction
                    } else {
                        return ((Number) left).doubleValue() - ((Number) right).doubleValue(); // Convert to double for subtraction
                    }
                case STAR:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left * (Integer) right; // Integer multiplication
                    } else {
                        return ((Number) left).doubleValue() * ((Number) right).doubleValue(); // Convert to double for multiplication
                    }
                case SLASH:
                    if (left instanceof Integer && right instanceof Integer) {
                        if ((Integer) right == 0) {
                            throw new RuntimeException("Division by zero");
                        }
                        return (Integer) left / (Integer) right; // Integer division
                    } else {
                        if (((Number) right).doubleValue() == 0) {
                            throw new RuntimeException("Division by zero");
                        }
                        return ((Number) left).doubleValue() / ((Number) right).doubleValue(); // Convert to double for division
                    }
                default:
                    throw new RuntimeException("Unsupported operator: " + binaryNode.operator.type);
            }
        }
        throw new RuntimeException("Unknown ASTNode type");
    }

    private Object parseLiteral(String value) {
        //System.out.println("parseLiteral received value: " + value);
        if (value.matches("\\d+\\.\\d+")) {
            return Double.parseDouble(value); // Parse doubles
        } else if (value.matches("\\d+")) {
            return Integer.parseInt(value); // Parse integers as doubles for simplicity
        } else if (value.length() == 1) { // Match a single character inside single quotes
            //System.out.println("Parsing character literal: " + value); // Debug
            return value.charAt(0); // Extract the character from the single-quoted string
        }
        throw new RuntimeException("Invalid literal: " + value);
    }

    public void assignVariable(String name, Object value) {
        //System.out.println("Assigning variable: " + name + " = " + value + " (type: " + value.getClass().getSimpleName() + ")");
        symbolTable.put(name, value);
    }

    public void printSymbolTable() {
        System.out.println("Symbol Table: " + symbolTable);
    }
}
