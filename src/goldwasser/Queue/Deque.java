package goldwasser.Queue;

// interface for a double-ended queue; a collection of elements that can be inserted
// and removed at both ends; this interface is a simplified version ofo java.util.Deque
public interface Deque<E> {
    // returns the number of elements in the deque
    int size();

    // tests whether the deque is empty
    boolean isEmpty();

    // returns, but does not remove, the first element of the deque (null if empty)
    E first();

    // returns, but does not remove, the last element of the dque (null if empty)
    E last();

    // inserts an element at the front of the deque
    void addFirst(E e);

    // inserts an element at the back of the deque
    void addLast(E e);

    // removes and returns the first element of the deque (null if empty)
    E removeFirst();

    // removes and returns the last element of the deque (null if empty)
    E removeLast();
}
