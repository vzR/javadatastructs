package goldwasser.Queue;

public interface CircularQueue<E> extends Queue<E> {
    // rotates the first element of the queue to the back of the queue
    // this does nothing if the queue is empty
    void rotate();
}
