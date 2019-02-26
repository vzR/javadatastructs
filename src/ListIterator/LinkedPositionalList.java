package ListIterator;

public class LinkedPositionalList<E> implements PositionalList<E> {
    /* nested NOde class*/
    private static class Node<E> implements Position<E> {
        private E element;
        private Node<E> prev;
        private Node<E> next;

        public Node(E e, Node<E> p, Node<E> n) {
            element = e;
            prev = p;
            next = n;
        }

        public E getElement() throws IllegalStateException {
            if (next == null)
                throw new IllegalStateException("position is no longer valid");
            return element;
        }

        public Node<E> getPrev() {
            return prev;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setElement(E e) {
            element = e;
        }

        public void setPrev(Node<E> p) {
            prev = p;
        }

        public void setNext(Node<E> n) {
            next = n;
        }
    } /* end of nested node class */

    // instance variables of the LInkedPositionalList
    private Node<E> header;
    private Node<E> trailer;
    private int size = 0;

    // constructs a new empty list
    private LinkedPositionalList() {
        header = new Node<>(null, null, null); // create header
        trailer = new Node<>(null, header, null); // trailer is preceded by header
        header.setNext(trailer); // header is followed by trailer
    }

    // private utilities
    /* validates the position and returns it as a node*/
    private Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node)) throw new IllegalArgumentException("invalid p");
        Node<E> node = (Node<E>) p; // safe cast
        if (node.getNext() == null) // convention for defunct node
            throw new IllegalArgumentException("p is no longer in the list");
        return node;
    }

    // returns the given node as a Position (or null, if it is a sentinel)
    private Position<E> position(Node<E> node) {
        if (node == header || node == trailer)
            return null; // do not expose user to the sentinels
        return node;
    }

    // public accessor methods
    // returns the number of elements in the linked list
    public int size() {
        return size;
    }

    // tests whether the list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // returns the first position in the linked list (or null, if empty)
    public Position<E> first() {
        return position(header.getNext());
    }

    // returns the last position in the linked list (or null, if empty)
    public Position<E> last() {
        return position(trailer.getPrev());
    }

    // returns the position immediately before Position p (or null, if p is first
    public Position<E> before(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return position(node.getPrev());
    }


    // returns the position immediately after position p (or null, if p is last)
    public Position<E> after(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return position(node.getNext());
    }

    // private utilities
    // adds element e to the linked likst between the given nodes
    private Position<E> addBetween(E e, Node<E> pred, Node<E> succ) {
        Node<E> newest = new Node<>(e, pred, succ); // create and link a new node
        pred.setNext(newest);
        succ.setPrev(newest);
        size++;
        return newest;
    }

    // public update methods
    // inserts element e at the front of the linked list and returns its new Position
    public Position<E> addFirst(E e) {
        return addBetween(e, header, header.getNext()); // just after the header
    }

    public Position<E> addLast(E e) {
        return addBetween(e, trailer.getPrev(), trailer); // just before the trailer
    }

    // inserts element E immediately before Position p, and returns its new Position
    public Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return addBetween(e, node.getPrev(), node);
    }

    // inserts element e immediately after position p, and returns its new position
    public Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return addBetween(e, node, node.getNext());
    }

    // replaces the element stored at position p and returns the replaced element
    public E set(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> node = validate(p);
        E answer = node.getElement();
        node.setElement(e);
        return answer;
    }

    // removes the element stored at position P and returns it (invalidating p)
    public E remove(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        Node<E> predecessor = node.getPrev();
        Node<E> successor = node.getNext();
        predecessor.setNext(successor);
        successor.setPrev(predecessor);
        size--;
        E answer = node.getElement();
        node.setElement(null); // add garbage collection
        node.setNext(null); // add convention for defunct node
        node.setPrev(null);
        return answer;
    }
}