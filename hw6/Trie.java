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
        List<Path> paths;
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
                child.paths = new LinkedList<>();
                level.add(child);
                for (Position p: board.getPos((char) (i + 'a'))) {
                    child.paths.add(new Path(p));
                }
            }
        }
        while (!level.isEmpty()) {
            levels.push(level);
            level = new LinkedList<>();
            List<Node> lastLevel = levels.peek();
            for (Node node: lastLevel) {
                Map<Character, List<Path>> childPaths = new HashMap<>();
                for (Path p: node.paths) {
                    for (Position neighbor: board.getNeighbor(p.getLastPos())) {
                        if (p.hasVisited(neighbor)) {
                            continue;
                        }
                        char c = board.getChar(neighbor);
                        List<Path> paths = childPaths.get(c);
                        if (paths == null) {
                            paths = new LinkedList<>();
                            childPaths.put(c, paths);
                        }
                        paths.add(new Path(p).appendPos(neighbor));
                    }
                }
                for (int i = 0; i < 26; i++) {
                    Node child = node.children[i];
                    if (child != null) {
                        child.paths = childPaths.get((char) (i + 'a'));
                        if (child.paths != null) {
                            level.add(child);
                        }
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
