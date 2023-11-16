/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufOnlyTop;
    private int n;
    private int countOpenSites;
    private boolean[] openState;
    private int top;
    private int bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        this.countOpenSites = 0;
        top = n * n;
        bottom = n * n + 1;
        openState = new boolean[n * n]; // 默认值为false
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufOnlyTop = new WeightedQuickUnionUF(n * n + 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException();
        if (isOpen(row, col)) return;
        int basic = rowCol2Index(row, col);
        this.openState[basic] = true;
        this.countOpenSites += 1;
        // 这个是两个虚拟结点
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(basic, basic - n);
            ufOnlyTop.union(basic, basic - n);
        }
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(basic, basic - 1);
            ufOnlyTop.union(basic, basic - 1);
        }
        if (col < n && isOpen(row, col + 1)) {
            uf.union(basic, basic + 1);
            ufOnlyTop.union(basic, basic + 1);
        }
        if (row < n && isOpen(row + 1, col)) {
            uf.union(basic, basic + n);
            ufOnlyTop.union(basic, basic + n);
        }
        if (basic < n) {
            uf.union(basic, top);
            ufOnlyTop.union(basic, top);
        }
        if (basic >= n * (n - 1)) uf.union(basic, bottom);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException();
        int basic = rowCol2Index(row, col);
        return openState[basic];
    }

    private boolean out_ouf_range(int n) {
        return n < 0 || n >= this.n * this.n;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException();
        int basic = rowCol2Index(row, col);
        return ufOnlyTop.find(basic) == ufOnlyTop.find(top) && isOpen(row, col);
    }


    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.countOpenSites;
    }


    // does the system percolate?
    public boolean percolates() {
        return uf.find(top) == uf.find(bottom);
    }

    private int rowCol2Index(int row, int col) {
        return this.n * (row - 1) + col - 1;
    }


    // test client (optional)
    public static void main(String[] args) {
        int n;
        n = StdIn.readInt();
        Percolation test = new Percolation(n);
        while (true) {
            int p, q;
            p = StdIn.readInt();
            q = StdIn.readInt();
            test.open(p, q);
            System.out.println(test.numberOfOpenSites());
        }
    }

}
