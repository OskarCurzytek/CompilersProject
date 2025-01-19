import java.util.HashMap;
import java.util.Objects;

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
                case OR:
                    ensureBooleanOperands(left, right, "ro");
                    return (Boolean) left || (Boolean) right;
                case AND:
                    ensureBooleanOperands(left, right, "dna");
                    return (Boolean) left && (Boolean) right;
                case PLUS:
                    ensureNumericOperands(left, right, "+");
                    return (left instanceof Integer && right instanceof Integer)
                            ? (Integer) left + (Integer) right
                            : ((Number) left).doubleValue() + ((Number) right).doubleValue();
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
                case LESS:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left < (Integer) right;
                    } else {
                        return (Double) left < (Double) right;
                    }
                case GREATER:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left > (Integer) right;
                    } else {
                        return (Double) left > (Double) right;
                    }
                case LESSEQ:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left <= (Integer) right;
                    } else {
                        return (Double) left <= (Double) right;
                    }
                case GREATEREQ:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left >= (Integer) right;
                    } else {
                        return (Double) left >= (Double) right;
                    }
                case EQEQ:
                    if (left instanceof Integer && right instanceof Integer) {
                        return ((Integer) left).equals((Integer) right);
                    } else if (left instanceof Boolean && right instanceof Boolean){
                        return ((Boolean) left).equals((Boolean) right);
                    }else{
                        return Objects.equals((Double) left, (Double) right);
                    }
                case NOTEQ:
                    if(left instanceof Integer && right instanceof Integer){
                        return !((Integer) left).equals((Integer) right);
                    }else if(left instanceof Boolean && right instanceof Boolean) {
                        return !((Boolean) left).equals((Boolean) right);
                    }else{
                        return !Objects.equals((Double) left, (Double) right);
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
        } else if (value.equals("eurt")) {
            return true; // Boolean true
        } else if (value.equals("eslaf")) {
            return false; // Boolean false
        } else if (value.matches("\\d+")) {
            return Integer.parseInt(value); // Parse integers as doubles for simplicity
        } else if (value.length() == 1) { // Match a single character inside single quotes
            //System.out.println("Parsing character literal: " + value); // Debug
            return value.charAt(0); // Extract the character from the single-quoted string
        }
        throw new RuntimeException("Invalid literal: " + value);
    }

    public void assignVariable(String name, Object value) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Variable name cannot be null or empty.");
        }
        if (!name.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            throw new RuntimeException("Invalid variable name: " + name);
        }
        // Further type validations for `value` can be added here if needed.
        symbolTable.put(name, value);
    }

    public void printSymbolTable() {
        System.out.println("Symbol Table: " + symbolTable);
    }

    private void ensureBooleanOperands(Object left, Object right, String operator) {
        if (!(left instanceof Boolean) || !(right instanceof Boolean)) {
            throw new RuntimeException("Operator '" + operator + "' requires boolean operands. Got: " +
                    left.getClass().getSimpleName() + " and " +
                    right.getClass().getSimpleName());
        }
    }

    private void ensureNumericOperands(Object left, Object right, String operator) {
        if (!(left instanceof Number) || !(right instanceof Number)) {
            throw new RuntimeException("Operator '" + operator + "' requires numeric operands. Got: " +
                    left.getClass().getSimpleName() + " and " +
                    right.getClass().getSimpleName());
        }
    }
}
