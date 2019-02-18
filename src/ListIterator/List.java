package ListIterator;

public interface List<E> {
    // returns the number of elements in list
    int size();

    // returns whether the list is empty
    boolean isEmpty();

    // returns (but does not remove the element at index i
    E get(int i) throws IndexOutOfBoundsException;

}
