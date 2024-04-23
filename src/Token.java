public class Token {

    enum tokenType { WORD, NUMBER, ENDOFLINE,
        IF, THEN, FUNCTION, WHILE, END, PRINT,READ, INPUT, DATA,
        GOSUB, FOR, TO, STEP, NEXT, RETURN, STRINGLITERAL, LESSTHANOREQUAL,
        GREATERTHANOREQUAL, NOTEQUALS, EQUALS, LESSTHAN, GREATERTHAN, LPAREN,
        RPAREN, PLUS, MINUS, DIVIDE, MULTIPLY, LABEL, COMMA }

    private tokenType type;

    private String value;

    private int lineNum;

    private int characterPosition;

    /*
    2 constructors
    One holds token type, value, line number, character position
    Other constructor holds token type, line number, character position
     */

    //with value
    public Token (String value, tokenType type, int lineNum, int characterPos)
    {
        this.value = value;
        this.type = type;
        this.lineNum = lineNum;
        this.characterPosition = characterPos;

    }

    //without value
    public Token (tokenType type, int lineNum, int characterPos)
    {
        this.type = type;
        this.lineNum = lineNum;
        this.characterPosition = characterPos;

    }

    // Getters & Setters


    public String getValue() {
        return value;
    }

    public tokenType getType() {
        return type;
    }

    public int getCharacterPos() {
        return characterPosition;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(tokenType type) {
        this.type = type;
    }

    public void setCharacterPos(int characterPos) {
        this.characterPosition = characterPos;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    //ToString Method builds string for both possible outputs
    @Override
    public String toString() {

        if (value == null){
            return "Line Number: " + lineNum + ", Character Position: " + characterPosition +
                    ", Token Type: " + type;
        } else
            return "Line Number: " + lineNum + ", Character Position: " + characterPosition +
                    ", Token Type: " + "(" + type + ")" + ", Value: " + "(" + value + ")";
    }
}


