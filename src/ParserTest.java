import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;

public class ParserTest {

    @Test
    public void testParser() {
        // Define tokens for a simple script: "PRINT 1 + 2; x = 5; PRINT x;"
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token("PRINT", Token.tokenType.PRINT,0,0));
        tokens.add(new Token("1", Token.tokenType.NUMBER,0,0));
        tokens.add(new Token( "+",Token.tokenType.PLUS,0,0));
        tokens.add(new Token("2",Token.tokenType.NUMBER, 0,0));
        tokens.add(new Token( "\n",Token.tokenType.ENDOFLINE,0,0));
        tokens.add(new Token("xa",Token.tokenType.WORD, 0,0));
        tokens.add(new Token("=",Token.tokenType.EQUALS, 0,0));
        tokens.add(new Token( "5",Token.tokenType.NUMBER,0,0));
        tokens.add(new Token( "\n",Token.tokenType.ENDOFLINE,0,0));
        tokens.add(new Token( "PRINT",Token.tokenType.PRINT,0,0));
        tokens.add(new Token("xb",Token.tokenType.WORD,0,0 ));
        tokens.add(new Token( "\n",Token.tokenType.ENDOFLINE,0,0));

        // Create the parser
        Parser parser = new Parser(tokens);

        // Parse the script
        Node result = parser.Parse();

        // Ensure that the parser returns a non-null result
        assertNotNull(result);

        // Ensure that the result is a StatementsNode
        assertTrue(result instanceof StatementsNode);

        // Cast the result to StatementsNode
        StatementsNode statementsNode = (StatementsNode) result;

        // Ensure that the statements list is not null and contains the correct number of statements
        assertNotNull(statementsNode.getStatementNodes());
        assertEquals(3, statementsNode.getStatementNodes().size());

        // Check the first statement: PRINT 1 + 2;
        assertTrue(statementsNode.getStatementNodes().get(0) instanceof PrintNode);
        PrintNode printNode1 = (PrintNode) statementsNode.getStatementNodes().get(0);
        assertEquals(1, printNode1.getPrintNodes().size());

        // Check the second statement: x = 5;
        assertTrue(statementsNode.getStatementNodes().get(1) instanceof AssignmentNode);
        AssignmentNode assignmentNode = (AssignmentNode) statementsNode.getStatementNodes().get(1);
        assertTrue(assignmentNode.getVariableNode() instanceof VariableNode);
        assertEquals("xa", ((VariableNode) assignmentNode.getVariableNode()).getVarName());
        assertTrue(assignmentNode.getValue() instanceof IntegerNode);
        assertEquals(5, ((IntegerNode) assignmentNode.getValue()).getValue());

        // Check the third statement: PRINT x;
        assertTrue(statementsNode.getStatementNodes().get(2) instanceof PrintNode);
        PrintNode printNode2 = (PrintNode) statementsNode.getStatementNodes().get(2);
        assertEquals(1, printNode2.getPrintNodes().size());
        assertTrue(printNode2.getPrintNodes().get(0) instanceof VariableNode);
        assertEquals("xb", ((VariableNode) printNode2.getPrintNodes().get(0)).getVarName());
    }
}
