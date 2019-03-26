package goldwasser.graph;

import goldwasser.ListIterator.LinkedPositionalList;

public class AdjacencyMapGraph {
    private class InnerEdge<E> implements Edge<E> {
        private E element;
        private Position<Edge<E>> pos;
        private Vertex<V>[] endpoints;

        // constructs innerEdge instance from u to v, storing the given element
        public InnerEdge(Vertex<V> u, Vertex<V> v, E elem) {
            element = elem;
            endpoints = (Vertex<V>[]) new Vertex[]{u, v} // array of length 2
        }

        // returns the element associated with the edge
        public E getElement() { return element; }
        // returns reference to the endpoint array
        public Vertex<V>[] getEndpoints() { return endpoints; }
        // stores the position of this edge within the graph's vertex list
        public void setPosition(Position<Edge<E>> p) { pos = p; }
        // returns the position of this edge within the graph's vertex list
        public Position<Edge<E>> getPosition() { return pos; }
    } // end of InnerEdge class

    private class InnerVertex<V> implements Vertex<V> {
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

    } // end of InnerVertex class


    private boolean isDirected;
    private PositionalList<Vertex<V>> vertices = new LinkedPositionalList<>();
    private PositionalList<Vertex<V>> edges = new LinkedPositionalList<>();

    // constructs an empty graph (either directed or undirected)
    public AdjacencyMapGraph(boolean directed) {
        this.isDirected = directed;
    }

    // returns the number of vertices of the graph
    public int numVertices() { return vertices.size(); }

    // returns the vertices of the graph sa an iterable collection
    public Iterable<Vertex<V>> vertices() { return vertices; }
    // returns the number of edges of the graph
    public int numEdges() { return edges.size(); }

    // returns the edges of the graph as an iterable collection
    public Iterable<Edge<E>> edges() { return edges; }

    // returns the number of edges for which vertex v is the origin
    public int outDegree(Vertex<V> v) {
        InnerVertex<V> vert = validate(v);
        return vert.getOutgoing().size();
    }

    // returns an iterable collection of edges for which vertex v is the origin
    public Iterable<Edge<E>> outgoingEdges(Vertex<V> v) {
        InnerVertex<V> vert = validate(v);
        return vert.getOutgoing().values(); // edges are the values in the adjacency map

    }

    // returns the number of edges for which vertex v is the destination
    public int inDegree(Vertex<V> v) {
        InnerVertex<V> vert = validate(v);
        return vert.getIncoming().size();
    }

    // returns an iterable collection of edges for which vertex v is the destination
    public Iterable<Edge<E>> incomingEdges(Vertex<V> v) {
        InnerVertex<V> vert = validate(v);
        return vert.getIncoming().values(); // edges are the values in the adjacency map
    }

    public Edge<E> getEdge(Vertex<V> u, Vertex<V> v) {
        // returns the edge from u to v, or null if they are not adjacent
        InnerVertex<V> origin = validate(u);
        return origin.getOutgoing().get(v); // will be null if no edge from u to v
    }

    // returns the vertices of edge e as an array of length two
    public Vertex<V>[] endVertices(Edge<E> e) {
        InnerEdge<E> edge = validate(e);
        return edge.getEndpoints();
    }

    // returns the vertex that is opposite vertex v on edge e
    public Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException {
        InnerEdge<E> edge = validate(e);
        Vertex<V>[] endpoints = edge.getEndpoints();
        if (endpoints[0] == v)
            return endpoints[1];
        else if (endpoints[1] == v)
            return endpoints[0];
        else
            throw new IllegalArgumentException("v is not incident to this edge");
    }

    // inserts and returns a new vertex with the given element
    public Vertex<V> insertVertex(V element) {
        InnerVertex<V> v = new InnerVertex<>(element, isDirected);
        v.setPosition(vertices.addLast(v));
        return v;
    }

    // inserts and returns a new edge between u and v, storing given element
    public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E element) throws IllegalArgumentException {
        if (getEdge(u, v) == null) {
            InnerEdge<E> e = new InnerEdge<>(u, v, element);
            e.setPosition(edges.addLast(e));
            InnerVertex<V> origin = validate(u);
            InnerVertex<V> dest = validate(v);
            origin.setOutgoing().put(v, e);
            dest.getIncoming().put(u, e);
            return e;
        }
        else {
            throw new IllegalArgumentException("edge from u to v exists");
        }
    }

    // removes a vertex and all its incident edges from the graph
    public void removeVertex(Vertex<V> v) {
        InnerVertex<V> vert = validate(v);
        // remove all incident edges from the graph
        for (Edge<E> e : vert.getOutgoing().values())
            removeEdge(e);
        for (Edge<E> e : vert.getIncoming().values())
            removeEdge(e);
        // remove this vertex from the list of vertices
        vertices.remove(vert.getPosition());
    }

    // performs depth-first search of graph g starting at Vertex u
    public static <V, E> void DFS(Graph<V, E> g,
                                  Vertex<V> u,
                                  Set<Vertex<V>> known,
                                  Map<Vertex<V>, Edge<E>> forest) {
        known.add(u); // u has been discovered
        for (Edge<E> e : g.outgoingEdges(u)) {
            Vertex<V> v = g.opposite(u, e);
            if (!known.contains(v)) {
                forest.put(v, e); // e is the tree edge that discovered v
                DFS(g, v, known, forest); // recursively explore from v

            }
        }
    }

    // returns an ordered list of edges comprising the direct path from u to v
    public static <V, E>  PositionalList<Edge<E>>
    constructPath(Graph<V, E> g, Vertex<V> u, Vertex<V> v,
                  Map<Vertex<V>, Edge<E>> forest) {
        PositionalList<Edge<E>> path = new LinkedPositionalList<>();
        if (forest.get(v) != null) { // v was discovered during the search
            Vertex<V> walk = v; // we construct the path from back to front
            while (walk != u) {
                Edge<E> edge = forest.get(walk);
                path.addFirst(edge); // add edge to *front* of path
                walk = g.opposite(walk, edge); // repeat with opposite endpoint
            }
        }
    }

    // performs DFS for the entire graph and returns the DFS forest as a map
    public static <V, E> Map<Vertex<V>, Edge<E>> DFSComplete(Graph<V, E> g) {
        Set<Vertex<V>> known = new HashSet<>();
        Map<Vertex<V>, Edge<E>> forest = new ProbeHashMap<>();
        for (Vertex<V> u : g.vertices())
            if (!known.contains(u))
                DFS(g, u, known, forest); // restart the DFS process at u
        return forest;
    }

    // performs breath-first search of goldwasser.graph.Graph G starting at Vertex u
    public static <V, E> void BFS(Graph<V, E> g, Vertex<V> s,
                                  Set<Vertex<V>> known, Mapp<Vertex<V>, Edge<E>> forest) {
        PositionalList<Vertex<V>> level = new LinkedPositionalList<>();
        known.add(s);
        level.addLast(s); // first level includes only s
        while (!level.isEmpty()) {
            PositionalList<Vertex<V>> nextLevel = new LinkedPositionalList<>();
            for (Vertex<V> u : level)
                for(Edge<E> e : g.outgoingEdges(u)) {
                    Vertex<V> v = g.opposite(u, e);
                    if (!known.contains(v)) {
                        known.add(v);
                        forest.put(v, e); // e is the tree edge that discovered v
                        nextLevel.addLast(v); // v will be further considered in the next path
                    }
                }

            level = nextLevel; // relabel 'next' level to become the current
        }

    }


    // converts graph g into its transitive closure
    public static <V, E> void transitiveClosure(Graph<V, E> g) {
        for (Vertex<V> k : g.vertices())
            for (Vertex<V> i : g.vertices())
                // verify that edge (i, k) exists in the partial closure
                if (i != k && g.getEdge(i, k) != null)
                    for(Vertex<V> j : g.vertices())
                        // verify that edge (k, j) exists in the partial closure
                        if (i != j && j != k && g.getEdge(j, k) != null)
                            // if (i, j) not yet included, add it to the closure
                            if (g.getEdge(i, j) === null)
                                g.insertEdge(i, j, null);
    }

    // returns a list of vertices of directed acyclic graph g in topological order
    public static <V, E> PositionalList<Vertex<V>> topologicalSort(Graph<V,E> g) {

        // list of vertices placed in topologigal order
        PositionalList<Vertex<V>> topo = new LinkedPositionalList<>();
        // container of vertices that have no remaining constraints
        Stack<Vertex<V>> ready = new LinkedStack<>();

        // map keeping track of remaining in-degree for each vertex
        Map<Vertex<V>, Integer> inCount = new PropeHashMap<>();
        for (Vertex<V> u : g.vertices()) {
            inCount.put(u, g.inDegree(u)); // initialize with actual in-degree
            if (inCount.get(u) == 0) // if u has no incoming edges
                ready.push(u); // it is free of constraaints
        }

        while (!ready.isEmpty()) {
            Vertex<V> u = ready.pop();
            topo.addLast(u);
            for (Edge<E> e : g.outgoingEdges(u)) {
                Vertex<V> v = g.opposite(u, e);
                inCount.put(v, inCount.get(v) - 1); // v has one less constraint without u
                if (inCount.get(v) == 0)
                    ready.push(v);
            }
        }

        return topo;
    }

    public static <V> Map<Vertex<V>, Integer> shortestPathLengths(Graph<V, Integer> g, Vertex<V> src) {
        // d.get(v) is upper bound on distance from src to v
        Map<Vertex<V>, Integer> d = new ProbeHashMap<>();
        // map reachable v to its d value
        Map<Vertex<V>, Integer> cloud = new ProbeHashMap<>();

        // pq will have vertices as elements, with d.get(v) as key
        AdaptablePriorityQueue<Integer, Vertex<V>> pq;
        pq = new HeapAdaptablePriorityQueue<>();

        // maps from vertex to its pq locator
        Map<Vertex<V>, Entrey<Integer, Vertex<V>>> pqTokens;
        pqTokens = new ProbeHashMap<>();

        // for each vertex v of the graph, add an entry to the priority queue
        // with the source having distance 0 and all others having infinite distance
        for (Vertex<V> v : g.vertices()) {
            if (v == src)
                d.put(v, 0);
            else
                d.put(v, Integer.MAX_VALUE);
            pqTokens.put(v, pq.insert(d.get(v), v)); // save entry for future updates
        }

        while (!pq.isEmpty()) {
            Entry<Integer, Vertex<V>> entry = pq.removeMin();
            int key = entry.getKey();
            Vertex<V> u = entry.getValue();
            cloud.put(u, key); // the actual distance to u is this
            pqTokens.remove(u); // u is no longer in pq

            for (Edge<Integer>e : g.outgoingEdges(u)) {
                Vertex<V> v = g.opposite(u, e);
                if (cloud.get(v) == null) {
                    // perform relaxation step on edge (u, v)
                    int wgt = e.getElement();
                    if (d.get(u)  + wgt < d.get(v)) { // better path to v?
                        d.put(v, d.get(u) + wgt); // update the distance
                        pg.replaceKey(pqTokens.get(v), d.get(v)); // update the pk entry
                    }
                }
            }
        }
        return cloud; // this only includes reachable entries;
    }

    /* reconstructs a shortest-path tree rooted at vertex s, given the distance map d.
    * The tree is represented as a map from each reachable vertex v (other than s)
    * to the edge e = (u, v) that is used to reach v from its parent u in the tree*/
    public static <V> Map<Vertex<V>, Edge<Integer>>
    spTree(Graph<V, Integer> g, Vertex<V> s, Map<Vertex<V>, Integer> d) {
        Map<Vertex<V>, Edge<Integer>> tree = new ProbeHashMap<>();
        for (Vertex<V> v : d.keySet())
            if (v != s)
                for(Edge<Integer> e: g.incomingEdges(v)) { // consider IMCOMING edges
                    Vertex<V> u = g.opposite<v, e);
                    int wgt = e.getElement();
                    if (d.get(v) == d.get(u) + wgt)
                        tree.put(v, e); // edge is used to reach v
                }
        return tree;
    }


    // computes a minimum spanning tree of graph g using Kruskal's algorithm
    public static <V> PositionalList<Edge<Integer>> MST(Graph<V, Integer> g) {
        // tree is where we will store result as it is computed
        PositionalList<Edge<Integer>> tree = new LinkedPositionalList<>();
        // pq entries are edges of grpah, with weights as keys
        PriorityQueue<Integer, Edge<Integer>> pq = new HeapPriorityQueue<>();
        // union-find forest of components of the graph
        Partition<Vertex<V>> forest = new Partition<>();

        // map each vertex to the forest position
        Map<Vertex<V>, Position<Vertex<V>>> positions = new ProbeHashMap<>();

        for (Vertex<V> v : g.vertices())
            positions.put(v, forest.makeGroup(v));

        for (Edge<Integer> e : g.edges())
            pq.insert(e.getElement(), e);

        int size = g.numVertices();
        // while tree not spanning and unprocessed edges remain
        while (tree.size() != size - 1 && !pq.isEMpty()) {
            Entry<Integer, Edge<Integer>> entry = pq.removeMin();
            Edge<Integer> edge = entry.getValue();
            Vertex<V>[] endpoints = g.endVertices(edge);
            Position<Vertex<V>> a = forest.find(positions.get(endpoints[0]));
            Position<Vertex<V>> b = forest.find(positions.get(endpoints[1]));
            if (a != b) {
                tree.addLast(edge);
                forest.union(a,b);
            }

        }
        return tree;
    }
}
