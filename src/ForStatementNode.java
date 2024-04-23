public class ForStatementNode extends StatementNode {
    private String variable;
    private Node initialValue;
    private Node limit;
    private Node increment;


    // Getters for the fields

    public ForStatementNode(String variable, Node initialValue, Node limit, Node increment) {
        this.variable = variable;
        this.initialValue = initialValue;
        this.limit = limit;
        this.increment = increment;
    }

    public String getVariable() {
        return variable;
    }

    public Node getInitialValue() {
        return initialValue;
    }

    public Node getLimit() {
        return limit;
    }

    public Node getIncrement() {
        return increment;
    }


    @Override
    public String toString() {
        return "ForStatementNode{" +
                "variable='" + variable + '\'' +
                ", initialValue=" + initialValue +
                ", limit=" + limit +
                ", increment=" + increment +
                '}';
    }
}
