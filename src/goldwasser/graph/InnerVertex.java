package goldwasser.graph;

public class InnerVertex<V> implements Vertex<V> {
    private V element;
    private Position<Vertex<V>> pos;
    private Map<Vertex<V>, Edge<E>> outgoing, incoming;

    // constructs a new InnerVertex instance storing the given element
    public InnerVertex(V elem, boolean graphIsDirected) {
        element = elem;
        outgoing = new ProbeHashMap<>();
        if (graphIsDirected)
            incoming = new ProbeHashMap<>();
        else
            incoming = outgoing; // if undirected alias outgoing map
    }

    // returns the element associated with the vertex
    public V getElement() { return element; }
    // stores the position of this vertex within the graph's vertex list
    public void setPosition(Position<Vertex<V>> p) { pos = p; }
    // returns the position of this vertex within the graph's vertex list
    public Position<Vertex<V>> getPosition() { return pos; }
    // returns reference to the underlying map of outgoing edges
    public Map<Vertex<V>, Edge<E>> getOutgoing() { return outgoing; }
    // returns reference to the underlying map of incoming edges
    public Map<Vertex<V>, Edge<E>> getIncoming() { return incoming; }

}


