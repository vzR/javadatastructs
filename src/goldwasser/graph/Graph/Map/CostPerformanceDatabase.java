package goldwasser.graph.Graph.Map;

import java.util.Map;
import java.util.SortedMap;

public class CostPerformanceDatabase {
    SortedMap<Integer, Integer> map = (SortedMap<Integer, Integer>) new SortedtableMap<>();

    // constructs an initially empty database
    public CostPerformanceDatabase() {}

    // returns the (cost, performance) entry with largest cost not
    // exceeding c (or null if no entry exist with cost c or less)
    public Map.Entry<Integer, Integer> best(int cost) {
        return map.floorEntry(cost);
    }

    // add a new entry with given cost and performance p
    public void add(int c, int p) {
        // other is at least as cheap
        Map.Entry<Integer, Integer> other = map.floorEntry(c);
        if (other != null && other.getvalue() >= p)
            return;
        map.put(c, p); // else, add (c, p) to database
        // and now remove any entries that are dominated by the new one
        other = map.higherEntry(c); // other is more expensive than c
        while (other != null && other.getValue() <= p) {
            map.remove(other.getKey());
            other = map.higherEntry(c);
        }
    }
}
