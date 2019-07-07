import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class RandomizedQueueTest {

    @Test
    public void isEmpty() {
        RandomizedQueue<Integer> ints = new RandomizedQueue<>();
        assertEquals(true, ints.isEmpty());
        ints.enqueue(1);
        assertEquals(false, ints.isEmpty());
        ints.dequeue();
        assertEquals(true, ints.isEmpty());
    }

    @Test
    public void size() {
        RandomizedQueue<Integer> ints = new RandomizedQueue<>();
        assertEquals(0, ints.size());
        ints.enqueue(1);
        ints.enqueue(1);
        assertEquals(2, ints.size());
        ints.enqueue(1);
        ints.enqueue(1);
        assertEquals(4, ints.size());
        ints.dequeue();
        assertEquals(3, ints.size());
    }

    @Test
    public void enqueue() {

    }

    @Test
    public void dequeue() {
        List<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(2);
        Iterator<Integer> iter = arr.iterator();

        while (iter.hasNext()) {
            System.err.println(iter.next());
        }

    }

    @Test
    public void sample() {
        RandomizedQueue<Integer> ints = new RandomizedQueue<>();
        assertEquals(0, ints.size());
        ints.enqueue(1);
        ints.enqueue(1);
        assertEquals(2, ints.size());
        ints.enqueue(1);
        ints.enqueue(1);
        assertEquals(4, ints.size());
        ints.sample();
        assertEquals(4, ints.size());
    }

    @Test
    public void iterator() {
        RandomizedQueue<Integer> ints = new RandomizedQueue<>();
        assertEquals(0, ints.size());
        ints.enqueue(1);
        ints.enqueue(2);
        assertEquals(2, ints.size());
        ints.enqueue(3);
        ints.enqueue(4);
        assertEquals(4, ints.size());
        for (int i : ints) {
            System.err.print(i + " ");
        }
    }
}