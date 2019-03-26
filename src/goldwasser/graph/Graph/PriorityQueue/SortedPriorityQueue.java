package goldwasser.graph.Graph.PriorityQueue;

import goldwasser.graph.Graph.ListIterator.LinkedPositionalList;
import goldwasser.graph.Graph.ListIterator.Position;
import goldwasser.graph.Graph.ListIterator.PositionalList;

import java.util.Comparator;

public class SortedPriorityQueue<K, V> extends AbstractPriorityQueue<K, V> {
    // primary collection of priority queue entries
    private PositionalList<Entry<K, V>> list = new LinkedPositionalList<>();

    // creates an empty priority queue based on the natural ordering of its keys
    public SortedPriorityQueue() { super(); }

    // creates an empty prioriyt queue using the given comparator to order keys
    public SortedPriorityQueue(Comparator<K> comp) { super(comp); }

    // inserts a key-value pair and returns the entry created
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key); // auxiliary key-checking method (could throw exception)
        Entry<K, V> newest = new PQEntry<>(key, value);
        Position<Entry<K, V>> walk = list.last();
        // walk backward, looking for smaller key
        while (walk != null && compare(newest, walk.getElement()) < 0)
            walk = list.before(walk);
        if (walk == null)
            list.addFirst(newest);
        else
            list.addAfter(walk, newest); // newest goes after walk
        return newest;
    }

    // returns (but does nto remove) an entry with minimal key
    public Entry<K, V> min() {
        if (list.isEmpty()) return null;
        return list.first().getElement();
    }

    // remove and returns an entry with minimal k   ey
    public Entry<K, V> removeMin() {
        if (list.isEmpty()) return null;
        return list.remove(list.first());
    }

    // returns the number of items in the priority queue
    public int size() { return list.size(); }
}
                                                                                        