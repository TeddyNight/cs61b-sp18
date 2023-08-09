import edu.princeton.cs.introcs.In;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;

public class Board {
    private List<List<Character>> board;
    private Map<Character, Set<Character>> adjacent = new HashMap<>();
    private int N;
    private int M;
    Board(String boardFilePath) {
        board = readBoard(boardFilePath);
        N = board.size();
        M = board.get(board.size() - 1).size();
        buildAdjacent();
    }

    private List<List<Character>> readBoard(String boardFilePath) {
        In boardFile = new In(boardFilePath);
        List<List<Character>> lines = new LinkedList<>();
        while (boardFile.hasNextLine()) {
            List<Character> line = new LinkedList<>();
            for (char c: boardFile.readLine().toCharArray()) {
                line.add(c);
            }
            if (!lines.isEmpty()
                    && lines.get(lines.size() - 1).size() != line.size()) {
                throw new IllegalArgumentException();
            }
            lines.add(line);
        }
        return lines;
    }

    private void buildAdjacent() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                char c = board.get(i).get(j);
                Set<Character> node = adjacent.get(c);
                Set<Character> neighbors = findNeighbors(i, j);
                if (node == null) {
                    adjacent.put(c, neighbors);
                } else {
                    node.addAll(neighbors);
                }
            }
        }
    }

    private boolean isValidPos(int i, int j) {
        return i >= 0 && i < N && j >= 0 && j < M;
    }

    private Set<Character> findNeighbors(int i, int j) {
        Set<Character> neighbors = new HashSet<>();
        int[][] directions = {{i - 1, j}, {i + 1, j},
            {i - 1, j - 1}, {i - 1, j + 1},
            {i + 1, j - 1}, {i + 1, j + 1},
            {i, j - 1}, {i, j + 1}};
        for (int[] direction: directions) {
            int x = direction[0];
            int y = direction[1];
            if (!isValidPos(x, y)) {
                continue;
            }
            neighbors.add(board.get(x).get(y));
        }
        return neighbors;
    }

    public Set<Character> getAdjacent(char c) {
        return adjacent.get(c);
    }
}
