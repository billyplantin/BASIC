public class WhileNode extends StatementNode {
    private Node condition;
    private String endLabel;

    public WhileNode(Node condition, String endLabel) {
        this.condition = condition;
        this.endLabel = endLabel;
    }

    public Node getCondition() {
        return condition;
    }

    public String getEndLabel() {
        return endLabel;
    }

    @Override
    public String toString() {
        return "WhileNode{" +
                "condition=" + condition +
                ", endLabel='" + endLabel + '\'' +
                '}';
    }
}
