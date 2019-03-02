package Tree;

import ListIterator.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class AbstractTree<E> implements Tree<E> {

    private class ElementIterator implements Iterator<E> {
        Iterator<Position<E>> posIterator = positions().iterator();
        public boolean hasNext() { return posIterator.next() }
        public E next() {
            return posIterator.next().getElement();
        }

        public void remove() { posIterator.remove(); }
    }

    public boolean isInternal(Position<E> p) {
        return numChildren(p) > 0;
    }

    public boolean isExternal(Position<E> p) {
        return numChildren(p) == 0;
    }

    public boolean isRoot(Position<E> p) {
        return p == root();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /* returns the number of levels separating Position p from the root */
    public int depth(Position<E> p) {
        if (isRoot(p)) return 0;
        else return 1 + depth(parent(p));
    }

    /* returns the height of the  subtree rooted at Position p*/
    public int height(Position<E> p) {
        int h = 0; // base case if p is external
        for (Position<E> c: children(p))
            h = Math.max(h, 1 + height(c));
        return h;
    }

    /* returns height of the tree */
    private int heightBad() {
        int h = 0;
        for (Position<E> p : positions())
            if (isExternal(p)) // only consider leaf positions
                h = Math.max(h, depth(p));
            return h;
    }

    // adds positions of the subtree rooted at Position p to the given snapshot
    private void preorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        snapshot.add(p); // for preorder, we add position p before exploring subtrees
        for (Position<E> c : children(p))
            preorderSubtree(c, snapshot);
    }

    // returns an iterable collection of positions of the tree, reported in preorder
    public Iterable<Position<E>> preorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) preorderSubtree(root(), snapshot); // fill snapshot recursively
        return snapshot;
    }

    // adds positions of the subtree rooted at Position p to the given snapshot
    private void postorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        for (Position<E> c : children(p))
            postorderSubtree(c, snapshot);
        snapshot.add(p); // for postoroder, we add position p after exploring subtrees
    }

    // returns an iterable collection of positions of the tree, reported in postorder
    public Iterable<Position<E>> postorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty())
            postorderSubtree(root(), snapshot); // fill snapshot recursively
        return snapshot;
    }

    public Iterable<Position<E>> positions() { return preorder(); }

    // returns an iterable collection of positions of the tree in breath-first order
    public Iterable<Position<E>> breathfirst() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) {
            Queue<Position<E>> fringe = new LinkedQueue<Position<E>>() {
            };
            fringe.enqueue(root());
            while (!fringe.isEmpty()) {
                Position<E> p = fringe.dequeue(); // remove from front of the queue
                snapshot.add(p);
                for (Position<E> c : children(p))
                    fringe.enqueue(c); // add children to back of queue
            }
        }

        return snapshot;
    }
}
