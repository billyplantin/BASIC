public class BooleanNode extends StatementNode {
    private Node leftExpression;
    private Token.tokenType operator;
    private Node rightExpression;

    public BooleanNode(Node leftExpression, Token.tokenType operator, Node rightExpression) {
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    public Node getLeftExpression() {
        return leftExpression;
    }

    public Token.tokenType getOperator() {
        return operator;
    }

    public Node getRightExpression() {
        return rightExpression;
    }

    @Override
    public String toString() {
        return "BooleanNode LEFT: " + leftExpression + " OP: (" + operator + ") RIGHT: " + rightExpression + "}";
    }
}
