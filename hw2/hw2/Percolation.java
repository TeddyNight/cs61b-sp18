package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.List;

public class Percolation {
    private int N;
    private WeightedQuickUnionUF grids;
    private WeightedQuickUnionUF flow;
    private boolean[] isOpened;
    private int openSite;
    /**
     * create N-by-N grid, with all sites initially blocked
     * @param N
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        grids = new WeightedQuickUnionUF(N * N + 2);
        flow = new WeightedQuickUnionUF(N * N + 1);
        isOpened = new boolean[N * N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int site = xyTo1D(i, j);
                isOpened[site] = false;
            }
        }
        openSite = 0;
    }

    /**
     * return the corresponding #
     * @param r
     * @param c
     * @return
     */
    private int xyTo1D(int r, int c) {
        return r * N + c;
    }

    /**
     * open the site (row, col) if it is not open already
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        if (!isValidSite(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        int site = xyTo1D(row, col);
        // open already
        if (isOpen(row, col)) {
            return;
        }
        isOpened[site] = true;
        openSite++;
        if (row == 0) {
            // virtual top site
            flow.union(site, N * N);
            grids.union(site, N * N);
        }
        if (row == N - 1) {
            // virtual bottom site
            grids.union(site, N * N + 1);
        }
        for (int neighbor: neighborSite(row, col)) {
            grids.union(site, neighbor);
            flow.union(site, neighbor);
        }
    }

    /**
     * return all opened neighbors
     * @param row
     * @param col
     * @return
     */
    private List<Integer> neighborSite(int row, int col) {
        final int[][] neighbors = {{row - 1, col}, {row + 1, col}, {row, col + 1}, {row, col - 1}};
        List<Integer> ret = new ArrayList<>();
        for (int[] neighbor: neighbors) {
            if (isValidSite(neighbor[0], neighbor[1])
                    && isOpen(neighbor[0], neighbor[1])) {
                ret.add(xyTo1D(neighbor[0], neighbor[1]));
            }
        }
        return ret;
    }

    /**
     * is the site (row, col) open?
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col) {
        if (!isValidSite(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        int site = xyTo1D(row, col);
        return isOpened[site];
    }

    /**
     * is the site (row, col) full?
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col) {
        if (!isValidSite(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        int site = xyTo1D(row, col);
        return flow.connected(site, N * N);
    }

    /**
     * number of open sites
     * @return
     */
    public int numberOfOpenSites() {
        return openSite;
    }

    /**
     * does the system percolate?
     * @return
     */
    public boolean percolates() {
        return grids.connected(N * N, N * N + 1);
    }

    public static void main(String[] args) {

    }

    private boolean isValidSite(int row, int col) {
        if (row < 0 || row >= N) {
            return false;
        }
        if (col < 0 || col >= N) {
            return false;
        }
        return true;
    }
}
