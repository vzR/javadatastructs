package goldwasser.graph.Graph.PriorityQueue;

import java.util.Comparator;

public class HeapAdaptablePriorityQueue<K, V>
        extends HeapPriorityQueue<K, V> implements AdaptablePriorityQueu<K, V> {

    // nested AdaptablePQEntry class
    // extendsion of the PQEntry to include location information
    protected static class AdaptablePQEntry<K, V> extends PQEntry<K, V> {
        private int index; // entry's current index within the heap
        public AdaptablePQEntry(K key, V value, int j) {
            super(key, value); // sets the key and value
            index = j; // sets the new field
        }

        public int getIndex() { return index; }
        public void setIndex(int j) { index = j; }
    }

    // creates an empty adaptable priority queu using natural ordering of keys
    public HeapPriorityQueue() { super(); }

    // creates an emtpy adaptable priority queue using the given comparator
    public HeapAdaptablePriorityQueue(Comparator<K> comp) { super(comp); }

    // protected utilities
    // validates an entry to ensure it is location-aware
    protected AdaptablePQEntry<K, V> validate(Entry<K, V> entry) throws IllegalArgumentException {
        if (!(entry instanceof  AdaptablePQEntry))
            throw new IllegalArgumentException("invalid entry");

        AdaptablePQEntry<K, V> locator = (AdaptablePQEntry<K, V>) entry; // safe
        int j = locator.getIndex();
        if (j >= heap.size() || heap.get(j) != locator)
            throw new IllegalArgumentException("invalid entry");
        return locator;
    }

    // exchaneges the enties at indices i and j of the array list
    protected void swap(int i, int j) {
        super.swap(i, j);
        ((AdaptablePQEntry<K, V>) heap.get(i)).setIndex(i); // rest entry's index
        ((AdaptablePQEntry<K, V>) heap.get(j)).setIndex(j); // rest entry's index
    }

    // restores the heap property by moving the entry at index j upward / downward
    protected void bubble(int j) {
        if (j > 0 && compare(heap.get(j), heap.get(parent(j))) < 0)
            upheap(j);
        else
            downheap(j); // although it might not need to move
    }

    // inerts a key-value pair and returns the entry ycreated
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key); // might throw exception
        Entry<K, V> newest = new AdaptablePQEntry<K, V>(key, value, heap.size());
        heap.add(newest); // add to the end of the list
        upheap(heap.size() - 1); // upheap newly added entry
        return newest;
    }

    // removes the given entry from the priority queue
    public void remove(Entry<K, V> entry) throws IllegalArgumentException {
        AdaptablePQEntry<K, V> locator = validate(entry);
        int j = locator.getIndex();
        if (j == heap.size() - 1) // entry is at last position
            heap.remove(heap.size() - 1); // so just remove it
        else {
            swap(j, heap.size() - 1); // swap  entry to last position
            heap.remove(heap.size() - 1); // then remove it
            bubble(j); // an fix entry displaced by the swap
        }
    }

    // replaces the key of an entry
    public void replaceKey(Entry<K, V> entry, K key) throws IllegalArgumentException {
        AdaptablePQEntry<K, V> locator = validate(entry);
        checkKey(key); // might throw exception
        locator.setKey(key); // method inherited from PQEntry
        bubble(locator.getIndex()); // with new key, may need to move entry
    }

    // replaces the value of an entry
    public void replaceValue(Entry<K, V> entry, V value) throws IllegalArgumentException {
        AdaptablePQEntry<K, V> locator = validate(entry);
        locator.setValue(value); // method inherited from PQEntry
    }


}
