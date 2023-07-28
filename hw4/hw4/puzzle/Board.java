package hw4.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements WorldState {
    private int N;
    private int[][] tiles;
    private int blankX;
    private int blankY;
    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j
     * @param tiles
     */
    Board(int[][] tiles) {
        this.N = tiles.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                }
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    private boolean isValidPos(int i, int j) {
        return (i >= 0 && i < N && j >= 0 && j < N);
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     * @param i
     * @param j
     * @return
     */
    public int tileAt(int i, int j) {
        if (!isValidPos(i, j)) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    /**
     * Returns the board size N
     * @return
     */
    public int size() {
        return N;
    }

    private void moveToBlank(int i, int j) {
        tiles[blankX][blankY] = tiles[i][j];
        tiles[i][j] = 0;
        blankX = i;
        blankY = j;
    }

    /**
     * Returns the neighbors of the current board
     * @return
     */
    public Iterable<WorldState> neighbors() {
        List<WorldState> res = new ArrayList<>();
        int[][] directions = {{blankX - 1, blankY}, {blankX + 1, blankY},
            {blankX, blankY + 1}, {blankX, blankY - 1}};
        for (int[] direction: directions) {
            int x = direction[0];
            int y = direction[1];
            if (!isValidPos(x, y)) {
                continue;
            }
            Board w = new Board(tiles);
            w.moveToBlank(x, y);
            res.add(w);
        }
        return res;
    }

    private int xyTo1D(int i, int j) {
        if (i == N - 1 && j == N - 1) {
            return 0;
        }
        return (i * N) + j + 1;
    }

    /**
     * Hamming estimate described below
     * @return
     */
    public int hamming() {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != xyTo1D(i, j)) {
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * sum of the vertical and horizontal distance from
     * the tile (i, j) to their goal positions
     * @param i
     * @param j
     * @return
     */
    private int manhattanDist(int i, int j) {
        int goal = tiles[i][j];
        int x = N - 1;
        int y = N - 1;
        if (goal != 0) {
            x = goal / N;
            y = (goal % N) - 1;
        }
        return Math.abs(i - x) + Math.abs(j - y);
    }

    /**
     * Manhattan estimate described below
     * @return
     */
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != xyTo1D(i, j)) {
                    sum += manhattanDist(i, j);
                }
            }
        }
        return sum;
    }

    /**
     * Estimated distance to goal. This method should
     * simply return the results of manhattan() when submitted to
     * Gradescope.
     * @return
     */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Returns true if this board's tile values are the same
     * position as y's
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board board = (Board) o;
        return Arrays.equals(tiles, board.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tiles);
    }

    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
