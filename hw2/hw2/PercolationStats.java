package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private int N;
    private int T;
    private double[] results;
    private double mean = Double.NaN;
    private double stddev = Double.NaN;

    /**
     * perform T independent experiments on an N-by-N grid
     * @param N
     * @param T
     * @param pf
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        this.T = T;
        results = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation exp = pf.make(N);
            results[i] = doExperiment(exp);
        }
    }

    private double doExperiment(Percolation exp) {
        while (!exp.percolates()) {
            int row = StdRandom.uniform(0, N);
            int col = StdRandom.uniform(0, N);
            exp.open(row, col);
        }
        return (double) exp.numberOfOpenSites() / N * N;
    }

    /**
     * sample mean of percolation threshold
     * @return
     */
    public double mean() {
        if (Double.isNaN(mean)) {
            mean = StdStats.mean(results);
        }
        return mean;
    }

    /**
     * sample standard deviation of percolation threshold
     * @return
     */
    public double stddev() {
        if (T == 1) {
            return Double.NaN;
        }
        if (Double.isNaN(stddev)) {
            stddev = StdStats.stddev(results);
        }
        return stddev;
    }

    /**
     * low endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLow() {
        return mean() - 1.96 * stddev / Math.sqrt(T);
    }

    /**
     * high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev / Math.sqrt(T);
    }
}
