abstract class ASTNode { }

class LiteralNode extends ASTNode {
    String value;

    LiteralNode(String value) {
        this.value = value;
    }
}

class VariableNode extends ASTNode {
    String name;

    VariableNode(String name) {
        this.name = name;
    }
}

class BinaryOperationNode extends ASTNode {
    ASTNode left;
    Token operator;
    ASTNode right;

    BinaryOperationNode(ASTNode left, Token operator, ASTNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
