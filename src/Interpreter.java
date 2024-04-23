import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Interpreter {
    private Map<String, Integer> integerVariables;
    private Map<String, Float> floatVariables;
    private Map<String, String> stringVariables;
    private Map<String, LabeledStatementNode> labelMap;
    private LinkedList<Node> dataStatements;

    public Interpreter() {
        integerVariables = new HashMap<>();
        floatVariables = new HashMap<>();
        stringVariables = new HashMap<>();
        labelMap = new HashMap<>();
        dataStatements = new LinkedList<>();
    }
    public void interpret(StatementsNode root) {
        // Pre-process the AST to find DATA statements and store them
        collectDataStatements(root);

        // Pre-process the AST to find labeled statements and store them
        collectLabels(root);

        for (StatementNode statement : root.getStatementNodes()){
            if(statement instanceof ReadNode){
                    InterpretReadNode((ReadNode)statement );
            }
            else if(statement instanceof PrintNode){
                    InterpretPrintNode((PrintNode) statement);
            }
            else if(statement instanceof InputNode){
                    InterpretInputNode((InputNode) statement);
            }
            else if(statement instanceof AssignmentNode){
                    InterpretAssignmentNode((AssignmentNode)statement);
            }
            else if (statement instanceof GosubStatementNode) {

            }
            else if (statement instanceof ReturnStatementNode) {

            }
            else if (statement instanceof IfNode){

            }
            else if(statement instanceof WhileNode){

            }
            else if(statement instanceof ForStatementNode){

            }
            else if(statement instanceof NextNode){

            }
            else if(statement instanceof EndNode){

            }
        }
    }
    public void InterpretReadNode(ReadNode readNode){
        // Get the list of variables from the ReadNode
        LinkedList<VariableNode> variables = readNode.getVariableNodes();
        // Iterate over each variable in the list
        for(VariableNode vars: variables){
            // Get the variable name
            String varName = vars.getVarName();

            if(integerVariables.containsKey(varName)){
                //if data statement is empty, throw exception
                if (dataStatements.isEmpty()) {
                    throw new IllegalArgumentException("No data available for variable '" + varName + "'");
                }
                Node value = dataStatements.pop();
                //if data type does not match, throw exception
                if(!(value instanceof IntegerNode)){
                    throw new IllegalArgumentException("Data Type mismatch for variable '" + varName + "'. Expected Integer");
                }
                //set value to data value
                integerVariables.put(varName, ((IntegerNode) value).getValue());

            } else if (floatVariables.containsKey(varName)) {
                //if data statement is empty, throw exception
                if (dataStatements.isEmpty()) {
                    throw new IllegalArgumentException("No data available for variable '" + varName + "'");
                }
                Node value = dataStatements.pop();
                //if data type does not match, throw exception
                if(!(value instanceof FloatNode)){
                    throw new IllegalArgumentException("Data Type mismatch for variable '" + varName + "'. Expected Float");
                }
                //set value to data value
                floatVariables.put(varName,((FloatNode)value).getValue());
                
            } else if (stringVariables.containsKey(varName)) {

                //if data statement is empty, throw exception
                if (dataStatements.isEmpty()) {
                    throw new IllegalArgumentException("No data available for variable '" + varName + "'");
                }
                Node value = dataStatements.pop();
                //if data type does not match, throw exception
                if(!(value instanceof StringNode)){
                    throw new IllegalArgumentException("Data Type mismatch for variable '" + varName + "'. Expected String");
                }
                //set value to data value
                stringVariables.put(varName,((StringNode)value).getString());
                
            }else {
                // Handle variable not found error
                throw new IllegalArgumentException("Variable '" + varName + "' not found");
            }
        }

    }
    private void InterpretPrintNode(PrintNode printNode){
        //for each child of print
        for (Node statement : printNode.getPrintNodes()){
            if(statement instanceof VariableNode){
                String variableName = ((VariableNode) statement).getVarName();
                if(integerVariables.containsKey(variableName)){
                    System.out.println(integerVariables.get(variableName) + " ");
                } else if (floatVariables.containsKey(variableName)) {
                    System.out.println(floatVariables.get(variableName) + " ");
                } else if (stringVariables.containsKey(variableName)) {
                    System.out.println(stringVariables.get(variableName) + " ");
                }else {
                    // Handle variable not found error
                    System.out.print("Variable '" + variableName + "' not found ");
                }
            } else if (statement instanceof MathOpNode) {
                String value;
                MathOpNode mathOpNode = (MathOpNode) statement;
                value = String.valueOf(evaluateInt(mathOpNode));
                System.out.println(value + " ");
            }else {
                // For other types of nodes (e.g., ConstantNode), just print their string representation
                System.out.print(statement.toString() + " ");
            }
        }

    }
    private void InterpretInputNode(InputNode inputNode){

    }
    private void InterpretAssignmentNode(AssignmentNode assignmentNode){
            VariableNode variableNode = assignmentNode.getVariableNode();
            Node value = assignmentNode.getValue();
            int intVal = 0;
            Float floatVal = 0.0f;
            String stringVal = null;

            String variableName = variableNode.getVarName();

            //checks if the variable is declared as float
            boolean isFloat = variableName.endsWith("%");
            //checks if variable is declared as string
            boolean isString = variableName.endsWith("$");

            if(isFloat){
                floatVal = evaluateFloat(value);
                floatVariables.put(variableName,floatVal);
            }
            else if (isString){
                stringVal = evaluateString(value);
                stringVariables.put(variableName,stringVal);
            }
            else {
                intVal = evaluateInt(value);
                integerVariables.put(variableName,intVal);
            }
    }
    private String evaluateString(Node expression){
        if (expression instanceof VariableNode){
            String varName = ((VariableNode) expression).getVarName();
            if(stringVariables.containsKey(varName)){
                return stringVariables.get(varName);
            }else {
                throw new IllegalArgumentException("Variable '" + varName + "' not found");
            }
        }
        else if (expression instanceof StringNode){
            return ((StringNode) expression).getString();
        }else {
            throw new IllegalArgumentException("Unsupported node type: " + expression.getClass().getSimpleName());
        }
    }
    private int evaluateInt (Node expression){
        if (expression instanceof MathOpNode){
            MathOpNode mathOpNode = (MathOpNode) expression;
            int left = evaluateInt(mathOpNode.getLeft());
            int right = evaluateInt(mathOpNode.getRight().get());
            switch (mathOpNode.getOp()){
                case PLUS:
                    return left + right;
                case MULTIPLY:
                    return left * right;
                case MINUS:
                    return left - right;
                case DIVIDE:
                    return left / right;
                default: throw new IllegalArgumentException("Unknown operator: " + mathOpNode.getOp());

            }
        }
        else if (expression instanceof VariableNode ){
            String variableName = ((VariableNode) expression).getVarName();
            if(integerVariables.containsKey(variableName)){
                return integerVariables.get(variableName);
            }
        }
        else if (expression instanceof IntegerNode){
            return ((IntegerNode) expression).getValue();
        }
            throw new IllegalArgumentException("Unsupported expression type: " + expression.toString());

    }
    private float evaluateFloat(Node expression){
        //if expression is a math op node, recursively call function on left and right side,
        //perform the operation
        if (expression instanceof MathOpNode){
            MathOpNode mathOpNode = (MathOpNode) expression;
            Float left = evaluateFloat(mathOpNode.getLeft());
            Float right = evaluateFloat(mathOpNode.getRight().get());
            switch (mathOpNode.getOp()){
                case PLUS:
                    return left + right;
                case MULTIPLY:
                    return left * right;
                case MINUS:
                    return left - right;
                case DIVIDE:
                    return left / right;
                default: throw new IllegalArgumentException("Unknown operator: " + mathOpNode.getOp());

            }
        }
        else if (expression instanceof VariableNode ){
            String variableName = ((VariableNode) expression).getVarName();
            if (floatVariables.containsKey(variableName)){
                return floatVariables.get(variableName);
            }
        }
        else if (expression instanceof FloatNode){
            return ((FloatNode) expression).getValue();
        }
        throw new IllegalArgumentException("Unsupported expression type: " + expression.toString());
    }

    private void collectDataStatements(StatementsNode node) {
        // Iterate through each statement in the StatementsNode
        for (StatementNode statement : node.getStatementNodes()) {
            // Check if the statement is a DataNode
            if (statement instanceof DataNode) {
                // Cast the statement to a DataNode
                DataNode dataNode = (DataNode) statement;
                // Add the data values from the DataNode to the list
                dataStatements.addAll(dataNode.getData());
            }
        }
    }

    private void collectLabels(StatementsNode node) {
        // Iterate through each statement in the StatementsNode
        for (StatementNode statement : node.getStatementNodes()) {
            // Check if the statement is a LabelNode
            if (statement instanceof LabeledStatementNode) {
                // Cast the statement to a LabeledStatementNode
                LabeledStatementNode labeledStatement = (LabeledStatementNode) statement;
                // Add the data values from the DataNode to the list
                labelMap.put(labeledStatement.getLabel(), labeledStatement);
            }
        }
    }

    public Map<String, Integer> getIntegerVariables() {
        return integerVariables;
    }

    public Map<String, Float> getFloatVariables() {
        return floatVariables;
    }

    public Map<String, String> getStringVariables() {
        return stringVariables;
    }

    public Map<String, LabeledStatementNode> getLabelMap() {
        return labelMap;
    }

    public LinkedList<Node> getDataStatements() {
        return dataStatements;
    }

    //returns a random integer
    public static int random() {
        Random random = new Random();
        return random.nextInt();
    }

    // returns the leftmost N characters from the string
    public static String left(String data, int characters) {
        return data.substring(0, Math.min(characters, data.length()));
    }

    // returns the rightmost N characters from the string
    public static String right(String data, int characters) {
        int startIndex = Math.max(0, data.length() - characters);
        return data.substring(startIndex);
    }

    //returns the characters of the string, starting from the 2nd argument and taking the
    //3rd argument as the count; MID$(“Albany”,2,3) = “ban”
    public static String mid(String data, int start, int count) {
        int startIndex = Math.min(Math.max(0, start - 1), data.length());
        int endIndex = Math.min(startIndex + count, data.length());
        return data.substring(startIndex, endIndex);
    }

    //converts an int to a string
    public static String num(int value) {
        return String.valueOf(value);
    }

    //converts a float to a string
    public static String num(float value) {
        return String.valueOf(value);
    }

    // converts a string to an integer
    public static int val(String data) {
        return Integer.parseInt(data);
    }

    //converts a string to a float
    public static float val$(String data) {
        return Float.parseFloat(data);
    }



}


