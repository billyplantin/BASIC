import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CodeHandler {

    //holds basic document
    private String document;

    private int index;

    //CONSTRUCTOR FOR CODEHANDLER, SETS INDEX TO 0, uses readallBytes for the document reading
    public CodeHandler (String filename){

        try {
            byte[] bytes = Files.readAllBytes(Path.of(filename));
            this.document = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            this.document = "";
        }
        this.index = 0;

    }

    /*
       PEEK: looks “i” characters ahead and returns that character;
       doesn’t move the index
    */
    public char peek(int i) {

        /*
        Since peek looks "i" characters ahead,
        I created int "pos" as resultant of index + i
         */
        int pos = index + i;

        if(pos < document.length()) {
            return document.charAt(pos);
        }
        else {
            return '\0'; //returns null character
        }
    }

    /*
       PEEKSTRING: returns a string of the next “i” characters but doesn’t move the index
    */
    public String peekString (int i){

        /*
        Since peek looks "i" characters ahead,
        I created int "pos" as resultant of index + i
         */
        int pos = index + i;

        if (pos <= document.length()) {
            //as long as pos doesn't exceed length of doc, it will return substring
            return document.substring(index, index + i);

        } else {
            return null; // Return null if out of bounds
        }

    }

    /*
      GETCHAR: returns the next character and moves the index
   */
    public char getChar(){
        /*
        if document still has tokens, next character is consumed and the index increments 1
         */
        if(!isDone()){
            char current = document.charAt(index);
            //System.out.println(current);
            index++;
            return current;
        } else {
            return '\0'; // Return null character if at the end of the document
        }
    }
    /*
      SWALLOW: moves the index ahead “i” positions
   */
    public void swallow (int i) {

        int pos = index + i;
        if (pos <= document.length()) {
            index += i;
        } else {
            index = document.length(); // Move to the end if out of bounds
        }
    }

    /*
        ISDONE: returns true if we are at the end of the document
     */
    public boolean isDone() {
        return index >= document.length();
    }

    /*
   REMAINDER: returns the rest of the document as a string
    */
    public String remainder (){

        return document.substring(index);
    }

    public String getDocument() {
        return document;
    }
}
