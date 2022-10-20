import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestOffByN {

    // You must use this CharacterComparator and not instantiate
    // new ones,  or the autograder might be upset.

    // Your tests go here.
    @Test
    public void testOffByOne() {
        CharacterComparator offByOne = new OffByN(1);
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('b', 'a'));
        assertTrue(offByOne.equalChars('&', '%'));
        assertTrue(offByOne.equalChars('A', 'B'));
        assertFalse(offByOne.equalChars('A', 'b'));
        assertTrue(offByOne.equalChars('1', '0'));
        assertFalse(offByOne.equalChars('\0', '\0'));
    }
    @Test
    public void testOffByZero() {
        CharacterComparator offByZero = new OffByN(0);
        assertTrue(offByZero.equalChars('a', 'a'));
        assertTrue(offByZero.equalChars('\0', '\0'));
        assertFalse(offByZero.equalChars('a', 'b'));
        assertTrue(offByZero.equalChars('&', '&'));
    }
    @Test
    public void testOffByFive() {
        OffByN offBy5 = new OffByN(5);
        offBy5.equalChars('a',  'f');  // true
        offBy5.equalChars('f',  'a');  // true
        offBy5.equalChars('f',  'h');  // false
    }
}
