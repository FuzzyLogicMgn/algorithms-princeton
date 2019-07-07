import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head, tail;
    private int size = 0;

    /**
     * construct an empty deque
     */
    public Deque() {
    }

    /**
     * @return is the deque empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return return the number of items on the deque
     */
    public int size() {
        return size;
    }

    /**
     * add the item to the front
     *
     * @param item
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        final Node newNode = new Node(item);
        final Node prevHead = head;
        head = newNode;
        if (prevHead != null) {
            head.setNext(prevHead);
        } else {
            tail = head;
        }
        size++;
    }

    /**
     * add the item to the end
     *
     * @param item
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        final Node newNode = new Node(item);
        final Node prevTail = tail;
        tail = newNode;
        if (prevTail != null) {
            prevTail.setNext(tail);
        } else {
            head = tail;
        }
        size++;
    }

    /**
     * remove and return the item from the front
     *
     * @return
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        final Item resItem = head.val;
        head = head.next;
        if (size == 1) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return resItem;
    }

    /**
     * remove and return the item from the end
     *
     * @return
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        final Item resItem = tail.val;
        tail = tail.prev;
        if (size == 1) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        return resItem;
    }

    /**
     * return an iterator over items in order from front to end
     *
     * @return
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private final class Node {
        private Node prev, next;
        private final Item val;

        public Node(Item val) {
            this.val = val;
        }

        public void setNext(Node n) {
            next = n;
            n.prev = this;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node curNode = head;

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return curNode != null;
        }

        @Override
        public Item next() {
            if (hasNext()) {
                final Item val = curNode.val;
                curNode = curNode.next;
                return val;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * unit testing (optional)
     *
     * @param args
     */
    public static void main(String[] args) {
        final Deque<Integer> ints = new Deque<>();
        for (int i : ints) {
            System.err.print(i + " ");
        }
        System.err.println();
        ints.addFirst(1);
        for (int i : ints) {
            System.err.print(i + " ");
        }
        System.err.println();
        ints.addFirst(0);
        for (int i : ints) {
            System.err.print(i + " ");
        }
        System.err.println();
        ints.addLast(2);
        for (int i : ints) {
            System.err.print(i + " ");
        }
        System.err.println();
        ints.removeFirst();
        for (int i : ints) {
            System.err.print(i + " ");
        }
        System.err.println();
        ints.removeLast();
        for (int i : ints) {
            System.err.print(i + " ");
        }
    }
}