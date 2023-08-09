import java.util.List;

public class Trie {
    static class Node {
        Node[] children = new Node[26];
        boolean isTerminal;
        List<String> values;
    }
    Node root;

    void insert(String str) {
        root = insertHelper(str, parseKeys(str), 0, root);
    }

    private String parseKeys(String str) {
        return str.replaceAll("[' ]", "").toLowerCase();
    }

    private int key(String keys, int index) {
        return keys.charAt(index) - 'a';
    }

    private Node insertHelper(String str, String keys, int index, Node node) {
        if (node == null) {
            node = new Node();
        }
        if (index >= keys.length()) {
            node.isTerminal = true;
            node.values.add(str);
        } else {
            int key = key(keys, index);
            node.children[key] = insertHelper(str, keys, index + 1, node.children[key]);
        }
        return node;
    }
}
