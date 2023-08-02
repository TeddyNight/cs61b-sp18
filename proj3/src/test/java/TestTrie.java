import org.junit.Test;

import java.util.ArrayList;
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
        assertEquals("result not matched", expected, trie.findPrefix("bada"));
    }

    @Test
    public void testPrefixInsert() {
        Trie trie = new Trie();
        trie.insert("an");
        trie.insert("andy");
        trie.insert("ant");
        trie.insert("and");
        List<String> expected = new ArrayList<>();
        expected.add("an");
        expected.add("and");
        expected.add("andy");
        expected.add("ant");
        assertEquals("result not matched", expected, trie.findPrefix("an"));
    }

    @Test
    public void testMutipleInsert() {
        Trie trie = new Trie();
        trie.insert("an");
        trie.insert("(an)");
        List<String> expected = new ArrayList<>();
        expected.add("(an)");
        expected.add("an");
        assertEquals("result not matched", expected, trie.findPrefix("an"));
    }
}
