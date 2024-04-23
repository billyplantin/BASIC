public class AssignmentNode extends StatementNode {

    private VariableNode variableNode;

    //Members
    private Node value;

    //Constructor
    public AssignmentNode (VariableNode variableNode, Node value){

        this.variableNode = variableNode;
        this.value = value;
    }

    //Accessors
    public VariableNode getVariableNode() {
        return variableNode;
    }

    public Node getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "AssignmentNode{" +
                "variableNode=" + variableNode +
                ", value=" + value +
                '}';
    }
}
