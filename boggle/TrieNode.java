/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class TrieNode {
    private static final int R = 26;
    private TrieNode[] childrens = new TrieNode[R];
    private char value;
    private boolean isKey;

    public TrieNode(char value, boolean isKey) {
        this.value = value;
        this.isKey = isKey;
    }

    public void setChildren(TrieNode children) {
        childrens[children.getValue() - 'A'] = children;
    }

    public TrieNode getChildren(char children) {
        return childrens[children - 'A'];
    }

    public char getValue() {
        return value;
    }

    public boolean isKey() {
        return isKey;
    }
}
