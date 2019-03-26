package goldwasser.graph.Graph.PriorityQueue;

public interface Entry<K, V> {
    K getkey(); // returns the key stored in this entry
    V getValue();  // returns the value stored in this entry
}
