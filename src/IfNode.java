public class IfNode extends StatementNode {
    private Node condition;
    private String label;

    public IfNode(Node condition, String label) {
        this.condition = condition;
        this.label = label;
    }

    public Node getCondition() {
        return condition;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "condition=" + condition +
                ", label='" + label + '\'' +
                '}';
    }
}
