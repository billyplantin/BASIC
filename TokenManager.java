import java.util.LinkedList;
import java.util.Optional;

public class TokenManager {

    private LinkedList <Token> tokenList;

    public TokenManager (LinkedList<Token> tokens){
        this.tokenList = tokens;
    }

    /*
       peek “j” tokens ahead and return the token if we aren’t past the end of the token list.
    */
    public Optional<Token> peek (int j) {

        if (j >= 0 && j < tokenList.size()) {
            return Optional.of(tokenList.get(j));
        }
        return Optional.empty();
    }

    /*
    returns true if the token list is not empty
    */
    public boolean MoreTokens() {
        return !tokenList.isEmpty();
    }

    /*
    looks at the head of the list.
    If the token type of the head is the same as what was passed in, remove that token from the list and return it.
    In all other cases, returns Optional.Empty(). You will use this extensively.
   */
    public Optional<Token> MatchAndRemove(Token.tokenType type) {

        if (!tokenList.isEmpty() && tokenList.getFirst().getType() == type) {
            return Optional.of(tokenList.removeFirst());
        }
        return Optional.empty();
    }

}
