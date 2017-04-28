/**
 * This program uses union-find to determine if a N x N grid percolates.
 *
 * @author Jimmy Nguyen
 * @version 4/22/2017
 */
    
public class Percolation {
    // Declare variables
    private int n;
    private int open;
    private boolean[][] grid;
    private QuickUnionUF quf;
    
    public Percolation(int n){
        if(n <= 0) throw new java.lang.IllegalArgumentException();
        
        // Initialize
        this.n = n;
        open = 0;
        grid = new boolean[n][n];
        quf = new QuickUnionUF(n*n+2);
    }
    
    // Opens a site and connects it to adjacent sites
    public void open(int row, int col){
        checkBounds(row, col);
        if(!isOpen(row, col)){
            int id = calculateID(row, col);
            if(isStartEdge(row)){ //If this is the first row
                quf.union(id, 0);
            } else {
                if(isOpen(row-1, col)) quf.union(id, calculateID(row-1, col));
            }
            if(isEndEdge(row)){ //If this is the last row
                quf.union(id, n*n+1);
            } else {
                if(isOpen(row+1, col)) quf.union(id, calculateID(row+1, col));
            }
            if(!isStartEdge(col)){ //If this is not the first column
                if(isOpen(row, col-1)) quf.union(id, calculateID(row, col-1));
            }
            if(!isEndEdge(col)){ //If this is not the last column
                if(isOpen(row, col+1)) quf.union(id, calculateID(row, col+1));
            }
            grid[row-1][col-1] = true;
            open++;
        }
    }
    
    // Returns of the site is opened
    public boolean isOpen(int row, int col){
        checkBounds(row, col);
        return grid[row-1][col-1];
    }
    
    // Full if the coordinate is connected to the top row
    public boolean isFull(int row, int col){
        checkBounds(row, col);
        return quf.connected(calculateID(row, col), 0);
    }
    
    // Returns the number of open sites
    public int numberOfOpenSites(){
        return open;
    }
    
    // Returns if the grid percolates
    public boolean percolates(){
        return quf.connected(0, n*n+1);
    }
    
    // Helper to check the boundaries
    private void checkBounds(int row, int col){
        if(row < 1 || row > n || col < 1 || col > n){
            throw new java.lang.IndexOutOfBoundsException();
        }
    }
    
    // Calculates the ID of the site
    private int calculateID(int row, int col){
        return (row-1)*n + col;
    }
    
    // Top or left edge
    private boolean isStartEdge(int i){
        return i == 1;
    }
    
    // bottom or right edge
    private boolean isEndEdge(int i){
        return i == n;
    }
    
    /**
     * Nested class that implements the union-find algorithm
     */
    private class QuickUnionUF {
        private int[] id;
        private int[] sz;
        // 
        public QuickUnionUF(int N){
            id = new int[N];
            sz = new int[N];
            for (int i = 0; i < N; i++){
                id[i] = i;
                sz[i] = 1;
            }
        }
        
        // Finds the root
        private int root(int i){
            while (i != id[i]){
                id[i] = id[id[i]];
                i = id[i];
            }
            return i;
        }
        
        // Returns if two objects are connected
        public boolean connected(int p, int q)
        {
            return root(p) == root(q);
        }
        
        // Connects two objects
        public void union(int p, int q)
        {
             int i = root(p);
             int j = root(q);
             if (i == j) return;
             if (sz[i] < sz[j]){
                 id[i] = j;
                 sz[j] += sz[i];
             } else {
                 id[j] = i;
                 sz[i] += sz[j];
             } 
        }
    }

    // Quick Check
    public static void main(String[] args){
        Percolation p = new Percolation(3);
        p.open(1,1);
        System.out.println("Percolates? " + p.percolates());
        p.open(2,1);
        System.out.println("Percolates? " + p.percolates());
        p.open(3,1);
        System.out.println("Percolates? " + p.percolates());
        p.isOpen(4,1); // Should throw exception here
    }
}