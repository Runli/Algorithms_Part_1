package ProgrammingAssignments.Persolation;

import StdLib.StdOut;
import StdLib.StdRandom;
import StdLib.StdStats;

/**
 * Created by ilnurgazizov on 20.09.15.
 *
 * Full description of PA-1 Percolation in web site
 * Полное описание PA-1 Percolation на сайте
 *
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 * PercolationStats takes the grid size and the number of experiments and
 * determines what proportion of the cells must be open in order for the system to percolate.
 *
 */
public class PercolationStats {
    // Длина сетки N
    private int N;

    // The number of experiments to perform
    // Число экспериментов
    private int T;

    // The proportion of cells opened to percolate.
    // Доля клеток открытых percolate
    private double[] results;

    // Конструктор, принимающий размер клетки и число экспериментов
    public PercolationStats(int N, int T) {     // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0) throw new IllegalArgumentException("Input must be a positive integer");

        this.T = T;
        results = new double[T];

        for (int i = 0; i < this.T; i++) {
            results[i] = performT(N);
        }
    }

    // sample mean of percolation threshold(выборочное среднее из порога перколяции)
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold(образец стандартного отклонения порога проеткания)
    public double stddev() {
        if (T == 1) {
            return Double.NaN;
        } else {
            return StdStats.stddev(results);
        }
    }

    //Returns the lower, 95% confidence interval of open cell proportion, assumes trial number is over 30.
    public double confidenceLo() {            // low  endpoint of 95% confidence interval
        return (mean() - (1.96 * stddev() / Math.sqrt(T)));
    }

    //Returns the upper, 95% confidence interval of open cell proportion, assumes trial number is over 30.
    public double confidenceHi() {            // high endpoint of 95% confidence interval
        return (mean() + (1.96 * stddev() / Math.sqrt(T)));
    }

    // Выполняет эксперимент по открытию клеток до момента перколации, добавляя долю в results
    public double performT(int N){

        Percolation percolation = new Percolation(N);
        int sitesOpened = 0;

        while (!percolation.percolates()){
            while(true){
                int i = StdRandom.uniform(1, N + 1);
                int j = StdRandom.uniform(1, N + 1);

                if (!percolation.isOpen(i, j)){
                    percolation.open(i, j);
                    sitesOpened++;

                    break;
                }
            }
        }
        double result = (double) sitesOpened / (double)(N * N);

        return result;
    }

    public static void main(String[] args) {    // test client (described below)
        int T = Integer.parseInt(args[0]);
        int gridSize = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(gridSize, T);

        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }

}
