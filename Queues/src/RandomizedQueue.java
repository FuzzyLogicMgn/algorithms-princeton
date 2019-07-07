import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size = 0;

    public RandomizedQueue() {
        this(8);
    }

    /**
     * construct an empty randomized queue
     */
    private RandomizedQueue(final int initialSize) {
        items = (Item[]) new Object[initialSize];
    }

    private RandomizedQueue(final RandomizedQueue<Item> q) {
        items = q.items;
        size = q.size;
        setLength(size);
    }

    /**
     * @return is the randomized queue empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the number of items on the randomized queue
     */
    public int size() {
        return size;
    }

    /**
     * add the item
     *
     * @param item
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (items.length <= size) {
            setLength(items.length * 2);
        }

        items[size++] = item;
    }

    /**
     * remove and return a random item
     *
     * @return
     */
    public Item dequeue() {
        return dequeue(true);
    }

    private Item dequeue(final boolean resize) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (resize && size * 4 <= items.length) {
            setLength(items.length / 2);
        }

        final int randomIndex = StdRandom.uniform(size);
        final Item res = items[randomIndex];
        size--;
        items[randomIndex] = items[size];
        items[size] = null;
        return res;
    }

    /**
     * @return a random item (but do not remove it)
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniform(size)];
    }

    /**
     * @return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private void setLength(final int newLength) {
        final Item[] newItems = (Item[]) new Object[newLength];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    private class RandomQueueIterator implements Iterator<Item> {

        private final RandomizedQueue<Item> view = new RandomizedQueue<>(RandomizedQueue.this);

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return !view.isEmpty();
        }

        @Override
        public Item next() {
            if (hasNext()) {
                return view.dequeue(false);
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
        System.err.println("That's all folks!");
    }
}