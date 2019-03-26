package goldwasser.Stack;

public interface Stack<E> {
    // returns the number of elements in the stack
    int size();
    // tests whether the stack is empty
    // return true if the stack is empty, false otherwise
    boolean isEmpty();

    // inserts an element at the top of the stack e - the element to be inserted
    void push(E e);

    // returns, but does not remove, the element at the top of the stack
    E top();

    // removes and returns the top element from the stack.
    // return element removed or null if empty
    E pop();
}
