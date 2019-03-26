package goldwasser.PriorityQueue;

import goldwasser.ListIterator.Position;
import goldwasser.ListIterator.LinkedPositionalList;
import goldwasser.ListIterator.PositionalList;

import java.util.Comparator;

public class UnsortedPriorityQueue<K, V> extends AbstractPriorityQueue<K, V> {
    // primary collection of priority queue entries
    private PositionalList<Entry<K, V>> list = new LinkedPositionalList<>();

    // cretes an empty priority queue based on the natural ordering of its keys
    public UnsortedPriorityQueue() { super(); }

    // creates an empty priority queue using the given comparator to order keys
    public UnsortedPriorityQueue(Comparator<K> comp) { super(comp); }

    // returns the Position of an entry having minimal key
    private Position<Entry<K, V>> findMain() {
        Position<Entry<K, V>> small = list.first();
        for (Position<Entry<K, V>> walk : list.positions())
            if (compare(walk.getElement(), small.getElement()) < 0)
                small = walk; // found an even smaller key
        return small;
    }

    // inserts a key-value pair and returns the entry created
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key); // auxiliary key-checking method (could throw exception)
        Entry<K, V> newest = new PQEntry<>(key, value);
        list.addLast(newest);
        return newest;
    }

    // returns (but does not remove) an entry with minimal key
    public Entry<K, V> min() {
        if (list.isEmpty()) return null;
        return findMain().getElement();
    }

    // removes and returns an entry with minimal key
    public Entry<K, V> removeMin() {
        if (list.isEmpty()) return null;
        return list.remove(findMin());
    }

    // returns the number of items in the priority queue
    public int size() { return list.size(); }
}
