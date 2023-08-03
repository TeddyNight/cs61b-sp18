import java.util.List;
import java.util.LinkedList;

public class Trie {
    class Node {
        Node[] children = new Node[27];
        boolean isTerminal;
        List<String> values;
        Node() {
            values = new LinkedList<>();
        }
    }
    private Node root = new Node();

    private int key(String pattern, int i) {
        if (pattern.charAt(i) == ' ') {
            return 26;
        }
        return pattern.charAt(i) - 'a';
    }

    private Node getNode(String pattern) {
        Node node = root;
        for (int i = 0; i < pattern.length(); i++) {
            Node child = node.children[key(pattern, i)];
            if (child == null) {
                return null;
            }
            node = child;
        }
        return node;
    }
    List<String> find(String pattern) {
        pattern = GraphDB.cleanString(pattern);
        Node node = getNode(pattern);
        if (node != null && node.isTerminal) {
            return node.values;
        }
        return null;
    }
    List<String> findPrefix(String prefix) {
        if (prefix == null) {
            return new LinkedList<>();
        }
        prefix = GraphDB.cleanString(prefix);
        if (prefix.isEmpty()) {
            List<String> res = new LinkedList<>();
            if (root.isTerminal) {
                res.addAll(root.values);
            }
            return res;
        }
        return findAll(getNode(prefix));
    }

    private List<String> findAll(Node node) {
        List<String> res = new LinkedList<>();
        if (node == null) {
            return res;
        }
        if (node.isTerminal) {
            res.addAll(node.values);
        }
        for (Node child: node.children) {
            res.addAll(findAll(child));
        }
        return res;
    }

    void insert(String content) {
        String pattern = GraphDB.cleanString(content);
        root = insert(root, pattern, content, 0);
    }

    private Node insert(Node node, String pattern, String content, int i) {
        if (node == null) {
            node = new Node();
        }
        if (i == pattern.length()) {
            node.isTerminal = true;
            node.values.add(content);
            return node;
        }
        node.children[key(pattern, i)] = insert(
                node.children[key(pattern, i)], pattern, content, i + 1);
        return node;
    }
}
