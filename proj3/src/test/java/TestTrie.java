import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestTrie {
    @Test
    public void testUppercaseInsert() {
        Trie trie = new Trie();
        trie.insert("an");
        trie.insert("bad aPple");
        List<String> expected = new ArrayList<>();
        expected.add("bad aPple");
        assertEquals("result not matched", expected, trie.findPrefix("bad a"));
    }

    @Test
    public void testPrefixInsert() {
        Trie trie = new Trie();
        trie.insert("an");
        trie.insert("andy");
        trie.insert("ant");
        trie.insert("and");
        List<String> expected = new ArrayList<>();
        expected.add("and");
        expected.add("andy");
        expected.add("ant");
        assertEquals("result not matched", expected, trie.findPrefix("an"));
    }

    @Test
    public void testMutipleInsert() {
        Trie trie = new Trie();
        trie.insert("and");
        trie.insert("(and)");
        List<String> expected = new ArrayList<>();
        expected.add("and");
        expected.add("(and)");
        assertEquals("result not matched", expected, trie.findPrefix("an"));
    }

    @Test
    public void testEmptyInsert() {
        Trie trie = new Trie();
        trie.insert("123");
        trie.insert("456");
        trie.insert("789");
        List<String> expected = new LinkedList<>();
        expected.add("123");
        expected.add("456");
        expected.add("789");
        assertEquals("unmatched", expected, trie.findPrefix("123"));
    }
}
