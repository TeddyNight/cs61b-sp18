package hw4.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements WorldState {
    private int N;
    private int[][] tiles;
    private int blankX;
    private int blankY;
    private int manhattan = 0;
    private int hamming;
    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j
     * @param tiles
     */
    public Board(int[][] tiles) {
        this.N = tiles.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tileAt(i, j) == 0) {
                    blankX = i;
                    blankY = j;
                    continue;
                } else if (tileAt(i, j) != goalTile(i, j)) {
                    hamming++;
                }
                manhattan += manhattanDist(i, j);
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

    private void moveBlankTo(int i, int j) {
        tiles[blankX][blankY] = tileAt(i, j);
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
        int x = blankX;
        int y = blankY;
        for (int[] direction: directions) {
            if (!isValidPos(direction[0], direction[1])) {
                continue;
            }
            moveBlankTo(direction[0], direction[1]);
            Board w = new Board(tiles);
            moveBlankTo(x, y);
            res.add(w);
        }
        return res;
    }

    private int goalTile(int i, int j) {
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
        return hamming;
    }

    /**
     * sum of the vertical and horizontal distance from
     * the tile (i, j) to their goal positions
     * @param i
     * @param j
     * @return
     */
    private int manhattanDist(int i, int j) {
        int tile = tileAt(i, j);
        if (tile == 0) {
            return 0;
        }
        int goal = tile - 1;
        int goalX = goal / N;
        int goalY = goal % N;
        return Math.abs(i - goalX) + Math.abs(j - goalY);
    }

    /**
     * Manhattan estimate described below
     * @return
     */
    public int manhattan() {
        return manhattan;
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
        /**
         * Arrays.equals() does not work as expected for multidimensional arrays
         * it only compares items of the 1st dimension for reference equality
         * @source https://stackoverflow.com/questions/8777257/equals-vs-arrays-equals-in-java
         */
        if (board.size() != size()) {
            return false;
        }
        if (board.blankX != blankX && board.blankY != blankY) {
            return false;
        }
        if (board.manhattan != manhattan) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) != board.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
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
