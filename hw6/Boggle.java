import edu.princeton.cs.introcs.In;

import java.util.List;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        if (k <= 0) {
            throw new IllegalArgumentException();
        }

        Board board = new Board(boardFilePath);
        Trie dict = readDict(board);
        return dict.topKWords(k);
    }

    private static Trie readDict(Board board) {
        Trie dict = new Trie(board);
        In dictFile = new In(dictPath);
        while (dictFile.hasNextLine()) {
            String str = dictFile.readLine();
            // Pruning: don't construct those who can't be constructed
            if (board.existsLetters(str)) {
                dict.insert(str);
            }
        }
        return dict;
    }

    public static void main(String[] args) {
        Board board = new Board("exampleBoard.txt");
        Trie dict = readDict(board);
        for (String word: dict.topKWords(7)) {
            System.out.println(word);
        }
    }

}
