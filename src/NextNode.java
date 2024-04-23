// NextStatementNode.java
public class NextNode extends StatementNode {
    private String variable;

    public NextNode(String variable) {
        this.variable = variable;
    }

    // Getter for the variable

    @Override
    public String toString() {
        return "NextStatementNode{" +
                "variable='" + variable + '\'' +
                '}';
    }
}
