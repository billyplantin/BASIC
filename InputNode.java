import java.util.LinkedList;

public class InputNode extends StatementNode {

    private LinkedList<Node> inputs;

    public InputNode (LinkedList<Node> inputs){
        this.inputs = inputs;
    }
    public LinkedList<Node> getInputs() {
        return inputs;
    }

    @Override
    public String toString() {
        return "InputNode{" +
                inputs +
                '}';
    }
}
