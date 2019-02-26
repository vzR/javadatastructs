package ListIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements List<E> {

    private class ArrayIterator implements Iterator<E> {
        private int j = 0; // index of the next element to report
        private boolean removable = false; // can remove be called at this time?

        /*tests whether the iterator has a next object
        * @return true if there are further objects, false othewise*/
        public boolean hasNext() {
            return j < size; // size is field of outer instance
        }

        /* returns the next object in the iterator
        *
        * @return next object
        * @Throws NoSuchElementException if there are no further elements*/

        public E next() throws NoSuchElementException {
            if (j == size) throw new NoSuchElementException("no next element");
            removable = true; // this element can be subsequently removed
            return data[j++];  // post-increment j, so it is ready for future call to next
        }

        /* removes the element returned by most recent call to next
        * @throws IllegalStateException if next has not yet been called
        * @throws IllegalStateException if remove was already called since recent next*/
        public void remove() throws IllegalStateException {
            if (!removable) throw new IllegalStateException("nothing to remove");
            ArrayList.this.remove(j - 1); // that was the last one returned
            j--; // next element has shifted one cell to the left
            removable = false; // do not allow remove again until next is called
        }
    } // end of nested ArrayIterator class

    public Iterator<E> iterator() {
        return new ArrayIterator(); // create a new instance of the inner class
    }



    // instance variables
    // default array capacity
    public static final int CAPACITY = 16;
    // generic array used for storage
    private E[] data;

     private int size = 0; // current number of elements

    // constructors
    public ArrayList() {
        this(CAPACITY);
    }
    public ArrayList(int capacity) {
        data = (E[]) new Object[capacity]; // safe cast
    }

    // public methods
    /*returns the number of elements in the array list*/
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E get(int i) throws IndexOutOfBoundsException {
        checkIndex(i, size);
        return data[i];
    }

    /* replaces the element at index i with e, and returns the replaced element*/
    public E set(int i, E e) throws IndexOutOfBoundsException {
        checkIndex(i, size);
        E temp = data[i];
        data[i] = e;
        return temp;
    }


    /* inserts element e to be at index i, shifting all subsequent elements later*/
    public void add(int i, E e) throws IndexOutOfBoundsException {
        checkIndex(i, size + 1);
       if (size == data.length)
           resize(2 * data.length); // double current capacity

        for (int k = size - 1; k >= i; k--)
            data[k+1] = data[k]; // start by shifting rightmost
        data[i] = e;
        size++;
    }

    /* removes / returns the element at index i, shifting subsequent elements later*/
    public E remove(int i) throws IndexOutOfBoundsException {
        checkIndex(i, size);
        E temp = data[i];
        for (int k = i; k < size - 1; k++)
            data[k] = data[k+1]; // shift elements to fill hole
        data[size-1] = null; // help garbage collection
        size--;
        return temp;
    }

    protected void resize(int capacity) {
        E[] temp = (E[]) new Object[capacity]; // safe cast
        for (int k = 0; k < size; k++)
            temp[k] = data[k];
        data = temp; // start using the new array
    }

    // utility method
    // check whether the given index is in the range [0, n-1]
    protected void checkIndex(int i, int n) throws IndexOutOfBoundsException {
        if (i < 0 || i > n)
            throw new IndexOutOfBoundsException("illegal index " + i);
    }

}
