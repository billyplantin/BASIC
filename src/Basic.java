import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;


public class Basic {

    public static void main(String[] args){
            if(args.length != 1)
            {
                System.err.println("Filename error");
                System.exit(1);
            }

        String filename = args[0];
        Lexer lexer = new Lexer(filename);
        LinkedList<Token> tokens = lexer.Lex();

        System.out.println("Lexing result:");
        for (Token token : tokens) {
            System.out.println(token);
        }

        // Create a parser instance
        Parser parser = new Parser(tokens);

        // Parse the expression and print the result
        System.out.println("\nParsing result:");
        Node result = parser.Parse();
        if (result != null) {
            System.out.println(result); // Print the AST or parsing result
        } else {
            System.out.println("Failed to parse the expression.");
        }


        Interpreter interpreter = new Interpreter();
        System.out.println("\nInterpreter result:");
        interpreter.interpret((StatementsNode) result);

    }

}
