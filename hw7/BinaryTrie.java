import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    class Node implements Comparable<Node> {
        int weight;
        Node left;
        Node right;

        BitSequence seq;
        char value;
        boolean isTerminal = false;

        Node(char value, int weight) {
            this.value = value;
            this.weight = weight;
            this.isTerminal = true;
        }

        Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.weight = left.weight + right.weight;
        }

        @Override
        public int compareTo(Node o) {
            return this.weight - o.weight;
        }
    }
    Node root;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> nodes = new MinPQ<>();
        for (char c: frequencyTable.keySet()) {
            Node leaf = new Node(c, frequencyTable.get(c));
            nodes.insert(leaf);
        }
        while (nodes.size() != 1) {
            Node left = nodes.delMin();
            Node right = nodes.delMin();
            Node parent = new Node(left, right);
            nodes.insert(parent);
        }
        this.root = nodes.delMin();
        this.root.seq = new BitSequence();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        int i = 0;
        Node node = root;
        for (; !node.isTerminal; i++) {
            int direct = querySequence.bitAt(i);
            switch (direct) {
                case 0:
                    node = node.left;
                    break;
                case 1:
                    node = node.right;
                    break;
                default:
                    break;
            }
        }
        return new Match(querySequence.firstNBits(i), node.value);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> table = new HashMap<>();
        Stack<Node> toVisit = new Stack<>();
        toVisit.push(root);
        while (!toVisit.isEmpty()) {
            Node node = toVisit.pop();
            if (node.isTerminal) {
                table.put(node.value, node.seq);
            }
            if (node.left != null) {
                node.left.seq = node.seq.appended(0);
                toVisit.push(node.left);
            }
            if (node.right != null) {
                node.right.seq = node.seq.appended(1);
                toVisit.push(node.right);
            }
        }
        return table;
    }
}
