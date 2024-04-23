/*
Billy Plantin
ICSI 311
 */
import java.util.LinkedList;
import java.util.Optional;

public class Parser {

    private TokenManager tokenManager;

    public Parser (LinkedList<Token> tokens){
        this.tokenManager = new TokenManager(tokens);
    }

    public Node Parse(){
            return Statements();
    }
    public boolean AcceptSeparators() {
        boolean foundSeparator = false;

        while (tokenManager.MoreTokens()) {
            /*
            We use Optional<Token> to store the result of tokenManager.Peek(0).
            checks if the optional contains a token using optionalToken.isPresent()
             */
            Optional<Token> optionalToken = tokenManager.peek(0);

            if (optionalToken.isPresent()) {
                Token nextToken = optionalToken.get();

                // Check if the next token is a separator (newline or semi-colon)
                if (nextToken.getType() == Token.tokenType.ENDOFLINE) {
                    tokenManager.MatchAndRemove(Token.tokenType.ENDOFLINE);
                    foundSeparator = true;
                } else {
                    break;
                }
            } else {
                // Handle the case where the peek method returns an empty optional
                break;
            }
        }

        return foundSeparator;
    }
    public Node expression(){
        //System.out.println("In Expression"); //testing purposes
        Node left = term();
        Optional<Token> currentToken = tokenManager.peek(0);
        /*
        While there are more tokens in the list, check for plus and minus operators to complete the expression
         */
        while (tokenManager.MoreTokens()){
            //Token manager peeks the first token in the list to check if a plus, else if it's a minus, else break
            //Returns left node at the end
            if(tokenManager.peek(0).get().getType() == Token.tokenType.PLUS){
                tokenManager.MatchAndRemove(Token.tokenType.PLUS);
                Node right = term();
                if(right == null){
                    return null;
                }
                left = new MathOpNode(left,Optional.of(right), MathOpNode.operator.PLUS);
            }
            else if (tokenManager.peek(0).get().getType() == Token.tokenType.MINUS){
                tokenManager.MatchAndRemove(Token.tokenType.MINUS);
                Node right = term();

                if (right == null){
                    return null;
                }
                left = new MathOpNode(left,Optional.of(right), MathOpNode.operator.MINUS);
            }
            else { break; } //plus or minus operator not found
        }
        return left;
    }
    private Node term(){
        //System.out.println("In term"); //testing purposes
        Node left = factor();
        //System.out.println(left.toString()); //testing
        if (left == null){
            return null;
        }
        //Optional<Token> currentToken = tokenManager.peek(0);
        /*
        While there are more tokens in the list, check for multiply or divide token to complete the term
         */
        while (tokenManager.MoreTokens()){
            //Token manager peeks the first token in the list to check if a multiply, else if it's a divide, else break
            //Returns left node at the end
            if(tokenManager.peek(0).get().getType() == Token.tokenType.MULTIPLY){
                tokenManager.MatchAndRemove(Token.tokenType.MULTIPLY);
                Node right = factor();
                if(right == null){
                    return null;
                }
                left = new MathOpNode(left,Optional.of(right), MathOpNode.operator.MULTIPLY);
                if (tokenManager.peek(0).get().getType() != (Token.tokenType.MULTIPLY) && tokenManager.peek(0).get().getType() != Token.tokenType.DIVIDE){
                    break;
                }
            }
            else if (tokenManager.peek(0).get().getType() == Token.tokenType.DIVIDE){
                tokenManager.MatchAndRemove(Token.tokenType.DIVIDE);
                Node right = factor();

                if (right == null){
                    return null;
                }
                left = new MathOpNode(left,Optional.of(right), MathOpNode.operator.DIVIDE);
                if (tokenManager.peek(0).get().getType() != (Token.tokenType.MULTIPLY) && tokenManager.peek(0).get().getType() != Token.tokenType.DIVIDE){
                    break;
                }
            }
            else { break; }//multiply or divide operator not found
        }
            return left;
    }

    private Node factor(){
       // System.out.println("In factor"); //testing purposes
        Optional<Token> currentToken = tokenManager.peek(0);
        //System.out.println(currentToken);
        //check if its a number
        if(currentToken.get().getType() == Token.tokenType.NUMBER){
            //System.out.println("Found Number"); //testing purposes
            tokenManager.MatchAndRemove(Token.tokenType.NUMBER);
            //parse the integer value or float value
            if (currentToken.get().getValue().contains(".")){
                float value = Float.parseFloat(currentToken.get().getValue());
                FloatNode result = new FloatNode(value);
                return result;
            }
            //else it must be an int
            else {
                int value = Integer.parseInt(currentToken.get().getValue());
                IntegerNode result = new IntegerNode(value);
                return result;
            }
        }
        else if(tokenManager.peek(0).get().getType() == Token.tokenType.LPAREN){
            tokenManager.MatchAndRemove(Token.tokenType.LPAREN);
            Node left = expression();

            if(tokenManager.peek(0).get().getType() != Token.tokenType.RPAREN){
                throw new RuntimeException("Syntax error: Expected ')' after expression.");
            }
            tokenManager.MatchAndRemove(Token.tokenType.RPAREN);
            return left;
        } //Handles variables found in parser
        else if (tokenManager.peek(0).get().getType() == Token.tokenType.WORD) {
            tokenManager.MatchAndRemove(Token.tokenType.WORD);
            return new VariableNode(currentToken.get().getValue());
        } else {
            throw new RuntimeException("Syntax error: Expected number or '('.");
        }
    }
    /*
     Statements() should call Statement() until Statement() fails (returns NULL)
     */
    private Node Statements() {
        //list to hold any number of statements
        LinkedList<StatementNode> statementsList = new LinkedList<>();

        //runs while statement list isnt null
        while (tokenManager.MoreTokens()){
            StatementNode statement = Statement();
            //AcceptSeparators used to void endofline tokens, allowing statements to continue
            AcceptSeparators();
            if (statementsList != null){
                statementsList.add(statement);
            }
            else {
                break;
            }
        }
        return new StatementsNode(statementsList);
    }
    /**
     * Parses a single statement.
     * A statement can be a print statement, an assignment, a read statement,
     * a data statement, or an input statement.
     * @return A StatementNode representing the parsed statement.
     */
    private StatementNode Statement() {
        // Check if there are more tokens available
        if (!tokenManager.MoreTokens()) {
            return null; // If no tokens available, return null
        }

        // Peek at the current token
        Optional<Token> current = tokenManager.peek(0);

        // Determine the type of statement based on the current token
        if (current.isPresent() && current.get().getType() == Token.tokenType.LABEL) {
            return LabeledStatement();
        }
        else {
            switch (current.get().getType()) {
                case PRINT:
                    return Print(); // Parse and return a print statement
                case READ:
                    return Read(); // Parse and return a read statement
                case DATA:
                    return Data(); // Parse and return a data statement
                case INPUT:
                    return Input(); // Parse and return an input statement
                case WORD:
                    return Assignment(); // Parse and return an assignment statement
                case RETURN:
                    return Return();
                case END:
                    return END();
                case GOSUB:
                    return Gosub();
                case FOR:
                    return For();
                case IF:
                    return If();
                case WHILE:
                    return While();
                default:
                    return null; // If the token does not match any statement type, return null
            }
        }
    }

    private StatementNode For() {
            tokenManager.MatchAndRemove(Token.tokenType.FOR);

            // Parse loop variable
            Optional<Token> varToken = tokenManager.peek(0);
            if (!varToken.isPresent() || varToken.get().getType() != Token.tokenType.WORD) {
                throw new RuntimeException("Expected loop variable after FOR keyword.");
            }
            VariableNode loopVariable = new VariableNode(varToken.get().getValue());
            tokenManager.MatchAndRemove(Token.tokenType.WORD);

            // Match equals sign
            tokenManager.MatchAndRemove(Token.tokenType.EQUALS);

            // Parse initial value expression
            Node initialValue = expression();

            // Match TO keyword
            tokenManager.MatchAndRemove(Token.tokenType.TO);

            // Parse limit expression
            Node limit = expression();

            // Default increment to 1 if STEP is not provided
            Node increment = new IntegerNode(1);

            // Check if there's a STEP keyword
            Optional<Token> stepToken = tokenManager.peek(0);
            if (stepToken.isPresent() && stepToken.get().getType() == Token.tokenType.STEP) {
                tokenManager.MatchAndRemove(Token.tokenType.STEP);
                increment = expression();
            }
            // Create and return ForNode
            return new ForStatementNode(loopVariable.getVarName(), initialValue, limit, increment);
        }

    private StatementNode If(){
        tokenManager.MatchAndRemove(Token.tokenType.IF);
        Node condition = parseBoolean();

        tokenManager.MatchAndRemove(Token.tokenType.THEN);

        String label;

        Token current = tokenManager.peek(0).get();
        if(current.getType() == Token.tokenType.WORD){
            label = current.getValue();
            tokenManager.MatchAndRemove(Token.tokenType.WORD);
        }
        else {
            throw new RuntimeException("Expected label after THEN");
        }
        return new IfNode(condition,label);
    }

    private StatementNode While() {
        tokenManager.MatchAndRemove(Token.tokenType.WHILE);
        Node condition = parseBoolean();

        String label;

        Token current = tokenManager.peek(0).get();
        if(current.getType() == Token.tokenType.WORD){
            label = current.getValue();
            tokenManager.MatchAndRemove(Token.tokenType.WORD);
        }
        else {
            throw new RuntimeException("Expected label after condition");
        }
        return new WhileNode(condition,label);
    }
    private StatementNode Return() {
        // Match and remove the RETURN token,Create and return the RETURN statement node
        tokenManager.MatchAndRemove(Token.tokenType.RETURN); return new ReturnStatementNode(); }
    private StatementNode END() { tokenManager.MatchAndRemove(Token.tokenType.END); return new EndNode(); }
    private StatementNode Gosub() {
        // Match and remove the GOSUB token
        tokenManager.MatchAndRemove(Token.tokenType.GOSUB);

        // Peek at the current token to get the identifier
        Optional<Token> identifierToken = tokenManager.peek(0);

        // Match and remove the identifier token
        tokenManager.MatchAndRemove(Token.tokenType.WORD);

        // Create and return the GOSUB statement node
        return new GosubStatementNode(identifierToken.get().getValue());
    }
    public StatementNode LabeledStatement(){
        Token current = tokenManager.peek(0).get();
        tokenManager.MatchAndRemove(Token.tokenType.LABEL);
        //creates new Labeled Statement Node with label node
         String labelTok = current.getValue();

         LabeledStatementNode label = new LabeledStatementNode(labelTok, Statement());

        return label;
    }
    //reads values into variables from user input
    private StatementNode Read() {
        LinkedList<VariableNode> variables = new LinkedList<>();

        tokenManager.MatchAndRemove(Token.tokenType.READ);

        while (true) {
            Token current = tokenManager.peek(0).get();

            if(current.getType() == Token.tokenType.WORD){
                variables.add((VariableNode) expression());
            }
            else {
                break;
            }
            //checks for commas, matches and removes them
            Optional<Token> comma = tokenManager.peek(0);
            if(comma.isPresent() && comma.get().getType() == Token.tokenType.COMMA){
                tokenManager.MatchAndRemove(Token.tokenType.COMMA);
            }
            else { break;}
        }
        return new ReadNode(variables);
    }
    //initializes a data structure with values
    private StatementNode Data() {
        //match and remove data token
        tokenManager.MatchAndRemove(Token.tokenType.DATA);

        //creates linked list for list of constant data that can be accessed with read
        LinkedList<Node> data = new LinkedList<>();

        while (true) {
            Token current = tokenManager.peek(0).get();

            //if a next token is string literal add it to the data list
            if (current.getType() == Token.tokenType.STRINGLITERAL) {
                tokenManager.MatchAndRemove(Token.tokenType.STRINGLITERAL);
                data.add(new StringNode(current.getValue()));
            }
            else {      // else if its a variable, or number, call expression
                data.add(expression());
            }
            //checks for commas, matches and removes them
            Optional<Token> comma = tokenManager.peek(0);
            if(comma.isPresent() && comma.get().getType() == Token.tokenType.COMMA){
                tokenManager.MatchAndRemove(Token.tokenType.COMMA);
            }
            else {
                break;
            }
        }
        return new DataNode(data);
    }

    private StatementNode Input() {
        //match and remove the input node
        tokenManager.MatchAndRemove(Token.tokenType.INPUT);
        LinkedList<Node> inputs = new LinkedList<>();
        while(true){
            Token current = tokenManager.peek(0).get();
            //if a next token is string literal add it to the data list
            if (current.getType() == Token.tokenType.STRINGLITERAL) {
                tokenManager.MatchAndRemove(Token.tokenType.STRINGLITERAL);
                inputs.add(new StringNode(current.getValue()));
                // else if its a variable,call expression
            } else if (current.getType()== Token.tokenType.WORD) {
                inputs.add(expression());
            } else {
                throw new RuntimeException("Illegal input syntax. String or Variable required");
            }
            //checks for commas, matches and removes them
            Optional<Token> comma = tokenManager.peek(0);
            if(comma.isPresent() && comma.get().getType() == Token.tokenType.COMMA){
                tokenManager.MatchAndRemove(Token.tokenType.COMMA);
            }
            else {
                break;
            }
        }
        return new InputNode(inputs);
    }
    /*
     accepts a print statement and creates a PrintNode or returns NULL:
     */
    private PrintNode Print (){
        //Match and remove print token
        tokenManager.MatchAndRemove(Token.tokenType.PRINT);
        //Calls PrintList to collect print nodes and add to list
        LinkedList<Node> printNodes = PrintList();
        return new PrintNode(printNodes);
    }
    /*
        accepts a print list.
        A print list consists of a comma separated list of expressions
     */
    private LinkedList<Node> PrintList(){
        LinkedList<Node> printNodes = new LinkedList<>();
       //loops through tokens to parse print list
        while (true){
            //create nodes for expressions, if its not null, add to the list
            Node expression = expression();
            if(expression != null) {
                printNodes.add(expression);
            }
            else { break; }
            //checks for commas, matches and removes them
            Optional<Token> comma = tokenManager.peek(0);
            if(comma.isPresent() && comma.get().getType() == Token.tokenType.COMMA){
                tokenManager.MatchAndRemove(Token.tokenType.COMMA);
            }
            else { break; }
        }
        return printNodes;
    }
    /*
    Assignment() parser method that accepts an assignment and returns an AssignmentNode or returns NULL.
     */
    private AssignmentNode Assignment (){
        Optional<Token> var = tokenManager.peek(0);
        //if there is no word token for some reason, return null
        if(!var.isPresent() || var.get().getType() != Token.tokenType.WORD ){
            return null;
        }
        //match and remove word, create variable node from it.
        tokenManager.MatchAndRemove(Token.tokenType.WORD);
        VariableNode variableNode = new VariableNode(var.get().getValue());

        //if equal sign is non-existent, throw an exception. If it is there, match and remove it
        if(tokenManager.peek(0).get().getType() != Token.tokenType.EQUALS){
            throw new RuntimeException("Expected equal sign before expression");
        }
        tokenManager.MatchAndRemove(Token.tokenType.EQUALS);
        //Calls expression for value after assignment, if value is null, return null
        Node value = expression();
        if(value == null){
            return null;
        }
        return new AssignmentNode(variableNode,value);
    }

    private BooleanNode parseBoolean(){
        Node left = expression();

        Token current = tokenManager.peek(0).get();

        Token.tokenType op;
        if (current.getType() == Token.tokenType.LESSTHAN){
            tokenManager.MatchAndRemove(Token.tokenType.LESSTHAN);
            op = Token.tokenType.LESSTHAN;
        } else if (current.getType() == Token.tokenType.GREATERTHAN) {
            tokenManager.MatchAndRemove(Token.tokenType.GREATERTHAN);
            op = Token.tokenType.GREATERTHAN;
        } else if (current.getType() == Token.tokenType.EQUALS) {
            tokenManager.MatchAndRemove(Token.tokenType.EQUALS);
            op = Token.tokenType.EQUALS;
        } else if (current.getType() == Token.tokenType.NOTEQUALS) {
            tokenManager.MatchAndRemove(Token.tokenType.NOTEQUALS);
            op = Token.tokenType.NOTEQUALS;
        } else if (current.getType() == Token.tokenType.LESSTHANOREQUAL) {
            tokenManager.MatchAndRemove(Token.tokenType.LESSTHANOREQUAL);
            op = Token.tokenType.LESSTHANOREQUAL;
        } else if (current.getType() == Token.tokenType.GREATERTHANOREQUAL) {
            tokenManager.MatchAndRemove(Token.tokenType.GREATERTHANOREQUAL);
            op = Token.tokenType.GREATERTHANOREQUAL;
        }
        else {
            throw new RuntimeException("Expected Boolean Operator");
        }

        Node right = expression();

        return new BooleanNode(left,op,right);
    }

}

