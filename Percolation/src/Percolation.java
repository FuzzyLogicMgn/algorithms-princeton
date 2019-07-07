
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 *
 * @author nick
 */
public class Percolation {

    private final WeightedQuickUnionUF algo;
    private final WeightedQuickUnionUF algoAfterPercolation;
    private final int top;
    private final int bottom;
    private final int n;
    private final boolean[] sites;
    private int numberOfOpenSites = 0;
    private boolean percolates = false;

    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        final int total = n * n;
        algo = new WeightedQuickUnionUF(total + 2);
        algoAfterPercolation = new WeightedQuickUnionUF(total + 1);
        sites = new boolean[total];
        top = total;
        bottom = total + 1;

        final int lastIndex = total - 1;
        for (int i = 0; i < n; i++) {
            algoAfterPercolation.union(top, i);
            algo.union(top, i);
            algo.union(bottom, lastIndex - i);
        }
    }

    private void checkIndex(int index) {
        if (!doCheckIndex(index)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean doCheckIndex(int index) {
        return !(index < 1 || n < index);
    }

    private int toIndex(int row, int col) {
        return n * (row - 1) + (col - 1);
    }

    private void tryOpen(int row, int col, int rowDiff, int colDiff) {
        int newRow = row + rowDiff;
        int newCol = col + colDiff;
        if (doCheckIndex(newRow) && doCheckIndex(newCol)) {
            if (isOpen(newRow, newCol)) {
                final int thisIndex = toIndex(row, col);
                final int otherIndex = toIndex(newRow, newCol);
                algo.union(thisIndex, otherIndex);
                algoAfterPercolation.union(thisIndex, otherIndex);
                percolates();
            }
        }
    }

    public void open(int row, int col) { // open site (row, col) if it is not open already
        checkIndex(row);
        checkIndex(col);
        if (!isOpen(row, col)) {
            sites[toIndex(row, col)] = true;
            numberOfOpenSites++;
            tryOpen(row, col, 1, 0);
            tryOpen(row, col, -1, 0);
            tryOpen(row, col, 0, 1);
            tryOpen(row, col, 0, -1);
        }
    }

    public boolean isOpen(int row, int col) { // is site (row, col) open?
        checkIndex(row);
        checkIndex(col);
        return sites[toIndex(row, col)];
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        checkIndex(row);
        checkIndex(col);
        if (isOpen(row, col)) {
            if (!percolates) {
                return algo.connected(toIndex(row, col), top);
            } else {
                return algoAfterPercolation.connected(toIndex(row, col), top);
            }
        }
        return false;
    }

    public int numberOfOpenSites() {      // number of open sites
        return numberOfOpenSites;
    }

    public boolean percolates() {             // does the system percolate?
        if (percolates) {
            return true;
        }
        percolates = n == 1 ? isOpen(1, 1) : algo.connected(top, bottom);        
        return percolates;
    }

    public static void main(String[] args) { // test client (optional)
    }
}
