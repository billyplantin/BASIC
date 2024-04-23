import java.util.LinkedList;
import java.util.*;

public class Lexer {

    //LINKED LIST THAT CONTAINS LIST OF TOKENS
    private LinkedList<Token> tokens;

    private CodeHandler codeHandler;

    private int characterPos;

    private int lineNum;

    private HashMap<String,Token.tokenType> keywords;
    private HashMap<String, Token.tokenType> twoCharSymbolMap;
    private HashMap<String, Token.tokenType> oneCharSymbolMap;

    //constructor for lexer
    //calls new code handler and sets list for collected tokens
    //starts line number at 1 and character position at 0
    public Lexer(String input) {
        codeHandler = new CodeHandler(input);
        tokens = new LinkedList<>();
        lineNum = 1;
        characterPos = 0;

        keywords = new HashMap<>();
        keywords.put("IF", Token.tokenType.IF);
        keywords.put("THEN", Token.tokenType.THEN);
        keywords.put("FUNCTION", Token.tokenType.FUNCTION);
        keywords.put("WHILE", Token.tokenType.WHILE);
        keywords.put("END", Token.tokenType.END);
        keywords.put("PRINT", Token.tokenType.PRINT);
        keywords.put("READ", Token.tokenType.READ);
        keywords.put("INPUT", Token.tokenType.INPUT);
        keywords.put("DATA", Token.tokenType.DATA);
        keywords.put("GOSUB", Token.tokenType.GOSUB);
        keywords.put("FOR", Token.tokenType.FOR);
        keywords.put("TO", Token.tokenType.TO);
        keywords.put("STEP", Token.tokenType.STEP);
        keywords.put("NEXT", Token.tokenType.NEXT);
        keywords.put("RETURN", Token.tokenType.RETURN);

        twoCharSymbolMap = new HashMap<>();
        twoCharSymbolMap.put("<=", Token.tokenType.LESSTHANOREQUAL);
        twoCharSymbolMap.put(">=", Token.tokenType.GREATERTHANOREQUAL);
        twoCharSymbolMap.put("<>", Token.tokenType.NOTEQUALS);

        oneCharSymbolMap = new HashMap<>();
        oneCharSymbolMap.put("<", Token.tokenType.LESSTHAN);
        oneCharSymbolMap.put(">", Token.tokenType.GREATERTHAN);
        oneCharSymbolMap.put("=", Token.tokenType.EQUALS);
        oneCharSymbolMap.put("+", Token.tokenType.PLUS);
        oneCharSymbolMap.put("-", Token.tokenType.MINUS);
        oneCharSymbolMap.put("/", Token.tokenType.DIVIDE);
        oneCharSymbolMap.put("*", Token.tokenType.MULTIPLY);
        oneCharSymbolMap.put("(", Token.tokenType.LPAREN);
        oneCharSymbolMap.put(")", Token.tokenType.RPAREN);
        oneCharSymbolMap.put(",", Token.tokenType.COMMA);


    }

    public LinkedList<Token> Lex() {
        while (!codeHandler.isDone()) {

            Token symbol = ProcessSymbol();
            if (symbol != null) {
                tokens.add(symbol);
            } else {
                char nextChar = codeHandler.peek(0); //used to peek through string input

                //If the character is a space or tab, we will just move past it (increment position)
                if (nextChar == ' ' || nextChar == '\t') {
                    codeHandler.getChar();
                    characterPos++;
                }
                //If the character is a linefeed (\n), we will create a new EOL token with no “value” and add it to token list.
                else if (nextChar == '\n') {
                    tokens.add(new Token(Token.tokenType.ENDOFLINE, lineNum, characterPos));
                    codeHandler.getChar();
                    lineNum++;
                    characterPos = 0;
                }
                //If the character is a carriage return (\r), we will ignore it.
                else if (nextChar == '\r') {
                    codeHandler.getChar();
                }
                //If the character is a letter, process the word
                else if (Character.isLetter(nextChar)) {
                    Token wordTok = processWord();
                    tokens.add(wordTok);
                }
                //If character is a number, process the number
                else if (Character.isDigit(nextChar) || nextChar == '.') {
                    Token numTok = processNum();
                    tokens.add(numTok);
                } else if (nextChar == '"') {
                    HandleStringLiteral();
                }
                //if unrecognized, throw an exception
                else {
                    throw new RuntimeException("Character Unrecognized! @ Line: " + lineNum
                            + ", Position: " + characterPos);
                }

            }
        }
            return tokens;

    }

    /*
 PROCESS WORD:  method that returns a Token.
 It accepts letters, digits and underscores (_) and make a String of them.
 When it encounters a character that is NOT one of those, it stops and makes a “WORD” token,
 setting the value to be the String it has accumulated
  */
        private Token processWord () {

            StringBuilder wordBuilder = new StringBuilder();

            while (!codeHandler.isDone() && (Character.isLetter(codeHandler.peek(0))
                    || Character.isDigit(codeHandler.peek(0)) || codeHandler.peek(0) == '_')) {

                wordBuilder.append(codeHandler.getChar());
                characterPos++;

            }

            //Allows words to end with $ or %
            if (!codeHandler.isDone() && (codeHandler.peek(0) == '$' || codeHandler.peek(0) == '%')){
                wordBuilder.append(codeHandler.getChar());
                characterPos++;

            } else if (!codeHandler.isDone() && codeHandler.peek(0) == ':') {
                wordBuilder.append(codeHandler.getChar());
                characterPos++;
                String word = wordBuilder.toString();
                return new Token(word, Token.tokenType.LABEL, lineNum, characterPos);
            }
                String word = wordBuilder.toString();;

                //if word matches a keyword in the hashmap
                if(keywords.containsKey(word)){
                    return new Token(keywords.get(word),lineNum,characterPos);

                }

            return new Token(word, Token.tokenType.WORD, lineNum, characterPos);
        }

    /*
PROCESS NUM: similar to ProcessWord, but accepts 0-9 and one “.”
*/
    private Token processNum(){
        StringBuilder numberBuilder = new StringBuilder();
        //integer to hold amount of decimals
        int decimalCount = 0;

        while (!codeHandler.isDone() && (Character.isDigit(codeHandler.peek(0)) || codeHandler.peek(0) == '.')) {
            char current = codeHandler.peek(0);
            //increments decimal count if found, but breaks if it finds more than one
            if (current == '.'){
                decimalCount++;
                if (decimalCount > 1){
                    break;
                }
            }

            numberBuilder.append(codeHandler.getChar());
            characterPos++;
        }

        return new Token(numberBuilder.toString(), Token.tokenType.NUMBER, lineNum, characterPos);
    }

    private Token ProcessSymbol(){
        String twoCharSymbol = codeHandler.peekString(2);
        String oneCharSymbol = codeHandler.peekString(1);

        if (twoCharSymbolMap.containsKey(twoCharSymbol)){

            codeHandler.getChar();
            characterPos += 2;
            codeHandler.getChar();
            return new Token(twoCharSymbolMap.get(twoCharSymbol),lineNum,characterPos);
        }
        else if (oneCharSymbolMap.containsKey(oneCharSymbol)) {

            codeHandler.getChar();
            characterPos++;

            return new Token(oneCharSymbolMap.get(oneCharSymbol),lineNum,characterPos);
        }
        else {return null;}

    }

    private void HandleStringLiteral(){
        StringBuilder str = new StringBuilder();

        //Consume double quotes
        codeHandler.getChar();

        while (!codeHandler.isDone()){
            char nextChar = codeHandler.getChar();
            //if codeHandler reads a ending double quote, check if there is an escape
            if(nextChar == '"'){

                //check for escape \ before ending double quote.
                if (codeHandler.peek(-1) != '\\'){
                    break;
                }
            }
            str.append(nextChar); characterPos++;
        }
        tokens.add(new Token(str.toString(), Token.tokenType.STRINGLITERAL,lineNum,characterPos));


    }

}
