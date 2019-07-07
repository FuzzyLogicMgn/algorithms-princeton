import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class Solver {

    private final Board initial;
    private SearchNode winnerNode;
    private List<Board> solution;

    /**
     * find a solution to the initial board (using the A* algorithm)
     *
     * @param initial
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.initial = initial;
    }

    /**
     * @return is the initial board solvable?
     */
    public boolean isSolvable() {
        if (winnerNode == null) {
            solve();
        }
        return solution.get(0) == initial;
    }

    /**
     * @return min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        if (winnerNode == null) {
            solve();
        }
        return isSolvable() ? winnerNode.moves : -1;
    }

    /**
     * @return sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (winnerNode == null) {
            solve();
        }
        return isSolvable() ? solution : null;
    }

    /**
     * solve a slider puzzle (given below)
     */

    private void solve() {
        final Board twin = initial.twin();

        final MinPQ<SearchNode> queue = new MinPQ<>();
        final MinPQ<SearchNode> queueTwin = new MinPQ<>();
        queue.insert(new SearchNode(null, initial));
        queueTwin.insert(new SearchNode(null, twin));

        while (true) {
            winnerNode = step(queue);
            if (winnerNode != null) {
                break;
            }

            winnerNode = step(queueTwin);
            if (winnerNode != null) {
                break;
            }
        }

        solution = new LinkedList<>();
        SearchNode curNode = winnerNode;
        while (curNode != null) {
            solution.add(0, curNode.board);
            curNode = curNode.prevNode;
        }
    }

    private SearchNode step(MinPQ<SearchNode> queue) {
        final SearchNode searchNode = queue.delMin();
        if (searchNode.board.isGoal()) {
            return searchNode;
        } else {
            for (Board ch : searchNode.board.neighbors()) {
                if (searchNode.prevNode == null || !ch.equals(searchNode.prevNode.board)) {
                    queue.insert(new SearchNode(searchNode, ch));
                }
            }
        }
        return null;
    }

    private static class SearchNode implements Comparable<SearchNode> {

        private final SearchNode prevNode;
        private final Board board;
        private final int moves;
        private int manhattan = -1;

        public SearchNode(SearchNode prevNode, Board board) {
            this.prevNode = prevNode;
            this.board = board;
            this.moves = prevNode != null ? prevNode.moves + 1 : 0;
        }

        @Override
        public int compareTo(SearchNode other) {
            return (getManhattan() + moves) - (other.getManhattan() + other.moves);
        }

        private int getManhattan() {
            if (manhattan == -1) {
                manhattan = board.manhattan();
            }
            return manhattan;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            final SearchNode other = (SearchNode) obj;
            if (moves != other.moves) {
                return false;
            }
            if (!board.equals(other.board)) {
                return false;
            }
            return true;
        }
    }


    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}