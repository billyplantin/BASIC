import org.junit.jupiter.api.Test;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InterpreterTest {

    @Test
    public void testInterpretReadNode() {
        // Create an instance of Interpreter
        Interpreter interpreter = new Interpreter();

        // Add some test data to the integerVariables map
        interpreter.getIntegerVariables().put("x", 0);
        interpreter.getFloatVariables().put("y%", 0.0f);
        interpreter.getStringVariables().put("z$", "");

        // Create a ReadNode with some variable nodes
        LinkedList<VariableNode> variables = new LinkedList<>();
        variables.add(new VariableNode("x"));
        variables.add(new VariableNode("y%"));
        variables.add(new VariableNode("z$"));
        ReadNode readNode = new ReadNode(variables);

        // Invoke the interpretReadNode method
        interpreter.InterpretReadNode(readNode);

        // Check if the interpreter data types were updated correctly
        assertEquals(0, interpreter.getIntegerVariables().get("x"));
        assertEquals(0.0f, interpreter.getFloatVariables().get("y%"));
        assertEquals("", interpreter.getStringVariables().get("z$"));

        // Test with a variable that doesn't exist
        LinkedList<VariableNode> invalidVariables = new LinkedList<>();
        invalidVariables.add(new VariableNode("invalid"));
        ReadNode invalidReadNode = new ReadNode(invalidVariables);
        // Ensure that an IllegalArgumentException is thrown
        assertThrows(IllegalArgumentException.class, () -> interpreter.InterpretReadNode(invalidReadNode));
    }
}

