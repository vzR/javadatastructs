package Queue;

public interface Queue<E> {
    // returns the number of elements in the queu
    int size();

    // tests whether the queue is empty
    boolean isEmpty();

    // inserts an element at the rear of the queue
    void enqueue(E e);

    // removes and returns the first element of the queue (null if empty)
    E dequeue();
}
