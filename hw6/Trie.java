import edu.princeton.cs.algs4.Stack;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Trie {
    static class Node {
        Node[] children = new Node[26];
        boolean isTerminal;
        List<String> values;
        Node() {
            values = new LinkedList<>();
        }
    }
    private Node root;
    private Board board;

    Trie(Board board) {
        this.board = board;
    }

    void insert(String str) {
        root = insertHelper(str, parseKeys(str), 0, root);
    }

    private String parseKeys(String str) {
        return str.toLowerCase();
    }

    private int key(String keys, int index) {
        char c = keys.charAt(index);
        if (c == 'é' || c == 'ê' || c == 'è' || c == 'ë') {
            c = 'e';
        }
        return c - 'a';
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

    private Stack<List<Node>> getLevels() {
        Stack<List<Node>> levels = new Stack<>();
        List<Node> level = new LinkedList<>();
        for (int i = 0; i < 26; i++) {
            Node child = root.children[i];
            if (child != null) {
                level.add(child);
            }
        }
        while (!level.isEmpty()) {
            levels.push(level);
            level = new LinkedList<>();
            List<Node> lastLevel = levels.peek();
            for (Node node: lastLevel) {
                for (int i = 0; i < 26; i++) {
                    Node child = node.children[i];
                    if (child != null) {
                        level.add(child);
                    }
                }
            }
        }
        return levels;
    }

    List<String> topKWords(int k) {
        List<String> res = new LinkedList<>();
        Stack<List<Node>> levels = getLevels();
        while (!levels.isEmpty()) {
            for (Node node: levels.pop()) {
                if (node.isTerminal) {
                    for (String value: node.values) {
                        res.add(value);
                        if (res.size() >= k) {
                            return res;
                        }
                    }
                }
            }
        }
        return res;
    }
}
