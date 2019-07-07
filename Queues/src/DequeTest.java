import junit.framework.TestCase;

import static org.junit.Assert.*;

public class DequeTest {

    @org.junit.Test
    public void isEmpty() {
        Deque<Integer> ints = new Deque<>();
        ints.addFirst(1);
        assertEquals(false, ints.isEmpty());
        ints.removeFirst();
        assertEquals(true, ints.isEmpty());
    }

    @org.junit.Test
    public void size() {
        Deque<Integer> ints = new Deque<>();
        ints.addFirst(1);
        assertEquals(1, ints.size());
        ints.addLast(2);
        assertEquals(2, ints.size());
        ints.removeFirst();
        assertEquals(1, ints.size());
    }

    @org.junit.Test
    public void addFirst() {
        Deque<Integer> ints = new Deque<>();
        ints.addFirst(1);
        assertEquals(1, ints.size());
    }

    @org.junit.Test
    public void addLast() {
        Deque<Integer> ints = new Deque<>();
        ints.addLast(1);
        assertEquals(1, ints.size());
    }

    @org.junit.Test
    public void removeFirst() {
        Deque<Integer> ints = new Deque<>();
        ints.addFirst(1);
        assertEquals(1, ints.size());
        ints.removeFirst();
        assertEquals(0, ints.size());
    }

    @org.junit.Test
    public void removeLast() {
        Deque<Integer> ints = new Deque<>();
        ints.addLast(1);
        assertEquals(1, ints.size());
        ints.removeLast();
        assertEquals(0, ints.size());
    }

    @org.junit.Test
    public void iterator() {
        Deque<Integer> ints = new Deque<>();
        ints.addFirst(2);
        ints.addFirst(1);
        ints.addLast(3);
        ints.addLast(4);
        int num = 1;
        for (int i : ints) {
            assertEquals(num++, i);
        }
    }
}