import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final int[][] blocks;
    private final int n;
    private int hamming = -1;
    private int manhattan = -1;

    /**
     * construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j)
     */
    public Board(int[][] blocks) {
        if (blocks == null || blocks.length < 2 || 128 <= blocks.length) {
            throw new IllegalArgumentException();
        }
        n = blocks.length;
        this.blocks = doCopy(blocks);
    }

    /**
     * @return board dimension n
     */
    public int dimension() {
        return blocks[0].length;
    }

    /**
     * @return number of blocks out of place
     */
    public int hamming() {
        if (hamming == -1) {
            hamming = 0;
            final int nSquare = n * n;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    final int expected = ((i * n + j) + 1) % nSquare;
                    if (expected != 0 && expected != blocks[i][j]) {
                        hamming++;
                    }
                }
            }
        }
        return hamming;
    }

    /**
     * @return sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        if (manhattan == -1) {
            manhattan = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    final int val = blocks[i][j];
                    final int row;
                    final int col;
                    if (val == 0) {
                        continue;
                    } else {
                        row = (val - 1) / n;
                        col = (val - 1) % n;
                    }
                    manhattan += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        return manhattan;
    }

    /**
     *
     * @return is this board the goal board?
     */
    public boolean isGoal() {
        if (hamming != -1) {
            return hamming == 0;
        }
        return manhattan() == 0;
    }

    /**
     *
     * @return a board that is obtained by exchanging any pair of blocks
     */
    public Board twin() {
        final int[][] twin = doCopy(blocks);

        final int row = n - 1;
        final int col = n - 1;
        if (twin[row][col] != 0 && twin[row][col - 1] != 0) {
            swap(twin, row, col, row, col - 1);
        } else {
            swap(twin, row - 1, col, row - 1, col - 1);
        }

        return new Board(twin);
    }

    private int[][] doCopy(final int[][] arr) {
        final int size = arr[0].length;
        final int[][] twin = new int[size][size];
        for (int i = 0; i < size; i++) {
            twin[i] = Arrays.copyOf(arr[i], size);
        }
        return twin;
    }

    private void swap(int[][] arr, int i, int j, int m, int n) {
        final int val = arr[i][j];
        arr[i][j] = arr[m][n];
        arr[m][n] = val;
    }

    /**
     *
     * @param y other Board
     * @return does this board equal y?
     */
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this == y) {
            return true;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        final Board other = (Board) y;
        if (n != other.n) {
            return false;
        }
        return Arrays.deepEquals(blocks, other.blocks);
    }

    /**
     *
     * @return all neighboring boards
     */
    public Iterable<Board> neighbors() {
        final ArrayList<Board> boards = new ArrayList<>(4);
        final int[] zeroPos = getZeroPos();
        final Board right = createNeighbour(zeroPos, zeroPos[0] + 1, zeroPos[1]);
        if (right != null) {
            boards.add(right);
        }
        final Board left = createNeighbour(zeroPos, zeroPos[0] - 1, zeroPos[1]);
        if (left != null) {
            boards.add(left);
        }
        final Board up = createNeighbour(zeroPos, zeroPos[0], zeroPos[1] + 1);
        if (up != null) {
            boards.add(up);
        }
        final Board down = createNeighbour(zeroPos, zeroPos[0], zeroPos[1] - 1);
        if (down != null) {
            boards.add(down);
        }
        return boards;
    }

    private int[] getZeroPos() {
        final int[] zeroPos = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    zeroPos[0] = i;
                    zeroPos[1] = j;
                    return zeroPos;
                }
            }
        }
        return zeroPos;
    }

    private Board createNeighbour(final int[] zeroPos, final int x, final int y) {
        if (x < 0 || n <= x) {
            return null;
        }
        if (y < 0 || n <= y) {
            return null;
        }
        final int[][] copyBlocks = doCopy(blocks);
        swap(copyBlocks, zeroPos[0], zeroPos[1], x, y);
        return new Board(copyBlocks);
    }

    /**
     *
     * @return string representation of this board (in the output format specified below)
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(n).append('\n');
        for (int i = 0; i < n; i++) {
            sb.append(' ');
            boolean first = true;
            for (int j = 0; j < n; j++) {
                if (first) {
                    first = false;
                } else {
                    sb.append("  ");
                }
                sb.append(blocks[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.err.println("Some code");
    }
}