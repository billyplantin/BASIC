import java.util.LinkedList;

public class ReadNode extends StatementNode{

    private LinkedList<VariableNode> variableNodes;

    public ReadNode(LinkedList<VariableNode> variableNodes){
        this.variableNodes = variableNodes;
    }

    public LinkedList<VariableNode> getVariableNodes() {
        return variableNodes;
    }

    @Override
    public String toString() {
        return "ReadNode{" +
                "variableNodes=" + variableNodes +
                '}';
    }
}
