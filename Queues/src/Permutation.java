import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;


public class Permutation {

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }
        final int k = Integer.parseInt(args[0]);
        final RandomizedQueue<String> queue = new RandomizedQueue<>();
        String line;
        int index = 0;
        while (!StdIn.isEmpty()) {
            line = StdIn.readString();
            if (StdRandom.uniform(++index) < k) {
                if (queue.size() == k) {
                    queue.dequeue();
                }
                queue.enqueue(line);
            }
        }
        while (!queue.isEmpty()) {
            StdOut.println(queue.dequeue());
        }
    }
}
