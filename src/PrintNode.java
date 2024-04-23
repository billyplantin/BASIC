import java.util.LinkedList;

public class PrintNode extends StatementNode{

    private LinkedList<Node> printNodes;

    public PrintNode (LinkedList<Node> printNodes){
        this.printNodes = printNodes;
    }
    public LinkedList<Node> getPrintNodes() {
        return printNodes;
    }

    @Override
    public String toString() {
        return "PrintNode{" +
                "printNodes=" + printNodes +
                '}';
    }
}
