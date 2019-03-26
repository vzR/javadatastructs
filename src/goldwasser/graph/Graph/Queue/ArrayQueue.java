package goldwasser.graph.Graph.Queue;

public class ArrayQueue<E> implements Queue<E> {
    public static final int CAPACITY = 1000;  // default array capacity


    // instance variables
    private E[] data; // generic array used for storage
    private int f = 0; // index of the front element
    private int sz = 0; // current number of elements

    // constructors
    public ArrayQueue() { this(CAPACITY); } // coNSTRUCTS QUEUE WITH DEFAULT CAPACITY
    public ArrayQueue(int capacity) {
        data = (E[]) new Object[capacity];
    }

    // methods
    // returns the number of elements in the queue
    public int size() { return sz; }

    // tests whether the queue is empty
    public boolean isEmpty() { return sz == 0; }

    // inserts an element at the rear of the queue
    public void enqueue(E e) throws IllegalStateException {
        if (sz == data.length) throw new IllegalStateException("queue is full");
        int avail = (f + sz) % data.length; // use modular arithmetic
        data[avail] = e;
        sz++;
    }

    // returns, but does not remove, the first element of the queue (null if empty)
    public E first() {
        if (isEmpty()) return null;
        return data[f];
    }

    // removes and returns the first element of the queue (null if empty)
    public E dequeue() {
        if (isEmpty()) return null;
        E answer = data[f];
        data[f] = null; // aid garbace collection
        f = (f + 1) % data.length;
        sz--;
        return answer;
    }
}
