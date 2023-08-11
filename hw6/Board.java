import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class Board {
    char[][] board;
    boolean[][] adjacent = new boolean[26][26];
    int[] total = new int[26];
    Map<Character, List<Position>> words = new HashMap<>();
    int N;
    int M;
    Board(String path) {
        In file = new In(path);
        String[] lines = file.readAllLines();
        N = lines.length;
        M = lines[0].length();
        board = new char[N][M];
        for (int i = 0; i < N; i++) {
            char[] line = lines[i].toCharArray();
            if (line.length != M) {
                throw new IllegalArgumentException();
            }
            board[i] = line;
            for (int j = 0; j < M; j++) {
                char c = board[i][j];
                total[key(c)]++;
                List<Position> poses = words.get(c);
                if (poses == null) {
                    poses = new LinkedList<>();
                    words.put(c, poses);
                }
                poses.add(new Position(i, j));
                if (i > 0) {
                    adjacent[key(board[i - 1][j])][key(board[i][j])] = true;
                    adjacent[key(board[i][j])][key(board[i - 1][j])] = true;
                }
                if (j > 0) {
                    adjacent[key(board[i][j - 1])][key(board[i][j])] = true;
                    adjacent[key(board[i][j])][key(board[i][j - 1])] = true;
                }
                if (i > 0 && j > 0) {
                    adjacent[key(board[i - 1][j - 1])][key(board[i][j])] = true;
                    adjacent[key(board[i][j])][key(board[i - 1][j - 1])] = true;
                }
                if (i > 0 && j < M - 1) {
                    adjacent[key(board[i - 1][j + 1])][key(board[i][j])] = true;
                    adjacent[key(board[i][j])][key(board[i - 1][j + 1])] = true;
                }
            }
        }
    }

    private int key(char c) {
        if (c == 'é' || c == 'ê' || c == 'è' || c == 'ë') {
            c = 'e';
        }
        return c - 'a';
    }

    boolean existsLetters(String str) {
        char[] letters = str.toLowerCase().toCharArray();
        // at least three letters long
        if (letters.length < 3) {
            return false;
        }
        int[] cnt = new int[26];
        cnt[key(letters[0])]++;
        for (int i = 1; i < letters.length; i++) {
            // do not account for "qu" tile
            if (letters[i] == '\'') {
                return false;
            }
            int key = key(letters[i]);
            cnt[key]++;
            if (cnt[key] > total[key]) {
                return false;
            }
            if (!adjacent[key(letters[i - 1])][key]) {
                return false;
            }
        }
        return true;
    }

    List<Position> getNeighbor(Position p) {
        List<Position> neighbors = new LinkedList<>();
        int x = p.x;
        int y = p.y;
        Position[] poses = {new Position(x - 1, y), new Position(x + 1, y),
            new Position(x, y - 1), new Position(x, y + 1),
            new Position(x - 1, y - 1), new Position(x + 1, y + 1),
            new Position(x + 1, y - 1), new Position(x - 1, y + 1)};
        for (Position pos: poses) {
            if (validPos(pos)) {
                neighbors.add(pos);
            }
        }
        return neighbors;
    }

    char getChar(Position p) {
        return board[p.x][p.y];
    }

    List<Position> getPos(char c) {
        return words.get(c);
    }

    boolean validPos(Position p) {
        return p.x >= 0 && p.x < N && p.y >= 0 && p.y < M;
    }
}
