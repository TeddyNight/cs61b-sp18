package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (p.key.compareTo(key) < 0) {
            return getHelper(key, p.left);
        } else if (p.key.compareTo(key) > 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }
        if (p.key.compareTo(key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (p.key.compareTo(key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            size++;
            p.value = value;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        Stack<Node> nodes = new Stack<>();
        nodes.push(root);
        while (!nodes.isEmpty()) {
            Node cur = nodes.pop();
            if (cur == null) {
                continue;
            }
            keys.add(cur.key);
            nodes.push(cur.left);
            nodes.push(cur.right);
        }
        return keys;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (size() == 0) {
            throw new IndexOutOfBoundsException();
        }
        if (key == null) {
            throw new IllegalArgumentException();
        }
        V ret = get(key);
        root = remove(key, root);
        return ret;
    }

    /**
     * @source https://algs4.cs.princeton.edu/32bst/BST.java.html
     * @param key
     * @param p
     * @return
     */
    private Node remove(K key, Node p) {
        Node ret = p;
        int cmp = p.key.compareTo(key);
        if (cmp < 0) {
            p.left = remove(key, p.left);
        } else if (cmp > 0) {
            p.right = remove(key, p.right);
        } else {
            ret = max(p.left);
            ret.right = p.right;
            ret.left = removeMax(p.left);
        }
        return ret;
    }

    private Node max(Node p) {
        if (p.right == null) {
            return p;
        } else {
            return max(p.right);
        }
    }

    private Node removeMax(Node p) {
        if (p.right == null) {
            return p.left;
        }
        p.right = removeMax(p.right);
        return p;
    }


    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        V cur = get(key);
        if (cur == null || !cur.equals(value)) {
            return null;
        }
        root = remove(key, root);
        return cur;
    }

    private class MapIterator implements Iterator<K> {
        private Stack<Node> nodes;
        MapIterator() {
            nodes = new Stack<>();
            if (size() == 0) {
                throw new NullPointerException();
            }
            nodes.push(root);
        }
        @Override
        public boolean hasNext() {
            return nodes.isEmpty();
        }

        @Override
        public K next() {
            Node cur = nodes.pop();
            if (cur.left != null) {
                nodes.push(cur.left);
            }
            if (cur.right != null) {
                nodes.push(cur.right);
            }
            return cur.key;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new MapIterator();
    }
}
