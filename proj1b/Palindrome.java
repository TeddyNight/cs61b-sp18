public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<Character>();
        if(word.isBlank()) {
            deque.addLast('\0');
        }
        char[] chars = word.toCharArray();
        for (Character c:chars) {
            deque.addLast(c);
        }
        return deque;
    }
    public boolean isPalindrome(String word) {
        CharacterComparator cc = new OffByN(0);
        return isPalindrome(word,cc);
    }
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> chars = wordToDeque(word);
        while (!chars.isEmpty()) {
            Character first = chars.removeFirst();
            if (chars.isEmpty()) {
                return true;
            }
            Character last = chars.removeLast();
            if (!cc.equalChars(first,last)) {
                return false;
            }
        }
        return true;
    }
}
