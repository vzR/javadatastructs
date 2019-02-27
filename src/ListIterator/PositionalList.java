package ListIterator;

import java.util.Iterator;

public interface PositionalList<E> {
    // returns the number of elements in the list
    int size();

    // tests whether the list is empty
    boolean isEmpty();

    // returns the first Position in the list (or null, if empty)
    Position<E> first();

    // returns the last Position in thte list (or null, if empty)
    Position<E> last();

    // returns the position immediately before Position p (or null, if p is first)
    Position<E> before(Position<E> p) throws IllegalArgumentException;

    // returns the Position immediately after Position p (or null, if p is last)
    Position<E> after(Position<E> p) throws IllegalArgumentException;

    // inserts element e at the front of the list and returns its new Position
    Position<E> addFirst(E e);

    // inserts element e at the back of the list and returns its new Position
    Position<E> addLast(E e);

    // inserts element e immediately before Position p and returns its new Position
    Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException;

    // inserts element e immediately after Position p and returns the new Position
    Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException;

    // replaces the element stored at Position p and returns the replaced element
    E set(Position<E> p, E e) throws IllegalArgumentException;

    // removes the element stored at Position p and returns it (invalidating p)
    E remove(Position<E> p) throws IllegalArgumentException;

    Iterator<E> iterator();

}
