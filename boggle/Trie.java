/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Trie {
    private TrieNode root; // root为空，下面管26个字符


    /**
     * 如果插进去的是已经存在的，也没有问题
     *
     * @param word
     */
    public void insert(String word) {
        char[] chars = word.toCharArray();
        int len = chars.length;
        TrieNode curNode = root;
        for (int i = 0; i < len; ++i) {
            // 这个是到此处之后就没有匹配的了
            if (curNode.getChildren(chars[i]) == null) {
                for (int j = i; j < len - 1; ++j) {
                    TrieNode node = new TrieNode(chars[j], false);
                    curNode.setChildren(node);
                    curNode = node;
                }
                TrieNode keyNode = new TrieNode(chars[len - 1], true);
                curNode.setChildren(keyNode);
                return; // 记得这里结束之后就要return了
            }
            else {
                curNode = curNode.getChildren(chars[i]);
            }
        }
    }

    public boolean isPrefix(String query) {
        char[] chars = query.toCharArray();
        int len = chars.length;
        TrieNode curNode = root;
        for (int i = 0; i < len; ++i) {
            TrieNode children = curNode.getChildren(chars[i]);
            if (children == null) return false;
            curNode = children;
        }
        // 到了这一步说明整个query都在字典树里面，可能是前缀了
        return true;
    }

    public boolean contains(String query) {
        char[] chars = query.toCharArray();
        int len = chars.length;
        TrieNode curNode = root;
        for (int i = 0; i < len; ++i) {
            TrieNode children = curNode.getChildren(chars[i]);
            if (children == null) return false;
            curNode = children;
        }
        // 到了这一步说明整个query都在字典树里面，可能是前缀了
        return curNode.isKey();
    }

    public Trie() {
        root = new TrieNode('R', false);
    }
}
