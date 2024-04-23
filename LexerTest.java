import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    @Test
    public void testLexer() {
        String input = "yooobro he11o: .41 1 11 \n for while + \"yooo\" <= ";
        Lexer lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.Lex();

    //Tests all functionalities for Lexer 1 & 2
        assertEquals(Token.tokenType.WORD, tokens.get(0).getType());
        assertEquals(Token.tokenType.LABEL, tokens.get(1).getType());
        assertEquals(Token.tokenType.NUMBER, tokens.get(2).getType());
        assertEquals("1", tokens.get(3).getValue());
        assertEquals(Token.tokenType.NUMBER, tokens.get(4).getType());
        assertEquals(Token.tokenType.ENDOFLINE, tokens.get(5).getType());
        assertEquals(Token.tokenType.FOR, tokens.get(6).getType());
        assertEquals(Token.tokenType.WHILE, tokens.get(7).getType());
        assertEquals(Token.tokenType.PLUS, tokens.get(8).getType());
        assertEquals(Token.tokenType.STRINGLITERAL, tokens.get(9).getType());
        assertEquals(Token.tokenType.LESSTHANOREQUAL, tokens.get(10).getType());




    }
}
