public class VariableNode extends Node{

    //Member
    String varName;

    //Constructor
    public VariableNode (String varName){
        this.varName = varName;
    }

    //Accessor
    public String getVarName() {
        return varName;
    }


    @Override
    public String toString() {
        return "VariableNode{" +
                "varName='" + varName + '\'' +
                '}';
    }
}
