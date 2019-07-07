
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nick
 */
public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final double[] results;
    private double mean = -1;
    private double stddev = -1;

    public PercolationStats(int n, int trials) {    // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        results = new double[trials];
        for (int iter = 0; iter < trials; iter++) {
            results[iter] = doTrial(n);
        }
    }

    private double doTrial(int n) {
        final Percolation trial = new Percolation(n);
        while (!trial.percolates()) {
            int row = StdRandom.uniform(n) + 1;
            int col = StdRandom.uniform(n) + 1;
            trial.open(row, col);
        }
        return trial.numberOfOpenSites() / (double) (n * n);
    }

    public double mean() {              // sample mean of percolation threshold
        if (mean == -1) {
            mean = StdStats.mean(results);
        }
        return mean;
    }

    public double stddev() {                     // sample standard deviation of percolation threshold
        if (stddev == -1) {
            stddev = StdStats.stddev(results);
        }
        return stddev;
    }

    public double confidenceLo() {        // low  endpoint of 95% confidence interval
        return mean() - CONFIDENCE_95 * (stddev() / Math.sqrt(results.length));
    }

    public double confidenceHi() {          // high endpoint of 95% confidence interval
        return mean() + CONFIDENCE_95 * (stddev() / Math.sqrt(results.length));
    }

    public static void main(String[] args) {   // test client (described below)
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        final PercolationStats stats = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
