import java.util.Optional;

public class MathOpNode extends Node {

    enum operator {
        PLUS, MINUS, MULTIPLY, DIVIDE
    }

    private operator op;

    private Node left;

    private Optional<Node> right;
    //Constructor
    public MathOpNode(Node left, Optional<Node> right, operator op) {

        this.left = left;
        this.right = right;
        this.op = op;
    }

    //Read only accessors
    public Node getLeft() {
        return left;
    }

    public Optional<Node> getRight() {
        return right;
    }

    public operator getOp() {
        return op;
    }

    @Override
    public String toString() {
        return "MathOpNode{" + "LEFT: " + left + " OP: " + op + " RIGHT: " + right + "}";

    }
}
