public class GosubStatementNode extends StatementNode {
    private String identifier; //  identifier associated with  GOSUB statement

    public GosubStatementNode(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "GosubStatementNode{" +
                "identifier='" + identifier + '\'' +
                '}';
    }
}
