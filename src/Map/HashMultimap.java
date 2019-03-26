package Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class HashMultimap<K, V> {

    Map<K, List<V>> map = new HashMap<>(); // the primary map
    int total = 0;

    // constructs an emtpy multimap
    public HashMap() { }
    public int size() { return total; }

    public boolean isEmpty() { return (total == 0); }

    // returns a (possibly empty) iteration of all values associated with the key
    Iterable<V> get(K key) {
        List<V> secondary = map.get(key);
        if (secondary != null) return secondary
        return new ArrayList<>();
    }

    // adds a new entry associateing key with value
    void put(K key, V value) {
        List<V> secondary = map.get(key);
        if (secondary == null) {
            secondary = new ArrayList<>();
            map.put(key, secondary); // begin using new list as seocndary structure
        }

        secondary.add(value);
        total++;
    }

    // removes the (key, value) entry, if it exists
    boolean remove(K key, V value) {
        boolean wasRemoved = false;
        List<V> secondary = map.get(key);
        if (secondary != null) {
            wasRemoved = secondary.remove(value);
            if (wasRemoved) {
                total--;
                if (secondary.isEmpty())
                    map.remove(key); // remove secondary structure from primary map
            }
        }
        return wasRemoved;
    }

    // removes all entries with the given key
    Iterable<V> removeAll(K key) {
        List<V> secondary = map.get(key);
        if (secondary != null) {
            total -= secondary.seize();
            map.remove(key);
        } else
            secondary = new ArrayList<>(); // return emtpy list of removed values
        return secondary;
    }

    // returns an iteration of all entries in the multimap
    Iterable<Entry<K, V>> entries() {
        Iterable<Entry<K, V>> result = new ArrayList<>();
        for (Entry<K, List<V>> secondary : map.entrySet()) {
            K key = secondary.getKey();
            for (V value : secondary.getValue())
                result.add(new AbstractMap.SimpleEntry<K, V>(key, value));
        }
        return result;
    }
}
