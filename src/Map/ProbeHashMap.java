package Map;

import java.util.ArrayList;
import java.util.Map;

public class ProbeHashMap<K, V> extends AbstractHashMap<K, V> {
    private MapEntry<K, V>[] table; // a fixed array of entries (all initially null)
    private MapEntry<K, V> DEFUNCT = new MapEntry<>(null, null); // sentinel

    public ProbeHashMap() {
        super();
    }

    public ProbeHashMap(int cap) {
        super(cap);
    }

    public ProbeHashMap(int cap, int p) {
        super(cap, p);
    }

    // creates an empty table having length equal to current capacity
    protected void createTable() {
        table = (MapEntry<K, V>[]) new MapEntry[capacity]; // safe cast
    }

    // returns true if location is either empty or the "defunct" sentinel
    private boolean isAvailable(int j) {
        return (table[j] == null || table[j] == DEFUNCT);
    }

    // returns index with key k or -(a + 1) such that k could be added at index a
    private int findSlot(int h, K k) {
        int avail = -1; // no slot available (thus far)
        int j = h; // index while scanning tale
        do {
            if (isAvailable(j)) { // may be either empty or defunct
                if (avail == -1) avail = j; // this is the first available slot
                if (table[j] == null) break; // if empty, search fails immediately

            } else if (table[j].getKey().equals(k))
                return j; // successful match
            j = (j + 1) % capacity; // keep looking (cyclically)
        } while (j != h);
        return -(avail + 1);
    }

    // returns value associated with key k in bucket with hash value h, or else null
    protected V bucketGet(int h, K k) {
        int j = findSlot(h, h);
        if (j < 0 ) return null; // no match found
        return table[j].getValue();
    }

    // associates key k with value v in bucket with hash value h; returns old value
    protected V bucketPut(int h, K k, V v) {
        int j = findSlot(h, k);
        if (j >= 0)
            return table[j].setValue(v); // this key has an existing entry
        table[-(j + 1)] = new MapEntry<>(k, v);
        n++;
        return null;
    }

    // removes entry having key k from bucket with has value h (if any)
    protected V bucketRemove(int h, K k) {
        int j = findSlot(h, k);
        if (j < 0) return null; // nothing to remove
        V answer = table[j].getValue();
        table[j] = DEFUNCT; // mark this slot as deactivated
        n --;
        return answer;
    }

    // returns an iterable collection of all key-value entries of the map
    public Iterable<Map.Entry<K, V>> entrySet() {
        ArrayList<Map.Entry<K, V>> buffer = new ArrayList<>();
        for (int h = 0; h < capacity; h++)
            if (!isAvailable(h)) buffer.add(table[h]);
            return buffer;
    }
}
