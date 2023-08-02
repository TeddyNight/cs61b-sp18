import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;

public class Trie {
    class Node {
        Node[] children = new Node[26];
        boolean isTerminal;
        Set<String> values;
        Node() {
            values = new HashSet<>();
        }
    }
    private Node root = new Node();

    private int key(String pattern, int i) {
        pattern = parsePattern(pattern);
        return pattern.charAt(i) - 'a';
    }

    List<String> findPrefix(String prefix) {
        if (prefix == null) {
            return null;
        }
        Node node = root;
        for (int i = 0; i < prefix.length(); i++) {
            Node child = node.children[key(prefix, i)];
            if (child == null) {
                break;
            }
            node = child;
        }
        return getAll(node);
    }

    private List<String> getAll(Node node) {
        List<String> res = new LinkedList<>();
        if (node.isTerminal) {
            res.addAll(node.values);
        }
        for (Node child: node.children) {
            if (child == null) {
                continue;
            }
            res.addAll(getAll(child));
        }
        return res;
    }

    private String parsePattern(String pattern) {
        pattern = pattern.toLowerCase();
        StringBuilder res = new StringBuilder();
        for (char c: pattern.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                res.append(c);
            }
        }
        return res.toString();
    }

    void insert(String pattern) {
        root = insert(root, pattern, 0);
    }

    private Node insert(Node node, String pattern, int i) {
        if (node == null) {
            node = new Node();
        }
        if (i == parsePattern(pattern).length()) {
            node.isTerminal = true;
            node.values.add(pattern);
            return node;
        }
        node.children[key(pattern, i)] = insert(node.children[key(pattern, i)], pattern, i + 1);
        return node;
    }
}
