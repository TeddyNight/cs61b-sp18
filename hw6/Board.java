class Board {
    char[][] board;
    boolean[][] adjacent = new boolean[26][26];
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
                if (i >0 && j < M - 1) {
                    adjacent[key(board[i - 1][j + 1])][key(board[i][j])] = true;
                    adjacent[key(board[i][j])][key(board[i - 1][j + 1])] = true;
                }
            }
        }
    }

    private int key(char c) {
        if (c == 'é' || c == 'ê') {
            c = 'e';
        }
        return c - 'a';
    }

    boolean existsLetters(String str) {
        char[] words = str.toLowerCase().toCharArray();
        // at least three letters long
        if (words.length < 3) {
            return false;
        }
        for (int i = 1; i < words.length; i++) {
            // do not account for "qu" tile
            if (words[i] == '\'') {
                return false;
            }
            if (!adjacent[key(words[i - 1])][key(words[i])]) {
                return false;
            }
        }
        return true;
    }
}