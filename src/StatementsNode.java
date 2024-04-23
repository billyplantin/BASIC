import java.util.LinkedList;

public class StatementsNode extends StatementNode{

    //Member
    private LinkedList<StatementNode> statementNodes;

    //Constructor
    public StatementsNode (LinkedList<StatementNode> statementNodes){
        this.statementNodes = statementNodes;
    }

    //Accessor
    public LinkedList<StatementNode> getStatementNodes() {
        return statementNodes;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("StatementsNode{\n");
        for (StatementNode statementNode : statementNodes) {
            stringBuilder.append("\t").append(statementNode.toString()).append("\n");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();

    }
}
